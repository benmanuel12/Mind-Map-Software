package mindmapsoftware;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class Connector extends Element implements Serializable {

    private String label;

    public Connector(String label, String labelColor, String color, String type) {
        // super.setId(active.getIdCounter());
        // active.setIdCounter(active.getIdCounter() + 1);
        this.label = label;
        this.labelColor = labelColor;
        this.color = color;
        this.type = type;
    }

    public Connector() {
        // super.setId(active.getIdCounter());
        // active.setIdCounter(active.getIdCounter() + 1);
        this.label = "Add label";
        this.labelColor = "black";
        this.color = "black";
        this.type = "line";
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
        color = color;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        ArrayList<Object> returnList = new ArrayList<>();
        returnList.add("Connector");
        returnList.add(label);
        returnList.add(labelColor);
        returnList.add(color);
        returnList.add(type);
        return returnList.toString();
    }

//    public void addNode(Node node, ){
//        ArrayList tempArray = this.getConnectedTo();
//        tempArray.add(connector);
//        this.setConnectedTo(tempArray);
//    }
//    public void removeNode{
//        ArrayList<Connector> tempArray = this.getConnectedTo();
//        for (Connector connector : tempArray){
//            if (connector.getId() == id){
//                tempArray.remove(Arrays.asList(tempArray).indexOf(connector));
//            };
//        }
//    }

}
