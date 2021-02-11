package mindmapsoftware;

import java.io.Serializable;
import java.util.ArrayList;

public class Connector extends Element implements Serializable {

    private String label;

    public Connector(String label, String labelColor, String color, String type, boolean isRendered) {
        this.label = label;
        this.labelColor = labelColor;
        this.color = color;
        this.type = type;
        this.isRendered = isRendered;
    }

    public Connector() {
        this.label = "Add label";
        this.labelColor = "black";
        this.color = "black";
        this.type = "line";
        this.isRendered = false;

    }

    private String labelColor;
    private String color;
    private String type;
    private boolean isRendered;
    private CustomNode node1;
    private CustomNode node2;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabelColor() {
        return labelColor;
    }

    public void setLabelColor(String labelColor) {
        this.labelColor = labelColor;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean getIsRendered() {
        return isRendered;
    }

    public void setisRendered(boolean isRendered) {
        this.isRendered = isRendered;
    }

    public CustomNode getNode1() {
        return node1;
    }

    public void setNode1(CustomNode node1) {
        this.node1 = node1;
    }

    public CustomNode getNode2() {
        return node2;
    }

    public void setNode2(CustomNode node2) {
        this.node2 = node2;
    }

    @Override
    public String toString() {
        ArrayList<Object> returnList = new ArrayList<>();
        returnList.add("Connector");
        returnList.add(label);
        returnList.add(labelColor);
        returnList.add(color);
        returnList.add(type);
        returnList.add(isRendered);
        returnList.add(node1);
        returnList.add(node2);
        return returnList.toString();
    }
}
