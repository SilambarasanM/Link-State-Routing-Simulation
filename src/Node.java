import java.util.ArrayList;

public class Node {
	public final Integer nodeID;
	private static Integer nodeCounter = 0;
	protected ArrayList<Edge> outgoingEdges;
	private Integer[] cost;
	private boolean status;
	
	/**
	 * Parameterized constructor with the row from the 
	 * text file along with the number of nodes
	 * 
	 * @param row - a row of topology matrix
	 * @param nodeCount - number of nodes in the input
	 */
	public Node(String row, int nodeCount) {
		nodeID = ++nodeCounter;
		outgoingEdges = new ArrayList<Edge>();
		cost = new Integer[nodeCount];
		String[] line = row.split(" ",nodeCount);
		for(int i=0;i<nodeCount;i++){
			cost[i] = Integer.parseInt(line[i]);
			if(cost[i]>0){
				outgoingEdges.add(new Edge(nodeID,i+1,cost[i]));
			}
		}
		status = true;
	}
	
	/**
	 * To get the current status of a node
	 * 
	 * @return - current status of the node
	 */
	public boolean getStatus(){return status;}
	
	/**
	 * To set the status of a node as inactive
	 */
	public void setInactive(){status = false;}
	
	/**
	 * To set the status of a node as active
	 */
	public void setActive(){status = true;}
	
	/**
	 * To get the node identifier for the node
	 * 
	 * @return the node identifier for the node
	 */
	public Integer getID(){return nodeID;}
	
	/**
	 * To get the cost of the edge from current node to
	 * a target node.
	 * 
	 * @param target - target node to which the edge is connected
	 * @return - cost of the edge connecting the current node and
	 * 			the target node
	 */
	public Integer getEdgeCost(Integer target){
		Integer c = Integer.MAX_VALUE;
		for(Edge e: outgoingEdges){
			if(e.getDestination() == target){
				c=e.getCost();
				return c;
			}
		}
		return c;
	}
}
