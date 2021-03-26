package mindmapsoftware;

import java.io.File;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class NodePane extends Pane{

    // Pane attributes
    private Rectangle rect = new Rectangle(200, 100);
    private EditableLabel titleLabel = new EditableLabel();

    private EditableLabel textLabel = new EditableLabel();
    private ImageView imageview = new ImageView();

    // Node Attributes with a corresponding named attribute in CustomNode
    private String title; // links to name in CustomNode
    private String nameColour;
    private String backgroundColour;
    private String borderType; // links to border in CustomNode
    private String borderColour;
    private boolean center;
    private String media;
    private String text;
    private boolean isRendered;
    private double xCoord;
    private double yCoord;


    // Link to CustomNode
    private CustomNode node;

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNameColour() {
        return this.nameColour;
    }

    public void setNameColour(String nameColour) {
        this.nameColour = nameColour;
    }

    public String getBackgroundColour() {
        return this.backgroundColour;
    }

    public void setBackgroundColour(String backgroundColour) {
        this.backgroundColour = backgroundColour;
    }

    public String getBorderType() {
        return this.borderType;
    }

    public void setBorderType(String borderType) {
        this.borderType = borderType;
    }

    public String getBorderColour() {
        return this.borderColour;
    }

    public void setBorderColour(String borderColour) {
        this.borderColour = borderColour;
    }

    public boolean isCenter() {
        return this.center;
    }

    public boolean getCenter() {
        return this.center;
    }

    public void setCenter(boolean center) {
        this.center = center;
    }

    public String getMedia() {
        return this.media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isIsRendered() {
        return this.isRendered;
    }

    public boolean getIsRendered() {
        return this.isRendered;
    }

    public void setIsRendered(boolean isRendered) {
        this.isRendered = isRendered;
    }

    public double getXCoord() {
        return this.xCoord;
    }

    public void setXCoord(double xCoord) {
        this.xCoord = xCoord;
    }

    public double getYCoord() {
        return this.yCoord;
    }

    public void setYCoord(double yCoord) {
        this.yCoord = yCoord;
    }

    public CustomNode getNode() {
        return this.node;
    }

    public void setNode(CustomNode node) {
        this.node = node;
    }

    public NodePane(CustomNode node){

        this.node = node;

        this.title = node.getName();
        this.nameColour = node.getNameColour();
        this.backgroundColour = node.getBackgroundColour();
        this.borderType = node.getBorder();
        this.borderColour = node.getBorderColour();
        this.center = node.isCenter();
        this.media = "file:testImage.jpg";
        this.text = node.getText();
        this.isRendered = node.getIsRendered();
        this.xCoord = node.getXCoord();
        this.yCoord = node.getYCoord();



        // change below to use refernces to node attribute

        this.rect.setFill(Color.web(this.backgroundColour));
        this.rect.setStroke(Color.web(this.borderColour));
        this.rect.setStrokeWidth(5);

        this.titleLabel = new EditableLabel(this.title);
        this.titleLabel.relocate(rect.getX() + (rect.getWidth()/2), rect.getY() + 5);
        
        if (this.getText() != ""){
            this.textLabel = new EditableLabel(this.text);
            this.textLabel.relocate(rect.getX() + (rect.getWidth()/2), rect.getY() + 15);
        }

        if (this.getMedia() != "") {
            Image image = new Image(this.getMedia());
            imageview.setFitHeight(50);
            imageview.setFitWidth(50);
            imageview.setImage(image);
            this.imageview.relocate(rect.getX() + 5, rect.getY() + 5);
        }

        this.setOnMouseDragged(event -> {
            this.setTranslateX(event.getX() + this.getTranslateX());
            this.setTranslateY(event.getY() + this.getTranslateY());
            this.setXCoord(event.getX() + this.getTranslateX());
            this.setYCoord(event.getY() + this.getTranslateY());
            node.setXCoord(event.getX() + this.getTranslateX());
            node.setYCoord(event.getY() + this.getTranslateY());

            event.consume();
        });

        this.setOnMouseDragReleased(event -> {
            Pane pane = (Pane) this.getParent();

            for (Node paneNode: pane.getChildren()) {
                if (paneNode instanceof ConnectorPane){
                    ConnectorPane connectorPane = (ConnectorPane) paneNode;
                    if (connectorPane.getConnector().getNode1() == this.getNode()){
                        connectorPane.getLine().setStartX(this.xCoord);
                        connectorPane.getLine().setStartY(this.yCoord);
                    } else if (connectorPane.getConnector().getNode2() == this.getNode()){
                        connectorPane.getLine().setEndX(this.xCoord);
                        connectorPane.getLine().setEndY(this.yCoord);
                    } else {
                        System.out.println("No change needed");
                    }
                }
            }

            event.consume();
        });
        

        this.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                showNodeStyleStage(this);
            }
        });

        this.getChildren().addAll(rect, titleLabel, textLabel, imageview);
    }

    public static void showNodeStyleStage(NodePane pane){

        
        Stage stage = new Stage();
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));

        // Name Colour
        Label nameColourLabel = new Label("Name Colour");
        ObservableList<String> nameColourOptions = FXCollections.observableArrayList(
            "black",
            "red",
            "green",
            "yellow",
            "blue"
        );
        ComboBox<String> nameColourBox = new ComboBox<>(nameColourOptions);
        nameColourBox.setValue("black");

    
        // Background Colour
        Label backgroundColourLabel = new Label("Background Colour");
        ObservableList<String> backgroundColourOptions = FXCollections.observableArrayList(
            "black",
            "red",
            "green",
            "yellow",
            "blue"
        );
        ComboBox<String> BackgroundColourBox = new ComboBox<>(backgroundColourOptions);
        BackgroundColourBox.setValue("blue");
    
        // Border
        Label borderLabel = new Label("Border Style");
        ObservableList<String> borderOptions = FXCollections.observableArrayList(
            "rectangle",
            "round",
            "cloud"
        );
        ComboBox<String> BorderStyleBox = new ComboBox<>(borderOptions);
        BorderStyleBox.setValue("rectangle");
    
        // BorderColour
        Label borderColourLabel = new Label("Border Colour");
        ObservableList<String> borderColourOptions = FXCollections.observableArrayList(
            "black",
            "red",
            "green",
            "yellow",
            "blue"
        );
        ComboBox<String> BorderColourBox = new ComboBox<>(borderColourOptions);
        BorderColourBox.setValue("black");

        // Media
        Label imageLabel = new Label("Image");
        Button imageButton = new Button("Choose");
    
        // Center
        CheckBox isCenter = new CheckBox("Center Node");

        Button saveButton = new Button("Save");

        // box, label, text, image, nested
        saveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                EditableLabel nameColourOut = (EditableLabel) pane.getChildren().get(1);
                nameColourOut.setTextFill(Color.web(nameColourBox.getValue()));
                Rectangle backgroundColourOut = (Rectangle) pane.getChildren().get(0);
                backgroundColourOut.setFill(Color.web(BackgroundColourBox.getValue()));
                // Border Style goes here
                backgroundColourOut.setStroke(Color.web(BorderColourBox.getValue()));
                // Handle isCenter
                if (isCenter.isSelected()){
                    pane.setCenter(true);
                    pane.getNode().setCenter(true);
                };
                // also need to change the Pane attributes and the CustomNode values too for perfect saving
                pane.nameColour = nameColourBox.getValue();
                pane.backgroundColour = BackgroundColourBox.getValue();
                pane.borderColour = BorderColourBox.getValue();

                pane.syncAttr();
            }
        });

        imageButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                FileChooser fileChooser = new FileChooser();
                File selectedFile = fileChooser.showOpenDialog(stage);
                if (selectedFile != null) {
                    System.out.println(selectedFile.getName());
                    pane.setMedia(selectedFile.getName());
                    pane.node.setMedia(selectedFile.getName());
                    // Somehow refresh the UI or cause it to be refreshed by saving later
                    Image image = new Image("file:" + pane.getMedia());
                    ImageView imageview = (ImageView) pane.getChildren().get(3);
                    imageview.setImage(image);
                    System.out.println("Loaded: " + selectedFile.getName());
                } else {
                    System.out.println("No file selected");
                }
            }
        });

        grid.add(nameColourLabel, 0, 0);
        grid.add(nameColourBox, 1, 0);
        grid.add(backgroundColourLabel, 0, 1);
        grid.add(BackgroundColourBox, 1, 1);
        grid.add(borderLabel, 0, 2);
        grid.add(BorderStyleBox, 1, 2);
        grid.add(borderColourLabel, 0, 3);
        grid.add(BorderColourBox, 1, 3);
        grid.add(imageLabel, 0, 4);
        grid.add(imageButton, 1, 4);
        grid.add(isCenter, 0, 5);
        grid.add(saveButton, 0, 6);

        Scene scene = new Scene (grid, 300, 200);
        stage.setScene(scene);
        stage.show();
    }

    // add function to sync attributes

    public void syncAttr(){
        node.setName(this.title);
        node.setNameColour(this.nameColour);
        node.setBackgroundColour(this.backgroundColour);
        node.setBorder(this.borderType);
        node.setBorderColour(this.borderColour);
        node.setCenter(this.center);
        node.setMedia(this.media);
        node.setText(this.text);
        node.setisRendered(this.isRendered);
        node.setXCoord(this.xCoord);
        node.setYCoord(this.yCoord);
    }
}
