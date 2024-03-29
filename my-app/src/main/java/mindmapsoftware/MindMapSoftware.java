package mindmapsoftware;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


public class MindMapSoftware extends Application {

    private static Board active;
    private static boolean enabled = false;
    private static NodePane selected1;
    private static NodePane selected2;
    private static ArrayList<NodePane> NodeResults = new ArrayList<>();
    private static ArrayList<ConnectorPane> ConnectorResults = new ArrayList<>();

    // This returns rather baffling results as nodes that contain other nodes are
    // returned in their entirity including the recursive nodes within them
    // I hope for the code that displays the results to abstract over this and just
    // display relevant data for each node and connector
    public static void search(String input, Pane mapSpace, ScrollPane scroll, VBox nodeHolder, VBox connectorHolder){
       
        nodeHolder.getChildren().clear();
        connectorHolder.getChildren().clear();
        System.out.println("Cleared holders");

        ObservableList<Node> nodeList = mapSpace.getChildren();

        for (Node x : nodeList){
            if (x instanceof NodePane){
                NodePane nodePane = (NodePane) x;
                CustomNode node = nodePane.getNode();

                if ((node.getName().toLowerCase().contains(input.toLowerCase())) || (node.getText().toLowerCase().contains(input.toLowerCase()))){
                    NodeResults.add(nodePane);
                }
                if (node.getNodeContent().size() != 0){
                    // The node contains more nodes and connectors
                    search(input, mapSpace, scroll, nodeHolder, connectorHolder);
                }
            } else if (x instanceof ConnectorPane) {
                ConnectorPane connectorPane = (ConnectorPane) x;
                Connector connector = connectorPane.getConnector();

                if (connector.getLabel().toLowerCase().contains(input.toLowerCase())){
                    ConnectorResults.add(connectorPane);
                } 
            }
        }

        System.out.println("Completed Search");
        System.out.println(NodeResults);
        System.out.println(ConnectorResults);
        
        for (NodePane nodePane : NodeResults){
            Button tempButton = new Button(nodePane.getNode().getName());
            for (Node node : mapSpace.getChildren()) {
                if (node instanceof NodePane){
                    NodePane possiblenodepane = (NodePane) node;
                    if (possiblenodepane.getNode() == nodePane.getNode()) {
                        tempButton.setOnAction(new EventHandler<ActionEvent>(){
                            @Override
                            public void handle(ActionEvent t) {
                                Rectangle rect = (Rectangle) nodePane.getChildren().get(0);
                                double newX = (nodePane.getXCoord() - rect.getWidth()/2 + 960)/19200;
                                double newY = (nodePane.getYCoord() - rect.getHeight()/2 + 540)/10800;
    
                                if (newX < 0.0){
                                    newX = 0.0;
                                }           
                                if (newY < 0.0){
                                    newY = 0.0;
                                }
    
                                scroll.setHvalue(newX);
                                scroll.setVvalue(newY);
                            }
                        });
                    }
                }
            }
            nodeHolder.getChildren().add(tempButton);
        }
        System.out.println("Created Buttons for Nodes");

        for (ConnectorPane connectorPane : ConnectorResults){
            Button tempButton = new Button(connectorPane.getConnector().getLabel());
            for (Node node : mapSpace.getChildren()) {
                if (node instanceof ConnectorPane){
                    ConnectorPane possibleconnectorpane = (ConnectorPane) node;
                    if (possibleconnectorpane.getConnector() == connectorPane.getConnector()) {
                        tempButton.setOnAction(new EventHandler<ActionEvent>(){
                            @Override
                            public void handle(ActionEvent t) {
                                Line line = (Line) connectorPane.getChildren().get(0);

                                // new X  is equal to (whichever X coord of start X and End X is smaller + (abs(startX - endX))2 + 960)/1920
                                // new Y  is equal to (whichever Y coord of start Y and End Y is smaller + (abs(startY - endY))2 + 540)/1080
                                double newX = (Math.min(line.getStartX(), line.getEndX()) - (Math.abs(line.getStartX() - line.getEndX()))/2 + 960)/19200;
                                double newY = (Math.min(line.getStartY(), line.getEndY()) - (Math.abs(line.getStartY() - line.getEndY()))/2 + 540)/10800;

                                if (newX < 0.0){
                                    newX = 0.0;
                                }           
                                if (newY < 0.0){
                                    newY = 0.0;
                                }
    
                                scroll.setHvalue(newX);
                                scroll.setVvalue(newY);
                            }
                        });
                    }
                }
            }
            connectorHolder.getChildren().add(tempButton);
        }
        System.out.println("Created Buttons for Connectors");
        
    }

    private static void save() {
        String filename = active.getName() + ".ser";

        try {
            FileOutputStream fileOut = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(active);
            out.close();
            fileOut.close();
            System.out.printf("Serialized data is saved in " + filename + "\n");
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    public static void load(String filename, Pane mapSpace, ScrollPane scroll, Label footerLabel) {
        Board loadedBoard;
        try {
            FileInputStream fileIn = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            loadedBoard = (Board) in.readObject();
            active = loadedBoard;
            footerLabel.setText(active.getName());
            mapSpace.getChildren().clear();
            refreshScreen(active.getNodeContent(), active.getConnectorContent(), mapSpace, scroll);
            in.close();
            fileIn.close();
        } catch (IOException i) {
            i.printStackTrace();
            return;
        } catch (ClassNotFoundException c) {
            System.out.println("Board class not found");
            c.printStackTrace();
            return;
        }
    }


    public static void refreshScreen(ArrayList<CustomNode> nodeList, ArrayList<Connector> connectorList, Pane mapSpace, ScrollPane scrollPane) {
        if (mapSpace.getChildren().size() != 0) {
            for (CustomNode node: nodeList){
                if (node.getIsRendered() == false) {
                    NodePane newPane = new NodePane(node);
                    mapSpace.getChildren().add(newPane);
                    newPane.relocate(newPane.getXCoord(), newPane.getYCoord());
                    newPane.setXCoord(newPane.getTranslateX());
                    newPane.setYCoord(newPane.getTranslateY());
                    newPane.setIsRendered(true);
                    node.setisRendered(true);
                    if (node.isCenter()){
                        Rectangle rect = (Rectangle) newPane.getChildren().get(0);
                        double newX = (newPane.getXCoord() - rect.getWidth()/2 + 960)/19200;
                        double newY = (newPane.getYCoord() - rect.getHeight()/2 + 540)/10800;
    
                        if (newX < 0.0){
                            newX = 0.0;
                        }
                        if (newY < 0.0){
                            newY = 0.0;
                        }
    
                        scrollPane.setHvalue(newX);
                        scrollPane.setVvalue(newY);
                    }
                } else {
                    System.out.println("Node is already on screen");
                }
            }
            for (Connector connector: connectorList){
                if (connector.getIsRendered() == false) {
                    ConnectorPane newPane = new ConnectorPane(connector);
                    newPane.getLine().setStrokeWidth(5);
                    mapSpace.getChildren().add(newPane);
                    newPane.setIsRendered(true);
                    connector.setisRendered(true);
                } else {
                    System.out.println("Connector is already on screen");
                }
            }
        } else {
            // Pane is empty, this is a full reload
            for (CustomNode node: nodeList){
                NodePane newPane = new NodePane(node);
                mapSpace.getChildren().add(newPane);
                newPane.relocate(newPane.getXCoord(), newPane.getYCoord());
                newPane.setXCoord(newPane.getTranslateX());
                newPane.setYCoord(newPane.getTranslateY());
                newPane.setIsRendered(true);
                node.setisRendered(true);
                if (node.isCenter()){
                    Rectangle rect = (Rectangle) newPane.getChildren().get(0);
                    double newX = (newPane.getXCoord() - rect.getWidth()/2 + 960)/1920;
                    double newY = (newPane.getYCoord() - rect.getHeight()/2 + 540)/1080;

                    if (newX < 0.0){
                        newX = 0.0;
                    }
                    if (newY < 0.0){
                        newY = 0.0;
                    }

                    scrollPane.setHvalue(newX);
                    scrollPane.setVvalue(newY);
                }
            }
            for (Connector connector: connectorList){
                ConnectorPane newPane = new ConnectorPane(connector);
                newPane.getLine().setStrokeWidth(5);
                mapSpace.getChildren().add(newPane);
                newPane.setIsRendered(true);
                connector.setisRendered(true);
            }  
        }    
    }     


    public static void main(String[] args){
        launch(args);
    }

    private static String linkFinder(String text){
        int startPoint = -2;
        do {
            startPoint = text.indexOf('[');
            if (startPoint != -1){
                String tempText = text.substring(startPoint, text.indexOf(')') + 1);
                if ((tempText.indexOf(']') > 0) && (tempText.indexOf('(') > tempText.indexOf(']')) && (tempText.indexOf(')') > tempText.indexOf('('))){
                    // Link is valid
                    // Check if hyperlink
                    if (tempText.substring(tempText.indexOf('(') + 1).startsWith("http")){
                        int oldLength = tempText.length();
                        // Assemble tempText into a HTML tag
                        // tempText = "<a href=\"" + tempText.substring(tempText.indexOf('(') + 1, tempText.indexOf(')')) + "\">" + tempText.substring(tempText.indexOf('[') + 1, (tempText.indexOf(']'))) + "</a>";
                        String link = tempText.substring(tempText.indexOf('(') + 1, tempText.indexOf(')'));
                        String label = tempText.substring(tempText.indexOf('[') + 1, (tempText.indexOf(']')));
                        tempText = "<a href=\"" + link + "\">" + label + "</a>";

                        // Set display of parent nodes text to html
                        String before = text.substring(0, startPoint);
                        String after = text.substring(startPoint + oldLength, text.length());
                        text = before + tempText + after;
                    }
                    // otherwise assume its a file link and try to bind the area between the [ ] to the relevant files 
                    // Leave until UI is created
                }
            }
        } while (startPoint != -1);
        return text;
    }

    public void start(Stage primaryStage){
        FileChooser fileChooser = new FileChooser();  
        
        BorderPane root = new BorderPane();
        TitledPane toolbar = new TitledPane();
        HBox hbox = new HBox();
        Pane mapSpace = new Pane();

        mapSpace.setPrefSize(19200, 10800);
        ScrollPane scroll = new ScrollPane(mapSpace);
        scroll.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.ALWAYS);
        scroll.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.ALWAYS);
        scroll.setPrefViewportWidth(800);
        scroll.setPrefViewportHeight(600); 
        
        // Supposedly makes the lag on dragging boxes reduce. Has negligible effect
        mapSpace.setCache(true);

        mapSpace.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            System.out.println(event.getTarget());
            if (event.isControlDown()){
                if (event.getTarget() instanceof Rectangle) {
                    if ((selected1 == null) && (selected2 == null)) {
                        System.out.println("Selected 1");
                        Rectangle shape = (Rectangle) event.getTarget();
                        selected1 = (NodePane) shape.getParent();

                    } else if ((selected1 instanceof NodePane) && (selected2 == null)) {
                        Rectangle shape = (Rectangle) event.getTarget();
                        selected2 = (NodePane) shape.getParent();
                        System.out.println("Selected 2");

                        Connector tempConnector = new Connector();
                        tempConnector.setNode1(selected1.getNode());
                        tempConnector.setNode2(selected2.getNode());
                        System.out.println("Created new Connector");
                        tempConnector.setLabel("Hi there");

                        ArrayList<Connector> currentContent = active.getConnectorContent();
                        currentContent.add(tempConnector);
                        active.setConnectorContent(currentContent);
                        System.out.println("Added to board");

                        // Render node to screen
                        refreshScreen(active.getNodeContent(), active.getConnectorContent(), mapSpace, scroll);
                        System.out.println("Refreshed screen");

                        // Reset selection variables
                        selected1 = null;
                        selected2 = null;
                    }
                }
            } else if (event.isShiftDown()){
                System.out.println("Shift is down");
                if (event.getButton() == MouseButton.PRIMARY){
                    System.out.println("Left click used");
                    if (event.getTarget() instanceof Rectangle) {
                        System.out.println("Click in right place");
                        Rectangle rect = (Rectangle) event.getTarget();
                        NodePane pane = (NodePane) rect.getParent();
                        CustomNode nodeToDelete = pane.getNode();
                        active.getNodeContent().remove(nodeToDelete);
                        System.out.println(active.getNodeContent());
                        
                        Pane parentPane = (Pane) pane.getParent();
                        parentPane.getChildren().remove(pane);
                        System.out.println(parentPane.getChildren());

                    } else if (event.getTarget() instanceof Line) {
                        System.out.println("Click in right place");
                        Line line = (Line) event.getTarget();
                        ConnectorPane pane = (ConnectorPane) line.getParent();
                        Connector connectorToDelete = pane.getConnector();
                        active.getConnectorContent().remove(connectorToDelete);

                        Pane parentPane = (Pane) pane.getParent();
                        parentPane.getChildren().remove(pane);

                    }
                }
            }             
        });

        HBox footer = new HBox();
        Label footerLabel = new Label ("No open Board");
        footer.getChildren().add(footerLabel);

        // Toolbar buttons
        Button newBoardButton = new Button("New Board");
        Button newCustomNodeButton = new Button("New Node");
        Button saveButton = new Button("Save");
        Button loadButton = new Button("Load");
        Button quickAddToggleButton = new Button("Toggle Quick Add Mode");
        Button upLayerButton = new Button("Up");
        Button downLayerButton = new Button("Down");
        TextField searchInput = new TextField();
        Button searchSubmitButton = new Button("Search");

        // Toolbar sub UI and button eventa
        newBoardButton.setOnAction(e -> {
                newBoardPopup(mapSpace, footerLabel);
        });

        newCustomNodeButton.setOnAction(e -> {
            if (active == null){
                System.out.println("No board is active");
            } else {
                CustomNode tempNode = new CustomNode();
                ArrayList<CustomNode> currentContent = active.getNodeContent();
                currentContent.add(tempNode);
                active.setNodeContent(currentContent);
                // Render node to screen
                refreshScreen(active.getNodeContent(), active.getConnectorContent(), mapSpace, scroll);
            }
        });

        saveButton.setOnAction(e -> {
            if (active == null){
                System.out.println("No board is active");
            } else {
                save();
                System.out.println("Saved");
            }
        });

        loadButton.setOnAction(e -> {
            File selectedFile = fileChooser.showOpenDialog(primaryStage);
            if (selectedFile != null) {
                load(selectedFile.getName(), mapSpace, scroll, footerLabel);
            System.out.println("Loaded: " + selectedFile.getName());
            } else {
                System.out.println("No file selected");
            }
            
        });

        quickAddToggleButton.setOnAction(e -> {
            
            if (enabled == false){
                quickAddToggleButton.setStyle("-fx-background-colour: #00ff00");
                enabled = true;
            } else {
                quickAddToggleButton.setStyle("-fx-background-colour: #ff0000");
                enabled = false;
            }
        });

        upLayerButton.setOnAction(e -> {
            if (active == null){
                System.out.println("No board is active");
            } else {
                // Clear screen
                // Load map from variable from last use of downLayerButon
            }
        });

        downLayerButton.setOnAction(e -> {
            if (active == null){
                System.out.println("No board is active");
            } else {
                // Locate the content attribute of the currently selected Node
                // Clear the screen of the current map, storing contents in variable
                // Load the contents of content to the screen
                // Optional: Cool animation
                // Optional: Some kind of layer tracker
            }
        });

        VBox nodeResultsholder = new VBox();
        VBox connectorResultsholder = new VBox();

        TabPane searchResultholder = new TabPane();
        Tab nodeTab = new Tab("Nodes", nodeResultsholder);
        Tab connectorTab = new Tab("Connectors", connectorResultsholder);

        searchResultholder.getTabs().addAll(nodeTab, connectorTab);

        searchSubmitButton.setOnAction(e -> {
            if (active == null){
                System.out.println("No board is active");
            } else {
                search(searchInput.getText(), mapSpace, scroll, nodeResultsholder, connectorResultsholder);
            }
        });

        // BorderSlideBar sidebar = new BorderSlideBar(300, searchSubmitButton, Pos.BASELINE_RIGHT, searchResultholder);


        // Toolbar arrangements
        hbox.getChildren().addAll(newBoardButton, newCustomNodeButton, saveButton, loadButton, quickAddToggleButton, upLayerButton, downLayerButton, searchInput, searchSubmitButton);
        hbox.setAlignment(Pos.CENTER);
        HBox.setHgrow(newBoardButton, Priority.SOMETIMES);
        HBox.setHgrow(newCustomNodeButton, Priority.SOMETIMES);
        HBox.setHgrow(saveButton, Priority.SOMETIMES);
        HBox.setHgrow(loadButton, Priority.SOMETIMES);
        HBox.setHgrow(quickAddToggleButton, Priority.SOMETIMES);
        HBox.setHgrow(upLayerButton, Priority.SOMETIMES);
        HBox.setHgrow(downLayerButton, Priority.SOMETIMES);
        HBox.setHgrow(searchInput, Priority.SOMETIMES);
        HBox.setHgrow(searchSubmitButton, Priority.SOMETIMES);
        

        toolbar.setText("Toolbar");
        toolbar.setContent(hbox);

        root.setTop(toolbar);
        root.setCenter(scroll);
        root.setRight(searchResultholder);
        root.setBottom(footer);
        Scene scene = new Scene(root, 800, 500);
        //scene.getStylesheets().add("stylesheet.css");

        primaryStage.setScene(scene);
        primaryStage.setTitle("Mind Map Software");
        primaryStage.show();

    }

    public void newBoardPopup(Pane mapSpace, Label footerLabel){

        Stage stage = new Stage();
        Text newBoardPopupText = new Text("Enter name");
        TextField newBoardPopupInput = new TextField();
        Button newBoardConfirm = new Button("Create");
        newBoardConfirm.setOnAction(e -> {
            Board newBoard = new Board(newBoardPopupInput.getText(), new ArrayList<CustomNode>(), new ArrayList<Connector>());
            active = newBoard;
            stage.hide();
            footerLabel.setText(newBoardPopupInput.getText());
            System.out.println("Creating new Board");
            // refresh the main section of the UI
            mapSpace.getChildren().clear();
        });

        VBox newBoardPopupRoot = new VBox();
        newBoardPopupRoot.setPadding(new Insets(10));
        newBoardPopupRoot.getChildren().addAll(newBoardPopupText, newBoardPopupInput, newBoardConfirm);

        Scene scene = new Scene (newBoardPopupRoot, 300, 100);
        stage.setScene(scene);
        stage.show();
    }
}