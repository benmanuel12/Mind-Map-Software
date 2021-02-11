package mindmapsoftware;

import java.util.ArrayList;
import java.io.Serializable;

public class Board implements Serializable{

    //final static long serialVersionUID;
    private String name;
    private ArrayList<CustomNode> nodeContent;
    private ArrayList<Connector> connectorContent;

    public Board(String name, ArrayList<CustomNode> nodeContent, ArrayList<Connector> connectorContent) {
        this.name = name;
        this.nodeContent = nodeContent;
        this.connectorContent = connectorContent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<CustomNode> getNodeContent() {
        return nodeContent;
    }

    public void setNodeContent(ArrayList<CustomNode> nodeContent) {
        this.nodeContent = nodeContent;
    }

    public ArrayList<Connector> getConnectorContent() {
        return connectorContent;
    }

    public void setConnectorContent(ArrayList<Connector> connectorContent) {
        this.connectorContent = connectorContent;
    }

    public String toString(){
        return this.nodeContent.toString() + this.connectorContent.toString();
    }
}
