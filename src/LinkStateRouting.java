import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class LinkStateRouting {
	
	/**
	 * The main function from where the I/O console runs
	 * 
	 * @param args - optional command line arguments
	 */
	public static void main(String[] args) {
		int choice = 0;
		boolean setupDone = false, removedNode = false;
		Integer source = 0, destination = 0, nodeID = 0, remaining = 0;
		NetworkTopology nt = new NetworkTopology();
		do{
			displayMenu();
				choice = getChoice(); 
				switch(choice){
				//Create Topology
				case 1: ArrayList<String> input = readFile();
						if(setupDone){
							System.out.println("Network already Setup!");
							break;
						}
						if (fileValidator(input)){
							nt = new NetworkTopology();
							nt.setup(input);
							setupDone=true;
							System.out.println(nt);
							System.out.println("Topology Created!");
							nt.dispConTab();
						}
						else{System.out.println("Invalid File!");}
						break;

				//Build Connection Table
				case 2:	if(setupDone){
							System.out.print("Enter the source node: ");
							source = getChoice();
							nt.buildRoutingTable(source);
						}
						else{System.out.println("Setup the Netork Topology first!");}
						break;	
						
				//Find Optimal Path
				case 3: if(setupDone){
							if(source == 0){
								System.out.print("Enter the source node: ");
								source = getChoice();
							}
							System.out.print("Enter the destination node: ");
							destination = getChoice();
							nt.getShortestPath(source, destination);
						}
						else{System.out.println("Setup the Netork Topology first!");}
						break;
						
				//Modify Topology
				case 4: if(setupDone){
							if(removedNode){
								System.out.print("Do you want to add a removed router?"
													+" (1 - Yes | 2 - No): ");
								int op = getChoice();
								if(op==1){
									System.out.print("Enter the Router ID to be added back: ");
									int add = getChoice();
									nt.addNode(add);
								}
							}
							if (nt.getAvailableNodes().size()!=0){
								System.out.print("Do you want to remove a router?"
													+" (1 - Yes | 2 - No): ");
								int op = getChoice();
								if(op ==1){
									System.out.print("Enter the Router ID to be removed: ");
									nodeID = getChoice();
									remaining = nt.removeNode(nodeID);
									removedNode = true;
									break;
								}
							}else{ 
								if (nt.getAvailableNodes().size()==0)
									System.out.println("All the routers are down!");
							}
						}
						else{System.out.println("Setup the Netork Topology first!");}
						break;
						
				case 5: System.out.println("Good Bye!"); 
						break;
						
				default:System.out.println("\nEnter a valid input between 1-5!\n");
				}
			
		}while(choice!=5);
	}

	/**
	 * To display the menu for every iteration
	 */
	public static void displayMenu()
	{
		 System.out.println("\n\n\t\t\t======================================");
		 System.out.println("\t\t\tLINK-STATE ROUTING PROTOCOL SIMULATION");
		 System.out.println("\t\t\t======================================\n");
		 System.out.println("\t\t\t  (1) Create a Network Topology");
		 System.out.println("\t\t\t  (2) Build a Connection Table");
		 System.out.println("\t\t\t  (3) Find Optimal Path to Destination");
		 System.out.println("\t\t\t  (4) Modify a Topology");
		 System.out.println("\t\t\t  (5) Exit ");
		 System.out.println();
		 System.out.print("Enter your choice: ");
		return;		
	}
	
	/**
	 * To get an integer input from user
	 * 
	 * @return - the integer entered by the user
	 */
	public static int getChoice(){
		Scanner sc = new Scanner(System.in);
		int choice = 0;
		try{
			choice = sc.nextInt();}
		catch(Exception ex) {
            System.out.println("Error reading input '" + 
            		(choice==0?"Invalid Input":"Valid Input 1-5") + "' "+ex.toString());     
            sc.next();
        }
		return choice;
	}
	
	/**
	 * To read the input file and return the file content
	 * 
	 * @return - String array list of text in the file
	 */
	public static ArrayList<String> readFile(){
		System.out.print("Enter the File Name: ");
		Scanner scanner = new Scanner(System.in);
		String fileName = scanner.nextLine();
		String line = null;
		ArrayList<String> topology = new ArrayList<String>();
		try {
            // FileReader reads text file in the default encoding.
            FileReader fileReader = new FileReader(fileName);

            // Wrapping FileReader in BufferedReader.
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
                topology.add(line);
            }   

            // Closing file.
            bufferedReader.close();         
        }
        catch(FileNotFoundException ex) {
            System.out.println("Unable to open file '" + fileName + "'");                
        }
        catch(IOException ex) {
            System.out.println("Error reading file '" + fileName + "'");                  
        }
		return topology;
	}
	
	/**
	 * To validate the input file for topology matrix
	 * 
	 * @param input - content of the input file
	 * @return - true if correct format else false
	 */
	public static boolean fileValidator(ArrayList<String> input){
		int rowCount = input.size();
		int colCount = 0;
		if(input.size()<1)
			return false;
		for(int i =0; i < rowCount;i++){
			String row[] = input.get(i).split(" ");
			colCount = row.length;
			
			if (rowCount!= colCount){
				System.out.println("Error: Row and column counts doesn't match! Input a square matrix..");
				return false;
			}
			else{
				for(int j = 0; j < colCount;j++){
					try{
						Integer.parseInt(row[j]);
					}
					catch(Exception NumberFormatException){
						System.out.println("Error: "+row[j]+" is not a valid integer!");
						return false;
					}
				}
			}
		}
		return true;
	}
}
