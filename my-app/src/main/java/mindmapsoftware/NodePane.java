package mindmapsoftware;

import java.io.File;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
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
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.paint.Color;

public class NodePane extends Pane{

    // Pane attributes
    private Rectangle rect = new Rectangle(200, 100);
    private EditableLabel titleLabel = new EditableLabel();

    private EditableLabel textLabel = new EditableLabel();
    private ImageView imageview = new ImageView();

    // Node Attributes with a corresponding named attribute in CustomNode
    private String title; // links to name in CustomNode
    private String nameColor;
    private String backgroundColor;
    private String borderType; // links to border in CustomNode
    private String borderColor;
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

    public String getNameColor() {
        return this.nameColor;
    }

    public void setNameColor(String nameColor) {
        this.nameColor = nameColor;
    }

    public String getBackgroundColor() {
        return this.backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getBorderType() {
        return this.borderType;
    }

    public void setBorderType(String borderType) {
        this.borderType = borderType;
    }

    public String getBorderColor() {
        return this.borderColor;
    }

    public void setBorderColor(String borderColor) {
        this.borderColor = borderColor;
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
        this.nameColor = node.getNameColor();
        this.backgroundColor = node.getBackgroundColor();
        this.borderType = node.getBorder();
        this.borderColor = node.getBorderColor();
        this.center = node.isCenter();
        this.media = "file:testImage.jpg";
        this.text = node.getText();
        this.isRendered = node.getIsRendered();
        this.xCoord = node.getXCoord();
        this.yCoord = node.getYCoord();



        // change below to use refernces to node attribute

        this.rect.setFill(Color.web(this.backgroundColor));
        this.rect.setStroke(Color.web(this.borderColor));
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

        this.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                showNodeStyleStage(this);
            } else if ((event.getClickCount() == 2) && (event.getButton() == MouseButton.PRIMARY)){
                Pane parentPane = (Pane) this.getParent();
                parentPane.getChildren().removeAll(this);
                // Delete corresponding Custom Node
                // Delete all connectorPanes linked to this node
                // Delete corresponding Connectors
            }
        });

        this.getChildren().addAll(rect, titleLabel, textLabel, imageview);



    }

    public static void showNodeStyleStage(NodePane pane){

        
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
        ComboBox<String> nameColorBox = new ComboBox<>(nameColorOptions);
        nameColorBox.setValue("black");

    
        // Background Color
        Label backgroundColorLabel = new Label("Background Color");
        ObservableList<String> backgroundColorOptions = FXCollections.observableArrayList(
            "black",
            "red",
            "green",
            "yellow",
            "blue"
        );
        ComboBox<String> BackgroundColorBox = new ComboBox<>(backgroundColorOptions);
        BackgroundColorBox.setValue("blue");
    
        // Border
        Label borderLabel = new Label("Border Style");
        ObservableList<String> borderOptions = FXCollections.observableArrayList(
            "rectangle",
            "round",
            "cloud"
        );
        ComboBox<String> BorderStyleBox = new ComboBox<>(borderOptions);
        BorderStyleBox.setValue("rectangle");
    
        // BorderColor
        Label borderColorLabel = new Label("Border Color");
        ObservableList<String> borderColorOptions = FXCollections.observableArrayList(
            "black",
            "red",
            "green",
            "yellow",
            "blue"
        );
        ComboBox<String> BorderColorBox = new ComboBox<>(borderColorOptions);
        BorderColorBox.setValue("black");

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
                EditableLabel nameColorOut = (EditableLabel) pane.getChildren().get(1);
                nameColorOut.setTextFill(Color.web(nameColorBox.getValue()));
                Rectangle backgroundColorOut = (Rectangle) pane.getChildren().get(0);
                backgroundColorOut.setFill(Color.web(BackgroundColorBox.getValue()));
                // Border Style goes here
                backgroundColorOut.setStroke(Color.web(BorderColorBox.getValue()));
                // Handle isCenter

                // also need to change the Pane attributes and the CustomNode values too for perfect saving
                pane.nameColor = nameColorBox.getValue();
                pane.backgroundColor = BackgroundColorBox.getValue();
                pane.borderColor = BorderColorBox.getValue();

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

        grid.add(nameColorLabel, 0, 0);
        grid.add(nameColorBox, 1, 0);
        grid.add(backgroundColorLabel, 0, 1);
        grid.add(BackgroundColorBox, 1, 1);
        grid.add(borderLabel, 0, 2);
        grid.add(BorderStyleBox, 1, 2);
        grid.add(borderColorLabel, 0, 3);
        grid.add(BorderColorBox, 1, 3);
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
        node.setNameColor(this.nameColor);
        node.setBackgroundColor(this.backgroundColor);
        node.setBorder(this.borderType);
        node.setBorderColor(this.borderColor);
        node.setCenter(this.center);
        node.setMedia(this.media);
        node.setText(this.text);
        node.setisRendered(this.isRendered);
        node.setXCoord(this.xCoord);
        node.setYCoord(this.yCoord);
    }
}
