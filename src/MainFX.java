import java.awt.Font;
import java.io.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.*;
import javafx.scene.effect.Light.Distant;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

public class MainFX extends Application{
	
	Button btnscene, btn1scene, btn2scene, btn3scene, btn4scene;
	Button btnscene1, btn1scene1, btn2scene1, btnscene2, btnscene3;
	Button btn1scene2, btn1scene3, btnscene4, btn1scene4, btn2scene4, btn3scene1;
	ComboBox cbscene2, cbscene3, cb1scene3, cbscene4, cb1scene4;
	Text txtscene, txtscene1, txtscene2, txtscene3, txtscene4;
	TableColumn<Map.Entry<String, String>, String> nodeTarget, nodeInterface;
	BorderPane bpscene, bpscene1, bpscene2, bpscene3, bpscene4;
	Label lblscene1, lblscene2, lbl1scene2, lblscene3, lbl1scene3;
	Label lbl2scene3, lblscene4, lbl1scene4, lbl2scene4;
    Scene scene, scene1, scene2, scene3, scene4;
    NetworkTopology nt = new NetworkTopology();
    Stage thestage;
    String errorMsg="";
    FileChooser fileChooser;
	TextArea tfscene1;
	DropShadow ds;
	Lighting l;
    boolean setupDone=false;
    TableView table;
    
    /**
     * Main function for the program to trigger GUI
     * @param args - optional command line parameters
     */
	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * Function overriding from Application class to invoke GUI
	 * 
	 * @param primaryStage - the main screen of GUI
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		//Common components to be accessed by all scenes
		thestage=primaryStage;
		
		l = new Lighting();
		Distant light = new Distant();
	    light.setAzimuth(-135.0f);
        l.setLight(light);
        l.setSurfaceScale(5.0f);
        ds = new DropShadow();
		ds.setOffsetY(2.0);
        ds.setOffsetX(2.0);
        ds.setColor(Color.BLACK);
        
        //Setting up components for scenes
		addScene();
		addScene1();
        addScene2();
        addScene3();
        addScene4();
        
        //Making scenes from the panes
        scene = new Scene(bpscene, 600, 350);
        scene1 = new Scene(bpscene1, 600, 350);
        scene2 = new Scene(bpscene2, 600, 350);
        scene3 = new Scene(bpscene3, 600, 350);
        scene4 = new Scene(bpscene4, 600, 350);
        
        //Main Stage setup
        primaryStage.setTitle("Link-State Simulator");
        primaryStage.setScene(scene);
        primaryStage.show();

	}
	
	/**
	 * Setup functionalities for each button
	 * 
	 * @param e - button clicked
	 */
	public void ButtonClicked(ActionEvent e)
    {
        if (e.getSource()==btnscene1){
        	lblscene1.setText("");
        	tfscene1.clear();
        	thestage.setScene(scene);
        }
        else if ( e.getSource()==btnscene2){
        	table.setDisable(true);
        	table.setItems(null);
        	table.setPrefHeight(50); 
        	thestage.setScene(scene);
        	lbl1scene2.setText("");
        }
        else if ( e.getSource()==btnscene4){ 
        	thestage.setScene(scene);
        	lbl1scene4.setText("");
        }
        else if (e.getSource()==btnscene3)
        	thestage.setScene(scene);
        else if (e.getSource()==btnscene)
            thestage.close();
        else if (e.getSource()==btn1scene)
            thestage.setScene(scene1);
        else if (e.getSource()==btn2scene1){
            tfscene1.clear();
            lblscene1.setText("");
        }
        else if (e.getSource()==btn1scene2){
        	lbl1scene2.setText("");
        	if (cbscene2.getValue() != null && 
                    !cbscene2.getValue().toString().isEmpty()){
        		Integer source = (Integer) cbscene2.getValue();
        		Map<String, String> map = nt.buildRoutingTable(source);
        		ObservableList<Map.Entry<String, String>> items = FXCollections.observableArrayList(map.entrySet());
        		table.setItems(items);
        		table.setPrefHeight(items.size() * 25 + 25); 
        		table.setDisable(false);
        	}
        	else{
        		lbl1scene2.setText("Select a router from the dropdown and then click Submit!");
        	}
        }    
        else if (e.getSource()==btn3scene1){
        	 fileChooser.getExtensionFilters().addAll(
                     new FileChooser.ExtensionFilter("TXT Files (*.txt)", "*.txt"),
                     new FileChooser.ExtensionFilter("All Files", "*.*")
                 );
    		File file = fileChooser.showOpenDialog(thestage);
    		if (file != null) {
    			tfscene1.clear();
                lblscene1.setText("File "+file.getName()+" is now open");
                tfscene1.setText(readFile(file));
            }
        }
        else if (e.getSource()==btn2scene){
        	lbl1scene2.setText("");
        	cbscene2.getItems().clear();
        	ArrayList<Integer> nAvail = nt.getAvailableNodes();
        	if(nAvail.size()>0){
        		cbscene2.getItems().addAll(nAvail);
        	}
        	else{
        		lbl1scene2.setText("No routers available!");
        	}
        	thestage.setScene(scene2);
        }
        else if (e.getSource()==btn3scene){
        	lbl2scene3.setText("");
        	cbscene3.getItems().clear();
        	cb1scene3.getItems().clear();
        	ArrayList<Integer> nAvail = nt.getAvailableNodes();
        	if(nAvail.size()>0){
        		cbscene3.getItems().addAll(nAvail);
        		cb1scene3.getItems().addAll(nAvail);
        	}
        	else{
        		lbl2scene3.setText("No routers available!");
        	}
        	thestage.setScene(scene3);
        }
        else if (e.getSource()==btn1scene1){
        	String text = tfscene1.getText();
    		if(text.isEmpty() || text.length() ==0)
    		{lblscene1.setText("Enter a cost matrix first!");}
    		else{
    			createTopology();
    			if(setupDone){
    				btn2scene.setDisable(false);
    				btn3scene.setDisable(false);
    				btn4scene.setDisable(false);
    				btn1scene.setDisable(true);
    				btn1scene1.setDisable(true);
    			}
    		}
        }
        else if (e.getSource()==btn1scene3){
        	lbl2scene3.setText("");
        	if (cbscene3.getValue() != null && cb1scene3.getValue() != null
                    && !cbscene3.getValue().toString().isEmpty() && 
                    !cb1scene3.getValue().toString().isEmpty()){
        		Integer source = (Integer) cbscene3.getValue();
        		Integer destination = (Integer) cb1scene3.getValue();
        		lbl2scene3.setText(nt.getShortestPath(source, destination));
        	}
        	else{
        		lbl2scene3.setText("Select a router from the dropdown and then click Submit!");
        	}
        }
        else if (e.getSource()==btn4scene){
        	lbl1scene4.setText("");
        	cbscene4.getItems().clear();
        	ArrayList<Integer> nAvail = nt.getAvailableNodes();
        	if(nAvail.size()>0){
        		cbscene4.getItems().addAll(nAvail);
        	}
        	else{
        		lbl1scene4.setText("No routers available!");
        	}
        	cb1scene4.getItems().clear();
        	ArrayList<Integer> nUnavail = nt.getUnavailableNodes();
        	if(nUnavail.size()>0){
        		cb1scene4.getItems().addAll(nUnavail);
        		btn2scene4.setDisable(false);
        		cb1scene4.setDisable(false);
        	}
        	else{
        		btn2scene4.setDisable(true);
        		cb1scene4.setDisable(true);
        	}
        	thestage.setScene(scene4);
        }
        else if(e.getSource()== btn1scene4){
        	lbl1scene4.setText("");
        	if (cbscene4.getValue() != null && 
                    !cbscene4.getValue().toString().isEmpty()){
        		Integer nodeID = (Integer) cbscene4.getValue();
        		nt.removeNode(nodeID);
        		String msg = "Router "+nodeID+" is down!";
        		lbl1scene4.setText(msg);
        		cbscene4.getItems().clear();
            	ArrayList<Integer> nAvail = nt.getAvailableNodes();
            	if(nAvail.size()>0){
            		cbscene4.getItems().addAll(nAvail);
            		setupDone = true;
        			btn2scene.setDisable(false);
        			btn3scene.setDisable(false);
        			btn4scene.setDisable(false);
            	}
            	else{
            			lbl1scene4.setText(msg+"\t\tNo more routers available in the network!");
            			setupDone = false;
            			btn2scene.setDisable(true);
            			btn3scene.setDisable(true);
            			btn4scene.setDisable(true);
            	}
            	
            	cb1scene4.getItems().clear();
            	ArrayList<Integer> nUnavail = nt.getUnavailableNodes();
            	if(nUnavail.size()>0){
            		cb1scene4.getItems().addAll(nUnavail);
            		btn2scene4.setDisable(false);
            		cb1scene4.setDisable(false);
            		setupDone = true;
        			btn2scene.setDisable(false);
        			btn3scene.setDisable(false);
        			btn4scene.setDisable(false);
            	}
            	else{
            		btn2scene4.setDisable(true);
            		cb1scene4.setDisable(true);
            	}
        	}
        	else if(!setupDone)
        		lbl1scene4.setText("No more routers available in the network!");
        	else{
        		lbl2scene3.setText("Select a router from the dropdown and then click Submit!");
        	}
        }
        else if(e.getSource()== btn2scene4){
        	lbl1scene4.setText("");
        	if (cb1scene4.getValue() != null && 
                    !cb1scene4.getValue().toString().isEmpty()){
        		Integer nodeID = (Integer) cb1scene4.getValue();
        		nt.addNode(nodeID);
        		String msg = "Router "+nodeID+" is now up and running!";
        		lbl1scene4.setText(msg);
        		cb1scene4.getItems().clear();
            	ArrayList<Integer> nUnavail = nt.getUnavailableNodes();
            	if(nUnavail.size()>0){
            		cb1scene4.getItems().addAll(nUnavail);
            		setupDone = true;
        			btn2scene.setDisable(false);
        			btn3scene.setDisable(false);
        			btn4scene.setDisable(false);
            	}
            	else{
            		btn2scene4.setDisable(true);
            		cb1scene4.setDisable(true);
            	}
            	
            	cbscene4.getItems().clear();
            	ArrayList<Integer> nAvail = nt.getAvailableNodes();
            	if(nAvail.size()>0){
            		cbscene4.getItems().addAll(nAvail);
            	}
            	else{
            			lbl1scene4.setText(msg+"\t\tNo more routers available in the network!");
            			setupDone = false;
            			btn2scene.setDisable(true);
            			btn3scene.setDisable(true);
            			btn4scene.setDisable(true);
            	}
        	}
        	else if(!setupDone)
        		lbl1scene4.setText("No more routers available in the network!");
        	else{
        		lbl2scene3.setText("Select a router from the dropdown and then click Submit!");
        	}
        }
    }
	
	/**
	 * To create the network topology
	 */
	public void createTopology(){
		String output ="";
		String text = tfscene1.getText();
		String[] textList = text.split("\n");
		ArrayList<String> input = new ArrayList<String>();
		for(int i =0; i< textList.length; i++){
			input.add(textList[i]);
		}
		if (fileValidator(input)){
			nt = new NetworkTopology();
			nt.setup(input);
			setupDone=true;
			output = "Topology Created!";
		}
		else{
			output = errorMsg;}
		lblscene1.setText(output);
		tfscene1.clear();
	}
	
	/**
	 * To validate the topology matrix in the file.
	 * 
	 * @param input - content of input file
	 * @return returns false if invalid else true
	 */
	public boolean fileValidator(ArrayList<String> input){
		int rowCount = input.size();
		int colCount = 0;
		for(int i =0; i < rowCount;i++){
			String row[] = input.get(i).split(" ");
			colCount = row.length;
			
			if (rowCount!= colCount){
				errorMsg = "Error: Row and column counts doesn't match. Input only square matrix.";
				return false;
			}
			else{
				for(int j = 0; j < colCount;j++){
					try{
						Integer.parseInt(row[j]);
					}
					catch(Exception NumberFormatException){
						errorMsg = "Error: "+row[j]+" is not a valid integer. Input only integer values.";
						return false;
					}
				}
			}
		}
		return true;
	}
	
	/**
	 * Setting up first scene in the application
	 */
	public void addScene(){
		bpscene = new BorderPane();
		
		HBox hbox = new HBox();
		hbox.setPadding(new Insets(20, 5, 20, 10));
		hbox.setSpacing(10);
		hbox.setStyle("-fx-background-color: #336699;");
		txtscene = new Text("Link-State Routing Simulator"); 
		txtscene.setEffect(l);		
		txtscene.setStyle("-fx-font: 22 arial; -fx-base: #cce6ff; -fx-font-weight: bold;");
		hbox.setAlignment(Pos.CENTER);
		hbox.getChildren().addAll(txtscene);
		bpscene.setTop(hbox);
		hbox.setEffect(ds);
		StackPane spScene = new StackPane();
		spScene.setAlignment(Pos.CENTER_RIGHT);
		btnscene = new Button("X");
		btnscene.setEffect(ds);
		btnscene.setOnAction(e-> ButtonClicked(e));
		btnscene.setStyle("-fx-font: 18 arial; -fx-base: #ce0000; -fx-font-weight: bold;");
		spScene.getChildren().addAll(btnscene);
		StackPane.setMargin(btnscene, new Insets(0, 10, 0, 0));
		hbox.getChildren().add(spScene);            
	    HBox.setHgrow(spScene, Priority.ALWAYS);
	    
	    FlowPane vbox = new FlowPane(Orientation.VERTICAL);
	    vbox.setVgap(15);
	    vbox.setAlignment(Pos.CENTER);
	    btn1scene=new Button("Create Network Toplogy");
		btn1scene.setOnAction(e-> ButtonClicked(e));
		btn2scene=new Button("Build a Connection Table");
		btn2scene.setOnAction(e-> ButtonClicked(e));
		btn3scene=new Button("Shortest Path to Destination");
		btn3scene.setOnAction(e-> ButtonClicked(e));
		btn4scene=new Button("Modify Network Toplogy");
		btn4scene.setOnAction(e-> ButtonClicked(e));
		btn1scene.setEffect(ds);
		btn2scene.setEffect(ds);
		btn3scene.setEffect(ds);
		btn4scene.setEffect(ds);
		btn2scene.setDisable(true);
		btn3scene.setDisable(true);
		btn4scene.setDisable(true);
		btn1scene.setMaxWidth(250);
		btn2scene.setMaxWidth(250);
		btn3scene.setMaxWidth(250);
		btn4scene.setMaxWidth(250);
		btn1scene.setStyle("-fx-font: 14 arial; -fx-base: #9ea7e5; -fx-font-weight: bold;");
		btn2scene.setStyle("-fx-font: 14 arial; -fx-base: #9ea7e5; -fx-font-weight: bold;");
		btn3scene.setStyle("-fx-font: 14 arial; -fx-base: #9ea7e5; -fx-font-weight: bold;");
		btn4scene.setStyle("-fx-font: 14 arial; -fx-base: #9ea7e5; -fx-font-weight: bold;");
	    vbox.getChildren().addAll(btn1scene, btn2scene, btn3scene, btn4scene);
	    bpscene.setCenter(vbox);    
		
	}
	
	/**
	 * Setting up Create Topology Scene
	 */
	public void addScene1(){
		bpscene1 = new BorderPane();
		HBox hbox = new HBox();
		hbox.setPadding(new Insets(20, 5, 20, 10));
		hbox.setSpacing(10);
		hbox.setAlignment(Pos.CENTER);
		hbox.setStyle("-fx-background-color: #336699;");
		hbox.setEffect(ds);
		txtscene1 = new Text("Creating Network Topology"); 
		txtscene1.setEffect(l);
		txtscene1.setStyle("-fx-font: 22 arial; -fx-base: #cce6ff; -fx-font-weight: bold;");
		hbox.getChildren().addAll(txtscene1);
		bpscene1.setTop(hbox);
		
		StackPane spScene = new StackPane();
		spScene.setAlignment(Pos.CENTER_RIGHT);
		btnscene1 = new Button("Back");
		btnscene1.setStyle("-fx-font: 18 arial; -fx-base: #cce6ff; -fx-font-weight: bold");
		btnscene1.setEffect(ds);
		btnscene1.setOnAction(e-> ButtonClicked(e));
		spScene.getChildren().addAll(btnscene1);
		StackPane.setMargin(btnscene1, new Insets(0, 10, 0, 0));
		hbox.getChildren().add(spScene);          
	    HBox.setHgrow(spScene, Priority.ALWAYS);
	    
	    FlowPane vbox = new FlowPane(Orientation.VERTICAL);
	    vbox.setAlignment(Pos.CENTER);
	    vbox.setVgap(15);
	    InnerShadow is = new InnerShadow();
		is.setOffsetX(1.0f);
        is.setOffsetY(1.0f);
        is.setColor(Color.WHITE);
	    fileChooser = new FileChooser();
	    fileChooser.setTitle("Open Topology File");
	    tfscene1 = new TextArea();
        tfscene1.setPrefRowCount(5); 
        tfscene1.setMaxWidth(350);
        tfscene1.setStyle("-fx-border-color: gray;");
        tfscene1.setEffect(ds);
        tfscene1.setPromptText("Enter the cost matrix here or choose a file to open");
        lblscene1 = new Label();
        btn3scene1 = new Button("Load a file");
        btn3scene1.setOnAction(e-> ButtonClicked(e));
	    vbox.getChildren().addAll( tfscene1, lblscene1);
	    bpscene1.setCenter(vbox);   

	    FlowPane pane = new FlowPane();
	    pane.setAlignment(Pos.TOP_CENTER);
	    pane.setHgap(25);
	    pane.setPadding(new Insets(0,0,50,0));
	    btn1scene1 = new Button("Create");
		btn1scene1.setOnAction(e-> ButtonClicked(e));
		btn2scene1 = new Button("Clear");
		btn1scene1.setEffect(ds);
		btn2scene1.setEffect(ds);
		btn3scene1.setEffect(ds);
		btn1scene1.setStyle("-fx-font: 13 arial; -fx-base: #9ea7e5; -fx-font-weight: bold");
		btn2scene1.setStyle("-fx-font: 13 arial; -fx-base: #9ea7e5; -fx-font-weight: bold");
		btn3scene1.setStyle("-fx-font: 13 arial; -fx-base: #9ea7e5; -fx-font-weight: bold");
		btn2scene1.setOnAction(e-> ButtonClicked(e));
		pane.getChildren().addAll(btn3scene1, btn1scene1, btn2scene1);
	    bpscene1.setBottom(pane);
	}
	
	/**
	 * Setting up Build Connection Table scene
	 */
	public void addScene2(){
		bpscene2 = new BorderPane();
		
		HBox hbox = new HBox();
		hbox.setPadding(new Insets(20, 5, 20, 10));
		hbox.setSpacing(10);
		hbox.setEffect(ds);
		hbox.setStyle("-fx-background-color: #336699;");
		txtscene2 = new Text("Connection Table"); 
		txtscene2.setStyle("-fx-font: 22 arial; -fx-base: #cce6ff;  -fx-font-weight: bold;");
		txtscene2.setEffect(l);
		hbox.getChildren().addAll(txtscene2);
		bpscene2.setTop(hbox);
		
		StackPane spScene = new StackPane();
		spScene.setAlignment(Pos.CENTER_RIGHT);
		btnscene2 = new Button("Back");
		btnscene2.setEffect(ds);
		btnscene2.setStyle("-fx-font: 18 arial; -fx-base: #cce6ff; -fx-font-weight: bold");
		btnscene2.setOnAction(e-> ButtonClicked(e));
		spScene.getChildren().addAll(btnscene2);
		StackPane.setMargin(btnscene2, new Insets(0, 10, 0, 0));
		hbox.getChildren().add(spScene);           
	    HBox.setHgrow(spScene, Priority.ALWAYS);
		cbscene2 = new ComboBox<Integer>();
		cbscene2.setEffect(ds);
        lblscene2=new Label("Router");  
        lblscene2.setStyle("-fx-font: 14 arial; -fx-font-weight: bold");
        lblscene2.setEffect(l);
        btn1scene2 = new Button("Build");
        btn1scene2.setEffect(ds);
        btn1scene2.setStyle("-fx-font: 14 arial; -fx-base: #9ea7e5; -fx-font-weight: bold;");
		btn1scene2.setOnAction(e-> ButtonClicked(e));
        BorderPane bp = new BorderPane();
		FlowPane pane=new FlowPane(Orientation.HORIZONTAL); 
		FlowPane fp = new FlowPane();
		fp.setAlignment(Pos.CENTER);
        pane.setAlignment(Pos.TOP_CENTER);
        pane.setPadding(new Insets(25,0,0,0));
        pane.setHgap(15);
        
        table = new TableView();
        nodeTarget = new TableColumn<>("Destination");
        nodeTarget.setMinWidth(150);
        nodeTarget.setStyle("-fx-base: #9ea7e5;");
        nodeTarget.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<String, String>, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Map.Entry<String, String>, String> p) {
                return new SimpleStringProperty(p.getValue().getKey());
            }
        });
 
        nodeInterface = new TableColumn<>("Interface");
        nodeInterface.setMinWidth(150);
        nodeInterface.setStyle("-fx-base: #9ea7e5;");
        nodeInterface.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<String, String>, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Map.Entry<String, String>, String> p) {
                return new SimpleStringProperty(p.getValue().getValue());
            }
        });
 
        table.setEditable(false);
        table.setPrefHeight(50);
        table.setEffect(ds);
        table.getColumns().addAll(nodeTarget, nodeInterface);
        table.setDisable(true);
        fp.getChildren().addAll(table);
        pane.getChildren().addAll(lblscene2, cbscene2, btn1scene2);
        bp.setTop(pane);
        bp.setCenter(fp);
        bpscene2.setCenter(bp);
        
        FlowPane pane1=new FlowPane();
        lbl1scene2 = new Label();
        pane1.setPadding(new Insets(0,0,10,0));
        pane1.setAlignment(Pos.CENTER);
        pane1.getChildren().addAll(lbl1scene2);
        bpscene2.setBottom(pane1);
	}
	
	/**
	 * Setting up Find Shortest Path Scene
	 */
	public void addScene3(){
		bpscene3 = new BorderPane();
		
		HBox hbox = new HBox();
		hbox.setPadding(new Insets(20, 5, 20, 10));
		hbox.setSpacing(10);
		hbox.setEffect(ds);
		hbox.setStyle("-fx-background-color: #336699;");
		txtscene3 = new Text("Shortest Path to Destination");
		txtscene3.setEffect(l);
		txtscene3.setStyle("-fx-font: 22 arial; -fx-base: #cce6ff; -fx-font-weight: bold;");
		hbox.getChildren().addAll(txtscene3);
		bpscene3.setTop(hbox);
		
		StackPane spScene = new StackPane();
		spScene.setAlignment(Pos.CENTER_RIGHT);
		btnscene3 = new Button("Back");
		btnscene3.setEffect(ds);
		btnscene3.setStyle("-fx-font: 18 arial; -fx-base: #cce6ff; -fx-font-weight: bold");
		btnscene3.setOnAction(e-> ButtonClicked(e));
		spScene.getChildren().addAll(btnscene3);
		StackPane.setMargin(btnscene3, new Insets(0, 10, 0, 0)); 
		hbox.getChildren().add(spScene);  
	    HBox.setHgrow(spScene, Priority.ALWAYS);
		cbscene3 = new ComboBox<Integer>();
		cb1scene3 = new ComboBox<Integer>();

        lblscene3=new Label("Source");
        lbl1scene3=new Label("Destination");  
        lblscene3.setStyle("-fx-font: 14 arial; -fx-font-weight: bold");
        lblscene3.setEffect(l);
        lbl1scene3.setStyle("-fx-font: 14 arial; -fx-font-weight: bold");
        lbl1scene3.setEffect(l);
        lbl2scene3=new Label();
        lbl2scene3.setStyle("-fx-font: 18 arial; -fx-font-weight: bold");
        lbl2scene3.setEffect(l);
        btn1scene3 = new Button("Find");
        btn1scene3.setStyle("-fx-font: 13 arial; -fx-base: #9ea7e5; -fx-font-weight: bold");
        btn1scene3.setEffect(ds);
		btn1scene3.setOnAction(e-> ButtonClicked(e));
		
		BorderPane bpane = new BorderPane();
        FlowPane pane=new FlowPane();        
        pane.setAlignment(Pos.TOP_CENTER);
        pane.setPadding(new Insets(40,0,0,0));
        pane.setHgap(15);
        pane.setVgap(15);
        pane.getChildren().addAll(lblscene3, cbscene3, lbl1scene3, cb1scene3);
        bpane.setTop(pane);
        
        FlowPane pane1 = new FlowPane(Orientation.VERTICAL);
        pane1.setPadding(new Insets(25, 0, 0, 0));
        pane1.setAlignment(Pos.CENTER);
        pane1.setHgap(15);
        pane1.setVgap(15);
        pane1.getChildren().addAll(lbl2scene3);
        bpane.setCenter(pane1);
        
        FlowPane pane2 = new FlowPane(Orientation.VERTICAL);
        pane2.setPadding(new Insets(25, 0, 0, 0));
        pane2.setAlignment(Pos.TOP_CENTER);
        pane2.setHgap(15);
        pane2.setVgap(15);
        pane2.getChildren().addAll(btn1scene3);
        bpane.setBottom(pane2);
        bpscene3.setCenter(bpane);

	}
	
	/**
	 * Setting up Modify Topology Scene
	 */
	public void addScene4(){
		bpscene4 = new BorderPane();
		
		HBox hbox = new HBox();
		hbox.setPadding(new Insets(20, 5, 20, 10));
		hbox.setSpacing(10);
		hbox.setStyle("-fx-background-color: #336699;");
		txtscene4 = new Text("Modify Network Topology"); 
		txtscene4.setStyle("-fx-font: 22 arial; -fx-base: #cce6ff; -fx-font-weight: bold;");
		txtscene4.setEffect(l);
		hbox.setEffect(ds);
		hbox.getChildren().addAll(txtscene4);
		bpscene4.setTop(hbox);
		
		StackPane spScene = new StackPane();
		spScene.setAlignment(Pos.CENTER_RIGHT);
		btnscene4 = new Button("Back");
		btnscene4.setEffect(ds);
		btnscene4.setStyle("-fx-font: 18 arial; -fx-base: #cce6ff; -fx-font-weight: bold");
		btnscene4.setOnAction(e-> ButtonClicked(e));
		spScene.getChildren().addAll(btnscene4);
		StackPane.setMargin(btnscene4, new Insets(0, 10, 0, 0)); 
		hbox.getChildren().add(spScene);      
	    HBox.setHgrow(spScene, Priority.ALWAYS);
	    
		cbscene4 = new ComboBox<Integer>();
		cb1scene4 = new ComboBox<Integer>();
		cbscene4.setEffect(ds);
		cb1scene4.setEffect(ds);
        lblscene4=new Label("Select the router to be removed");
        lblscene4.setStyle("-fx-font: 14 arial; -fx-font-weight: bold");
        lblscene4.setEffect(l);
        lbl2scene4=new Label("Select a removed router to be added");
        lbl2scene4.setStyle("-fx-font: 14 arial; -fx-font-weight: bold");
        lbl2scene4.setEffect(l);
        btn1scene4 = new Button("Remove");
        btn2scene4 = new Button("Add");
        btn1scene4.setEffect(ds);
        btn2scene4.setEffect(ds);
        btn1scene4.setStyle("-fx-font: 14 arial; -fx-base: #9ea7e5; -fx-font-weight: bold;");
        btn2scene4.setStyle("-fx-font: 14 arial; -fx-base: #9ea7e5; -fx-font-weight: bold;");
		btn1scene4.setOnAction(e-> ButtonClicked(e));
		btn2scene4.setOnAction(e-> ButtonClicked(e));
		FlowPane fp=new FlowPane();
        fp.setAlignment(Pos.TOP_CENTER);
        fp.getChildren().addAll(lblscene4, cbscene4, btn1scene4, lbl2scene4, cb1scene4, btn2scene4);
        fp.setPadding(new Insets(25,0,0,0));
        fp.setHgap(15);
        fp.setVgap(15);
        bpscene4.setCenter(fp);
            
        FlowPane pane1=new FlowPane();
        pane1.setPadding(new Insets(25,0,25,0));
        lbl1scene4 = new Label();
        pane1.setAlignment(Pos.TOP_CENTER);
        pane1.getChildren().addAll(lbl1scene4);
        bpscene4.setBottom(pane1);
	}
	
	/**
	 * To read the input file contents
	 * @param file - input file
	 * @return contents of the file in string array list format.
	 */
	 public String readFile(File file){
	        StringBuilder stringBuffer = new StringBuilder();
	        BufferedReader bufferedReader = null;
	         
	        try {
	            bufferedReader = new BufferedReader(new FileReader(file));
	            String text;
	            while ((text = bufferedReader.readLine()) != null) {
	                stringBuffer.append(text+"\n");
	            }
	        } catch (FileNotFoundException ex) {
	            Logger.getLogger(MainFX.class.getName()).log(Level.SEVERE, null, ex);
	            lblscene1.setText("Exception: FileNotFoundException");
	        } catch (IOException ex) {
	            Logger.getLogger(MainFX.class.getName()).log(Level.SEVERE, null, ex);
	            lblscene1.setText("Exception: IOException");
	        } finally {
	            try {
	                bufferedReader.close();
	            } catch (IOException ex) {
	                Logger.getLogger(MainFX.class.getName()).log(Level.SEVERE, null, ex);
	            }
	        } 
	        return stringBuffer.toString();
	    }
}