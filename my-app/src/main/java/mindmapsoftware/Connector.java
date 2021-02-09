package mindmapsoftware;

import java.io.Serializable;
import java.util.ArrayList;

public class Connector extends Element implements Serializable {

    private String label;

    public Connector(String label, String labelColor, String color, String type, boolean isRendered) {
        // super.setId(active.getIdCounter());
        // active.setIdCounter(active.getIdCounter() + 1);
        this.label = label;
        this.labelColor = labelColor;
        this.color = color;
        this.type = type;
        this.isRendered = isRendered;
    }

    public Connector() {
        // super.setId(active.getIdCounter());
        // active.setIdCounter(active.getIdCounter() + 1);
        this.label = "Add label";
        this.labelColor = "black";
        this.color = "black";
        this.type = "line";
        this.isRendered = false;

    }

    /*
    Connectors no longer require reference to what Nodes they are connected to
    At render, scan the data structure for Node's connected to Attribute
    Connectors referenced by Nodes must have one end attached to each nodes they reference
    UI control will restrict the limit to 2
    Connectors with no reference either won't render, or will render off to one side
     */

    private String labelColor;
    private String color;
    private String type;
    private boolean isRendered;

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

    @Override
    public String toString() {
        ArrayList<Object> returnList = new ArrayList<>();
        returnList.add("Connector");
        returnList.add(label);
        returnList.add(labelColor);
        returnList.add(color);
        returnList.add(type);
        returnList.add(isRendered);
        return returnList.toString();
    }
}
