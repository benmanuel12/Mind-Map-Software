package mindmapsoftware;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

public class ConnectorPane extends Pane {

    // Pane Attributes
    Line line;
    EditableLabel label;
    ObservableList<Double> dashArray;

    // Connector Attributes
    private String labelColour;
    private String colour;
    private String type;
    private boolean isRendered;
    private CustomNode node1;
    private CustomNode node2;

    // Link to Connector

    private Connector connector;


    public Line getLine() {
        return this.line;
    }

    public void setLine(Line line) {
        this.line = line;
    }

    public EditableLabel getLabel() {
        return this.label;
    }

    public void setLabel(EditableLabel label) {
        this.label = label;
    }

    public ObservableList<Double> getDashArray() {
        return this.dashArray;
    }

    public void setDashArray(ObservableList<Double> dashArray) {
        this.dashArray = dashArray;
    }

    public String getLabelColour() {
        return this.labelColour;
    }

    public void setLabelColour(String labelColour) {
        this.labelColour = labelColour;
    }

    public String getColour() {
        return this.colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
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

    public CustomNode getNode1() {
        return this.node1;
    }

    public void setNode1(CustomNode node1) {
        this.node1 = node1;
    }

    public CustomNode getNode2() {
        return this.node2;
    }

    public void setNode2(CustomNode node2) {
        this.node2 = node2;
    }

    public Connector getConnector() {
        return this.connector;
    }

    public void setConnector(Connector connector) {
        this.connector = connector;
    }
    
    public ConnectorPane(Connector connector){
        this.connector = connector;

        this.label = new EditableLabel(connector.getLabel());
        this.labelColour = connector.getLabelColour();
        this.colour = connector.getColour();
        this.type = connector.getType();
        this.isRendered = connector.getIsRendered();
        this.node1 = connector.getNode1();
        this.node2 = connector.getNode2();

        

        // Find out coords of connected Nodes
        double node1X = node1.getXCoord();
        double node1Y = node1.getYCoord();
        double node2X = node2.getXCoord();
        double node2Y = node2.getYCoord();

        // Set line to connectors
        line = new Line(node1X, node1Y, node2X, node2Y);

        // Move label
        double Xcoord = line.getStartX() + ((line.getEndX() - line.getStartX())/2);
        double Ycoord = line.getStartY() + ((line.getEndY() - line.getStartY())/2);
        label.relocate(Xcoord, Ycoord);

        // set up line style
        //line.getStrokeDashArray().addAll(dashArray);

        // Set up onclicks
        this.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                showConnectorStyleStage(this);
            }
        });
        
        // Set up children
        this.getChildren().addAll(line, label);
    }

        
    public static void showConnectorStyleStage(ConnectorPane pane){
        Stage stage = new Stage();
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
    
        // Label Colour
        Label labelColourLabel = new Label("Label Colour");
        ObservableList<String> labelColourOptions = FXCollections.observableArrayList(
            "black",
            "red",
            "green",
            "yellow",
            "blue"
        );
        ComboBox<String> labelColourBox = new ComboBox<>(labelColourOptions);
        labelColourBox.setValue("black");

        // Colour
        Label lineColourLabel = new Label("Line Colour");
        ObservableList<String> lineColourOptions = FXCollections.observableArrayList(
            "black",
            "red",
            "green",
            "yellow",
            "blue"
        );
        ComboBox<String> lineColourBox = new ComboBox<>(lineColourOptions);
        lineColourBox.setValue("black");

        Button saveButton = new Button("Save");

        // label colour, line colour, type
        saveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                EditableLabel label = (EditableLabel) pane.getChildren().get(1);
                label.setTextFill(Color.web(labelColourBox.getValue()));
                Line line = (Line) pane.getChildren().get(0);
                line.setStroke(Color.web(lineColourBox.getValue()));

                // need to set Pane attributes and Connector attributes for proper saving
                pane.labelColour = labelColourBox.getValue();
                pane.colour = lineColourBox.getValue();

                pane.syncAttr();
            }
        });

        grid.add(labelColourLabel, 0, 0);
        grid.add(labelColourBox, 1, 0);
        grid.add(lineColourLabel, 0, 1);
        grid.add(lineColourBox, 1, 1);
        grid.add(saveButton, 0, 2);

        Scene scene = new Scene (grid, 300, 100);
        stage.setScene(scene);
        stage.show();
    }

    public void syncAttr() {
        connector.setLabel(label.getText());
        connector.setLabelColour(labelColour);
        connector.setColour(colour);
        connector.setType(type);
        connector.setisRendered(isRendered);
        connector.setNode1(node1);
        connector.setNode2(node2);
    }
    
}
