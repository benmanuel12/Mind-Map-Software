package mindmapsoftware;

import java.io.*;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.scene.layout.Priority;
import javafx.geometry.Pos;

public class MindMapSoftware extends Application{

    private static Board active;

    // This returns rather baffling results as nodes that contain other nodes are returned in their entirity including the recursive nodes within them
    // I hope for the code that displays the results to abstract over this and just display relevant data for each node and connector
    public static ArrayList<Element> search(String input, ArrayList<Element> targetList){
        ArrayList<Element> results = new ArrayList<>();

        for (Element element : targetList){
            if (element instanceof CustomNode){
                CustomNode node = (CustomNode) element;
                if ((node.getName().toLowerCase().contains(input.toLowerCase())) || (node.getText().toLowerCase().contains(input.toLowerCase()))){
                    results.add(node);
                }
                if (node.getContent().size() != 0){
                    // The node contains more nodes and connectors
                    results.addAll(search(input, node.getContent()));
                }
            } else if (element instanceof Connector){
                Connector connector = (Connector) element;
                if (connector.getLabel().toLowerCase().contains(input.toLowerCase())){
                    results.add(connector);
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
            System.out.printf("Serialized data is saved in " + filename);
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    public static void load(String filename) {
        Board loadedBoard;
        try {
            FileInputStream fileIn = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            loadedBoard = (Board) in.readObject();
            active = loadedBoard;
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

        // Core UI elements
        VBox root = new VBox();
        TitledPane toolbar = new TitledPane();
        HBox hbox = new HBox();

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

        // Toolbar sub UI and button events
        Popup newBoardPopup = new Popup();
        Text newBoardPopupText = new Text("Enter name");
        TextField newBoardPopupInput = new TextField();
        Button newBoardConfirm = new Button("Create");
        newBoardConfirm.setOnAction(e -> {
            Board newBoard = new Board(newBoardPopupInput.getText(), new ArrayList<Element>());
            active = newBoard;
            newBoardPopup.hide();
            // refresh the main section of the UI
        });
        VBox newBoardPopupRoot = new VBox();
        newBoardPopupRoot.getChildren().addAll(newBoardPopupText, newBoardPopupInput, newBoardConfirm);

        newBoardButton.setOnAction(e -> {
            if (!newBoardPopup.isShowing()){
                newBoardPopup.show(primaryStage);
            } else {
                newBoardPopup.hide();
            }
        });

        newCustomNodeButton.setOnAction(e -> {
            CustomNode tempNode = new CustomNode();
            ArrayList<Element> tempArray = active.getContent().add(tempNode, active.getContent().size());
            active.setContent();

        });

        saveButton.setOnAction(e -> {
            save();
            System.out.println("Saved");
        });

        loadButton.setOnAction(e -> {
            File selectedFile = fileChooser.showOpenDialog(primaryStage);
            load(selectedFile.getName());
            System.out.println("Loaded: " + selectedFile.getName());
        });

        quickAddToggleButton.setOnAction(e -> {
            boolean enabled = false;
            if (enabled == false){
                quickAddToggleButton.setStyle("-fx-background-color: #0000ff");
                enabled = true;
            } else if (enabled == true) {
                quickAddToggleButton.setStyle("-fx-background-color: #ff0000");
                enabled = false;
            }
        });

        upLayerButton.setOnAction(e -> {
            // Clear screen
            // Load map from variable from last use of downLayerButon
        });

        downLayerButton.setOnAction(e -> {
            // Locate the content attribute of the currently selected Node
            // Clear the screen of the current map, storing contents in variable
            // Load the contents of content to the screen
            // Optional: Cool animation
            // Optional: Some kind of layer tracker
        });

        searchSubmitButton.setOnAction(e -> {
            System.out.println(search(searchInput.getText(), active.getContent()));
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

        // Placeholder
        Rectangle mapPlaceholder = new Rectangle(960, 550);
        root.getChildren().addAll(toolbar, mapPlaceholder);

        Scene scene = new Scene(root, 960, 600);
        //scene.getStylesheets().add("stylesheet.css");

        primaryStage.setScene(scene);
        primaryStage.setTitle("Mind Map Software");
        primaryStage.show();

    }
}
