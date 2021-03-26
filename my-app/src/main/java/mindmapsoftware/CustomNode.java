package mindmapsoftware;


import java.util.ArrayList;

public class CustomNode extends Element {
    private String name;
    private String nameColour;
    private String backgroundColour;
    private String border;
    private String borderColour;
    private boolean center;
    private ArrayList<CustomNode> nodeContent;
    private ArrayList<Connector> connectorContent;
    private String media;
    private String text;
    private boolean isRendered;
    private double xCoord;
    private double yCoord;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameColour() {
        return nameColour;
    }

    public void setNameColour(String nameColour) {
        this.nameColour = nameColour;
    }

    public String getBackgroundColour() {
        return backgroundColour;
    }

    public void setBackgroundColour(String backgroundColour) {
        this.backgroundColour = backgroundColour;
    }

    public String getBorder() {
        return border;
    }

    public void setBorder(String border) {
        this.border = border;
    }

    public String getBorderColour() {
        return borderColour;
    }

    public void setBorderColour(String borderColour) {
        this.borderColour = borderColour;
    }

    public boolean isCenter() {
        return center;
    }

    public void setCenter(boolean center) {
        this.center = center;
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

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean getIsRendered() {
        return isRendered;
    }

    public void setisRendered(boolean isRendered) {
        this.isRendered = isRendered;
    }

    public double getXCoord() {
        return xCoord;
    }

    public void setXCoord(double xCoord) {
        this.xCoord = xCoord;
    }

    public double getYCoord() {
        return yCoord;
    }

    public void setYCoord(double yCoord) {
        this.yCoord = yCoord;
    }


    public CustomNode(String name, String nameColour, String backgroundColour, String border, String borderColour, boolean center, ArrayList<CustomNode> nodeContent, ArrayList<Connector> connectorContent, String media, String text, boolean isRendered, double xCoord, double yCoord) {
        this.name = name;
        this.nameColour = nameColour;
        this.backgroundColour = backgroundColour;
        this.border = border;
        this.borderColour = borderColour;
        this.center = center;
        this.nodeContent = nodeContent;
        this.connectorContent = connectorContent;
        this.media = media;
        this.text = text;
        this.isRendered = isRendered;
        this.xCoord = xCoord;
        this.yCoord = yCoord;
    }

    public CustomNode(){
        this.name = "Add name";
        this.nameColour = "black";
        this.backgroundColour = "white";
        this.border = "solid";
        this.borderColour = "black";
        this.center = false;
        this.nodeContent = new ArrayList<CustomNode>();
        this.connectorContent = new ArrayList<Connector>();
        this.media = "";
        this.text = "Add text";
        this.isRendered = false;
    }

    @Override
    public String toString() {
        ArrayList<Object> returnList = new ArrayList<>();
        returnList.add("Node");
        returnList.add(name);
        returnList.add(nameColour);
        returnList.add(backgroundColour);
        returnList.add(border);
        returnList.add(borderColour);
        returnList.add(center);
        returnList.add(nodeContent);
        returnList.add(connectorContent);
        returnList.add(media);
        returnList.add(text);
        returnList.add(isRendered);
        return returnList.toString();
    }


}
