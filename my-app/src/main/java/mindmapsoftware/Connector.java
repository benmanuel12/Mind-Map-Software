package mindmapsoftware;

import java.util.ArrayList;

public class Connector extends Element {

    private String label;

    public Connector(String label, String labelColour, String colour, String type, boolean isRendered) {
        this.label = label;
        this.labelColour = labelColour;
        this.colour = colour;
        this.type = type;
        this.isRendered = isRendered;
        // add custom nodes
    }

    public Connector() {
        this.label = "Add label";
        this.labelColour = "black";
        this.colour = "black";
        this.type = "line";
        this.isRendered = false;
        // add custom nodes
    }

    private String labelColour;
    private String colour;
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

    public String getLabelColour() {
        return labelColour;
    }

    public void setLabelColour(String labelColour) {
        this.labelColour = labelColour;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
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
        returnList.add(labelColour);
        returnList.add(colour);
        returnList.add(type);
        returnList.add(isRendered);
        returnList.add(node1);
        returnList.add(node2);
        return returnList.toString();
    }
}
