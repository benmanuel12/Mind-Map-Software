package mindmapsoftware;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


public class MindMapSoftware extends Application{

    private static Board active;
    private static boolean enabled = false;

    // This returns rather baffling results as nodes that contain other nodes are returned in their entirity including the recursive nodes within them
    // I hope for the code that displays the results to abstract over this and just display relevant data for each node and connector
    public static ArrayList<CustomNode> search(String input, ArrayList<CustomNode> targetList){
        ArrayList<CustomNode> results = new ArrayList<>();

        for (Element element : targetList){
            if (element instanceof CustomNode){
                CustomNode node = (CustomNode) element;
                if ((node.getName().toLowerCase().contains(input.toLowerCase())) || (node.getText().toLowerCase().contains(input.toLowerCase()))){
                    results.add(node);
                }
                if (node.getNodeContent().size() != 0){
                    // The node contains more nodes and connectors
                    results.addAll(search(input, node.getNodeContent()));
                }
            } else if (element instanceof Connector){
                Connector connector = (Connector) element;
                if (connector.getLabel().toLowerCase().contains(input.toLowerCase())){
                    //results.add(connector);
                }
            } 
        }   
        return results;
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

    public static void load(String filename, Pane mapSpace, Label footerLabel) {
        Board loadedBoard;
        try {
            FileInputStream fileIn = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            loadedBoard = (Board) in.readObject();
            active = loadedBoard;
            footerLabel.setText(active.getName());
            mapSpace.getChildren().clear();
            refreshScreen(active.getNodeContent(), active.getConnectorContent(), mapSpace);
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

    public static void refreshScreen(ArrayList<CustomNode> nodeList, ArrayList<Connector> connectorList, Pane mapSpace) {
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
                } else {
                    System.out.println("Node is already on screen");
                }
            }
            for (Connector connector: connectorList){
                if (connector.getIsRendered() == false) {
                    ConnectorPane newPane = new ConnectorPane(connector);
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
            }
            for (Connector connector: connectorList){
                ConnectorPane newPane = new ConnectorPane(connector);
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
        ScrollPane scroll = new ScrollPane(mapSpace);
        

        // Supposedly makes the lag on dragging boxes reduce. Has negligible effect
        mapSpace.setCache(true);

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
                refreshScreen(active.getNodeContent(), active.getConnectorContent(), mapSpace);
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
                load(selectedFile.getName(), mapSpace, footerLabel);
            System.out.println("Loaded: " + selectedFile.getName());
            } else {
                System.out.println("No file selected");
            }
            
        });

        quickAddToggleButton.setOnAction(e -> {
            
            if (enabled == false){
                quickAddToggleButton.setStyle("-fx-background-color: #00ff00");
                enabled = true;
            } else {
                quickAddToggleButton.setStyle("-fx-background-color: #ff0000");
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

        searchSubmitButton.setOnAction(e -> {
            if (active == null){
                System.out.println("No board is active");
            } else {
                System.out.println(search(searchInput.getText(), active.getNodeContent()));
            }
        });

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
        root.setBottom(footer);

        Scene scene = new Scene(root, 960, 600);
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