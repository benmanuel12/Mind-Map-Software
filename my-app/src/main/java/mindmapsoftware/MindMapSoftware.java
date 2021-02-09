package mindmapsoftware;

import java.io.*;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;


public class MindMapSoftware extends Application{

    private static Board active;
    private static boolean enabled = false;

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
            refreshScreen(active.getContent(), mapSpace);
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

    public static void refreshScreen(ArrayList<Element> targetList, Pane mapSpace) {
        if (mapSpace.getChildren().size() != 0) {
            for (Element element : targetList){
                if (element instanceof CustomNode){
                    CustomNode node = (CustomNode) element;
                    if (node.getIsRendered() == false) {
                        Group graphics = new Group();
                        graphics.setStyle("-fx-border-color: black");
                        Rectangle box = new Rectangle(300, 100, 200, 100);
                        box.setFill(Color.TRANSPARENT);
                        box.setStroke(Color.BLACK);
                        box.setStrokeWidth(5);
                        EditableLabel label = new EditableLabel(node.getName());
                        label.relocate(box.getX() + (box.getWidth()/2), box.getY() + 5);
                        graphics.getChildren().addAll(box, label);
                
                        // For Testing
                        node.setMedia("file:testImage.jpg");
                        //
                
                        if (node.getText() != ""){
                            EditableLabel text = new EditableLabel(node.getText());
                            text.relocate((box.getX() + (box.getWidth()/2)), box.getY() + 15);
                            graphics.getChildren().add(text);
                        }
                        if (node.getMedia() != ""){
                            Image image = new Image(node.getMedia());
                            ImageView imageview = new ImageView();
                            imageview.setFitHeight(50);
                            imageview.setFitWidth(50);
                            imageview.setImage(image);
                            imageview.relocate((box.getX() + (box.getWidth()/2)), box.getY() + 50);
                            graphics.getChildren().add(imageview);
                        }
                
                        graphics.setOnMouseDragged(event -> {
                            graphics.relocate((event.getX() - (box.getX()/2)), (event.getY() - (box.getY()/2)));
                            //graphics.relocate(event.getX(), event.getY());
                        });
                
                        graphics.setOnMouseClicked(event -> {
                            if (event.getButton() == MouseButton.SECONDARY) {
                                showNodeStyleStage(graphics);
                            }
                        });
                
                        mapSpace.getChildren().add(graphics);
                        node.setisRendered(true);
                    } else {
                        System.out.println("Node is already on screen");
                    }
                } else if (element instanceof Connector){
                    Connector connector = (Connector) element;
                    // Render the connector
                    if (connector.getIsRendered() == false){
                        /*
                        // For each pair of nodes that each reference a Connector in the targetlist in their connectedTo attribute
                            StackPane lineHolder = new StackPane();
                            Line line = new Line();
                            line.setStroke(Color.web(connector.getColor()));
                            EditableLabel lineLabel = new EditableLabel("Label text here");
                            lineHolder.getChildren().addAll(line, lineLabel);

                            line.setOnMouseClicked(event -> {
                                if (event.getButton() == MouseButton.SECONDARY) {
                                    showConnectorStyleStage(line, lineLabel);
                                }
                            });
                            // enable proper creation
                            // enable them to move when nodes move
                            // need some way to actually create them from the UI
                            */

                    }
                }
            }
        } else {
            // Pane is empty, this is a full reload
            for (Element element : targetList){
                if (element instanceof CustomNode){
                    CustomNode node = (CustomNode) element;
                    Group graphics = new Group();
                    graphics.setStyle("-fx-border-color: black");
                    Rectangle box = new Rectangle(300, 100, 200, 100);
                    box.setFill(Color.TRANSPARENT);
                    box.setStroke(Color.BLACK);
                    box.setStrokeWidth(5);
                    EditableLabel label = new EditableLabel(node.getName());
                    label.relocate(box.getX() + (box.getWidth()/2), box.getY() + 5);
                    graphics.getChildren().addAll(box, label);
            
                    // For Testing
                    node.setMedia("file:testImage.jpg");
                    //
            
                    if (node.getText() != ""){
                        EditableLabel text = new EditableLabel(linkFinder(node.getText()));
                        text.relocate((box.getX() + (box.getWidth()/2)), box.getY() + 15);
                        graphics.getChildren().add(text);
                    }
                    if (node.getMedia() != ""){
                        Image image = new Image(node.getMedia());
                        ImageView imageview = new ImageView();
                        imageview.setFitHeight(50);
                        imageview.setFitWidth(50);
                        imageview.setImage(image);
                        imageview.relocate((box.getX() + (box.getWidth()/2)), box.getY() + 50);
                        graphics.getChildren().add(imageview);
                    }
            
                    graphics.setOnMouseDragged(event -> {
                        graphics.relocate((event.getX() - (box.getX()/2)), (event.getY() - (box.getY()/2)));
                        node.setXCoord(event.getX() - (box.getX()/2));
                        node.setYCoord(event.getY() - (box.getY()/2));
                        //graphics.relocate(event.getX(), event.getY());
                    });
            
                    graphics.setOnMouseClicked(event -> {
                        if (event.getButton() == MouseButton.SECONDARY) {
                            showNodeStyleStage(graphics);
                        }
                    });
            
                    mapSpace.getChildren().add(graphics);
                    graphics.relocate(node.getXCoord(), node.getYCoord());
                    node.setisRendered(true);
                    node.setXCoord(box.getX());
                    node.setYCoord(box.getY());
    
                } else if (element instanceof Connector){
                    Connector connector = (Connector) element;
                    // Render the connector
                    // Same as above, but so little works I'm not going to bother to copy paste it here yet
                }
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
                ArrayList<Element> currentContent = active.getContent();
                currentContent.add(tempNode);
                active.setContent(currentContent);
                // Render node to screen
                refreshScreen(active.getContent(), mapSpace);

                // Enable editing of the node
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
                System.out.println(search(searchInput.getText(), active.getContent()));
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
        root.setCenter(mapSpace);
        root.setBottom(footer);

        Scene scene = new Scene(root, 960, 600);
        //scene.getStylesheets().add("stylesheet.css");

        primaryStage.setScene(scene);
        primaryStage.setTitle("Mind Map Software");
        primaryStage.show();

    }

    public static void showNodeStyleStage(Group group){

        
        Stage stage = new Stage();
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));

        // Name Color
        Label nameColorLabel = new Label("Name Color");
        ObservableList<String> nameColorOptions = FXCollections.observableArrayList(
            "black",
            "red",
            "green",
            "yellow",
            "blue"
        );
        ComboBox<String> nameColor = new ComboBox<>(nameColorOptions);

    
        // Background Color
        Label backgroundColorLabel = new Label("Background Color");
        ObservableList<String> backgroundColorOptions = FXCollections.observableArrayList(
            "black",
            "red",
            "green",
            "yellow",
            "blue"
        );
        ComboBox<String> BackgroundColor = new ComboBox<>(backgroundColorOptions);
    
        // Border
        Label borderLabel = new Label("Border Style");
        ObservableList<String> borderOptions = FXCollections.observableArrayList(
            "rectangle",
            "round",
            "cloud"
        );
        ComboBox<String> BorderStyle = new ComboBox<>(borderOptions);
    
        // BorderColor
        Label borderColorLabel = new Label("Border Color");
        ObservableList<String> borderColorOptions = FXCollections.observableArrayList(
            "black",
            "red",
            "green",
            "yellow",
            "blue"
        );
        ComboBox<String> BorderColor = new ComboBox<>(borderColorOptions);
    
        // Center
        CheckBox isCenter = new CheckBox("Center Node");

        Button saveButton = new Button("Save");

        // box, label, text, image, nested
        saveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                Label nameColorOut = (Label) group.getChildren().get(1);
                nameColorOut.setTextFill(Color.web(nameColor.getValue()));
                Rectangle backgroundColorOut = (Rectangle) group.getChildren().get(0);
                backgroundColorOut.setFill(Color.web(BackgroundColor.getValue()));
                // Border Style goes here
                backgroundColorOut.setStroke(Color.web(BorderColor.getValue()));
                // Handle isCenter
            }
        });

        grid.add(nameColorLabel, 0, 0);
        grid.add(nameColor, 1, 0);
        grid.add(backgroundColorLabel, 0, 1);
        grid.add(BackgroundColor, 1, 1);
        grid.add(borderLabel, 0, 2);
        grid.add(BorderStyle, 1, 2);
        grid.add(borderColorLabel, 0, 3);
        grid.add(BorderColor, 1, 3);
        grid.add(isCenter, 0, 4);
        grid.add(saveButton, 0, 5);

        Scene scene = new Scene (grid, 300, 150);
        stage.setScene(scene);
        stage.show();
    }

    public static void showConnectorStyleStage(Line line, Label label){
        Stage stage = new Stage();
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));

       
    
        // Label Color
        Label labelColorLabel = new Label("Label Color");
        ObservableList<String> labelColorOptions = FXCollections.observableArrayList(
            "black",
            "red",
            "green",
            "yellow",
            "blue"
        );
        ComboBox<String> labelColor = new ComboBox<>(labelColorOptions);

        // Color
        Label lineColorLabel = new Label("Line Color");
        ObservableList<String> lineColorOptions = FXCollections.observableArrayList(
            "black",
            "red",
            "green",
            "yellow",
            "blue"
        );
        ComboBox<String> lineColor = new ComboBox<>(lineColorOptions);

        // Type
        Label typeLabel = new Label("Type");
        ObservableList<String> typeOptions = FXCollections.observableArrayList(
            "undirected no dash",
            "undirected dash",
            "directed no dash",
            "directed dash"
        );
        ComboBox<String> type = new ComboBox<>(typeOptions);

        Button saveButton = new Button("Save");

        // label color, line color, type
        saveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                label.setTextFill(Color.web(labelColor.getValue()));
                line.setStroke(Color.web(lineColor.getValue()));
                // Handle dashed lines
            }
        });

        grid.add(labelColorLabel, 0, 0);
        grid.add(labelColor, 1, 0);
        grid.add(lineColorLabel, 0, 1);
        grid.add(lineColor, 1, 1);
        grid.add(typeLabel, 0, 2);
        grid.add(type, 1, 2);
        grid.add(saveButton, 0, 3);

        Scene scene = new Scene (grid, 300, 100);
        stage.setScene(scene);
        stage.show();
    }

    public void newBoardPopup(Pane mapSpace, Label footerLabel){

        Stage stage = new Stage();
        Text newBoardPopupText = new Text("Enter name");
        TextField newBoardPopupInput = new TextField();
        Button newBoardConfirm = new Button("Create");
        newBoardConfirm.setOnAction(e -> {
            Board newBoard = new Board(newBoardPopupInput.getText(), new ArrayList<Element>());
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
