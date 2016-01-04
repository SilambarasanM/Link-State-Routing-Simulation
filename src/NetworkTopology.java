import java.util.*;
import javafx.collections.ObservableList;

public class NetworkTopology {
	public int[][] costMatrix;
	public int noOfNodes;
	public HashMap<Integer, Node> nodes;
	public HashMap<Integer, Node> removedNodes;
	
	/**
	 * Public default constructor
	 */
	public NetworkTopology() {
		nodes = new HashMap<Integer, Node>();
		removedNodes = new HashMap<Integer, Node>();
		noOfNodes = 0;
	}
	
	/**
	 * To setup a Network Topology
	 * 
	 * @param matrix - Topology matrix
	 */
	public void setup(ArrayList<String> matrix){
		noOfNodes = matrix.size();
		int rowCount=0;
		costMatrix = new int[noOfNodes][noOfNodes];
		for(String row: matrix){
			Node n = new Node(row, noOfNodes);
			nodes.put(n.getID(), n);
			String[] cost = row.split(" ",noOfNodes);
			for(int i=0;i<noOfNodes;i++){
				costMatrix[rowCount][i] = Integer.parseInt(cost[i]);
			}
			rowCount++;
		}
		return;
	}
	
	/**
	 * String representation of the Network Topology
	 */
	public String toString()
	{
		String output = "Cost Matrix:\n";
		for (int i=0; i<nodes.size()/*noOfNodes*/; i++){
			if(nodes.get(i+1)!=null){
				for (int j=0; j< nodes.size()/*noOfNodes*/; j++){
					int cost = costMatrix[i][j];
					if(nodes.get(j+1)!=null){
						output+="\t"+String.valueOf(cost);
					}
				}
				output+="\n";
			}
		}
		return output;
	}
	
	/**
	 * To validate whether a node is valid or not.
	 * 
	 * @param nodeID - node identifier
	 * @return - true if node is valid else false
	 */
	public boolean validateNode(Integer nodeID){		
		if(!nodes.containsKey(nodeID)){
			if(!removedNodes.containsKey(nodeID))
				System.out.println("Router "+nodeID+" not found in the network!");
			else
				System.out.println("Router "+ nodeID +" already down!");
			return false;
			}
		return true;
	}
	
	/**
	 * To remove a node form the network
	 * 
	 * @param nodeID - identifier of the node to be removed
	 * @return - remaining number of nodes in the network;
	 * 			returns -1 if node is invalid
	 */
	public Integer removeNode(Integer nodeID){
	
		if(validateNode(nodeID))
		{
			Node n = nodes.get(nodeID);
			nodes.remove(n.getID());
			removedNodes.put(n.getID(), n);
			n.setInactive();
			System.out.println("Router "+ nodeID +" is down!");
			return nodes.size();
		}
		return -1;
	}
	
	/**
	 * To add a node to the network
	 * 
	 * @param nodeID - identifier of the node to be added
	 * 					to the network.
	 * @return - number of valid nodes in network; returns
	 * 				-1 if not valid
	 */
	public Integer addNode(Integer nodeID){
		
		if(!validateNode(nodeID) && removedNodes.containsKey(nodeID))
		{
			Node n = removedNodes.get(nodeID);
			nodes.put(n.getID(), n);
			removedNodes.remove(n.getID());
			n.setActive();
			System.out.println("Router "+ nodeID +" up and running!");
			return nodes.size();
		}
		return -1;
	}
	
	/**
	 * To build a routing table for the given node
	 * 
	 * @param source - source node for which routing table
	 * 					is to be built.
	 * @return - The routing table in String key-value 
	 * 				pair format.
	 */
	public Map<String, String> buildRoutingTable(int source){ 
		
		Map<String, String> item = new HashMap<String, String>();
		
		if(!validateNode(source)){return null;}
		else{
		
		HashMap table = runDijkstra(source);
		System.out.println("\nCONNECTION TABLE - ROUTER "+source+":\n");
		System.out.println("\t\tDESTINATION\t\tINTERFACE");
		for(Iterator<Integer> i = table.keySet().iterator(); i.hasNext();) {
			Integer key = i.next();
			Vector values = (Vector)table.get(key);
			String node = (String)values.get(0);
			String[] splitString = node.split("-");
			System.out.println("\t\t      "+key + "\t\t\t     " + (source==key?"--":(splitString[1].equals("0")?"--":splitString[1])));
			item.put(key.toString(), (source==key?"--":(splitString[1].equals("0")?"--":splitString[1])));
			
			}
		return item;
		}
	}
	
	/**
	 * To run the Dijkstra's Algorithm to find shortest path.
	 * 
	 * @param source - Source node for the algorithm
	 * @return - Optimal Cost and Predecessors for every node from source node
	 */
	public HashMap runDijkstra(Integer source){
		Set<Integer> nodesCovered = new HashSet<Integer>();
		Set<Integer> nodesToBeCovered = new HashSet<Integer>();
		
		HashMap<Integer, Integer> cost = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer> predecessors = new HashMap<Integer, Integer>();
	    		
		Node sNode = nodes.get(source);
		cost.put(sNode.getID(), 0);
		nodesCovered.add(sNode.getID());	
		
	    for(Iterator<Integer> i = nodes.keySet().iterator(); i.hasNext();) {
			Integer key = i.next();
			Node  n   = ((Node)nodes.get(key));
			if(n!=sNode){
			nodesToBeCovered.add(n.getID());
			cost.put(n.getID(), sNode.getEdgeCost(n.getID()));
			predecessors.put(n.getID(), sNode.getID());
			}
	    }
	    
	    while (nodesToBeCovered.size() > 0) {
	    	Integer minCost = Integer.MAX_VALUE, minNodeID = 0, altNodeID = 0;
	    	
			for(Integer n: nodesToBeCovered){
				if(cost.get(n) < minCost){
					minCost = cost.get(n);
					minNodeID = n;
				}
			}
			
			if(minNodeID!= 0){
				nodesCovered.add(minNodeID);

				nodesToBeCovered.remove(minNodeID);
				Node minNode = nodes.get(minNodeID);
			
				for(Integer nID: nodesToBeCovered){
					Integer newCost = cost.get(nID)==Integer.MAX_VALUE?
										(minNode.getEdgeCost(nID)==Integer.MAX_VALUE? 
												Integer.MAX_VALUE: minCost + minNode.getEdgeCost(nID)):
													(minNode.getEdgeCost(nID)==Integer.MAX_VALUE? 
															cost.get(nID): minCost + minNode.getEdgeCost(nID));
				if(newCost < cost.get(nID)){
					cost.replace(nID, newCost);
					predecessors.replace(nID, minNodeID);
				}
				}
			}
			else{
				Integer toBeRemoved=0;
				for(Integer nID: nodesToBeCovered){
					toBeRemoved = nID;
					break;
				}
				nodesCovered.add(toBeRemoved);
				nodesToBeCovered.remove(toBeRemoved);
				cost.replace(toBeRemoved, Integer.MAX_VALUE);
				predecessors.replace(toBeRemoved, minNodeID);
			}
			

	    }
			HashMap table = new HashMap();
			for(Iterator<Integer> i = nodes.keySet().iterator(); i.hasNext();) {
				Integer key = i.next();
				Node  n   = ((Node)nodes.get(key));
				String shortestPath = getPath(source, key, predecessors);
				if(shortestPath.equals(source.toString()))
					shortestPath = "N/A (Same node)";
				Vector values = new Vector(2);
				values.setSize(2);
				values.set(0, shortestPath);
				values.set(1, cost.get(key));
				table.put(n.getID(), values);
				}
	    
	    
	    return table;
	}
	
	/**
	 * To compute the path from Dijkstra's predecessors.
	 * 
	 * @param source - source node of the path
	 * @param destination - destination node of the path
	 * @param predecessors - predecessors for each node
	 * @return - computed path as per Dijkstra's algorithm
	 */
	public String getPath(Integer source, Integer destination, HashMap<Integer, Integer> predecessors){
		if(source == destination)
			return String.valueOf(source);
		if(destination == null)
			return "null";
		else{
			return getPath(source, predecessors.get(destination), predecessors)+ "-" + String.valueOf(destination);
		}
	}
	
	/**
	 * To compute the shortest path from the Dijkstra's Algorithm.
	 * 
	 * @param source - source node for the path
	 * @param target - destination node for the path
	 * @return - optimal path from source to destination
	 */
	public String getShortestPath(Integer source ,Integer target){
		String sPath ="";
		
		if(!validateNode(target)){
			return sPath;
		}
		else{
			if(!validateNode(source)){
				return sPath;
			}
			else{
				HashMap table = runDijkstra(source);
				//System.out.println("Dijkstra Completed!");
				System.out.println("\nOPTIMAL PATH FROM ROUTER "+source+" TO ROUTER "+target+":\n");
				System.out.println("\t\tDESTINATION\t\tPATH\t\tCOST");
				Vector values = (Vector) table.get(target);
				String path = (String)values.get(0);
				if (!path.contains("null")){
					System.out.println("\t\t     "+target+"\t\t\t"+values.get(0)+ "\t\t"+values.get(1));
					sPath = "PATH:\t"+values.get(0)+ "\t\tCOST:\t"+values.get(1);
					return sPath;
				}
				else{
					System.out.println("\t\t"+target+"\t\tNot Reacheable\t\t-1");
					sPath = "PATH:\tNot Reacheable\t\tCOST:\t-1";
					return sPath;
				}
				}
			}
	}
	
	/**
	 * To get the currently available nodes in the network.
	 * 
	 * @return - returns the identifiers of currently available nodes
	 */
	public ArrayList<Integer> getAvailableNodes(){
		ArrayList<Integer> nAvail = new ArrayList<Integer>();
		for(Iterator<Integer> i = nodes.keySet().iterator(); i.hasNext();) {
			Integer key = i.next();
			nAvail.add(key);
		}
		return nAvail;
	}
	
	/**
	 * To get the currently available nodes in the network.
	 * 
	 * @return - list of identifiers of currently unavailable nodes
	 */
	public ArrayList<Integer> getUnavailableNodes(){
		ArrayList<Integer> nAvail = new ArrayList<Integer>();
		for(Iterator<Integer> i = removedNodes.keySet().iterator(); i.hasNext();) {
			Integer key = i.next();
			nAvail.add(key);
		}
		return nAvail;
	}
	
	/**
	 * To compute the connection table for all the nodes.
	 */
	public void dispConTab(){
		for(Iterator<Integer> i = nodes.keySet().iterator(); i.hasNext();) {
			Integer key = i.next();
			buildRoutingTable(key);
		}
	}
}