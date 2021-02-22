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
    private String labelColor;
    private String color;
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

    public String getLabelColor() {
        return this.labelColor;
    }

    public void setLabelColor(String labelColor) {
        this.labelColor = labelColor;
    }

    public String getColor() {
        return this.color;
    }

    public void setColor(String color) {
        this.color = color;
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
        this.labelColor = connector.getLabelColor();
        this.color = connector.getColor();
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
                EditableLabel label = (EditableLabel) pane.getChildren().get(1);
                label.setTextFill(Color.web(labelColor.getValue()));
                Line line = (Line) pane.getChildren().get(0);
                line.setStroke(Color.web(lineColor.getValue()));
                // Handle dashed lines

                // need to set Pane attributes and Connector attributes for proper saving
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

    public void syncAttr() {
        connector.setLabel(label.getText());
        connector.setLabelColor(labelColor);
        connector.setColor(color);
        connector.setType(type);
        connector.setisRendered(isRendered);
        connector.setNode1(node1);
        connector.setNode2(node2);
    }
    
}
