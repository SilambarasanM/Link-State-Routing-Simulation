
public class Edge {
	private static Integer edgeCounter =0;
	public Integer edgeID;
	public Integer source;
	public Integer destination;
	public Integer cost;
	
	/**
	 * Parameterized constructor for Edge connecting
	 * source, destination with a cost.
	 * 
	 * @param source - source of the edge
	 * @param destination - end of the edge
	 * @param cost - cost of the edge
	 */
	public Edge(Integer source, Integer destination, Integer cost) {
		edgeID = ++edgeCounter;
		this.source = source;
		this.destination = destination;
		this.cost = cost;
	}
	
	/**
	 * To get the source of the edge
	 * 
	 * @return - source for the current edge
	 */
	public Integer getSource(){return source;}
	
	/**
	 * To get the destination of the edge
	 * 
	 * @return - end of the current edge
	 */
	public Integer getDestination(){return destination;}
	
	/**
	 * To get cost for the current edge
	 * 
	 * @return - cost of the current edge
	 */
	public Integer getCost(){return cost;}

}
