package mindmapsoftware;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.scene.Group;

public class CustomNode extends Element implements Serializable {
    private String name;
    private String nameColor;
    private String backgroundColor;
    private String border;
    private String borderColor;
    private ArrayList<Connector> connectedTo;
    private boolean center;
    private ArrayList<Element> content;
    private String media;
    private String text;
    private Group graphics;
    private boolean isRendered;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameColor() {
        return nameColor;
    }

    public void setNameColor(String nameColor) {
        this.nameColor = nameColor;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getBorder() {
        return border;
    }

    public void setBorder(String border) {
        this.border = border;
    }

    public String getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(String borderColor) {
        this.borderColor = borderColor;
    }

    public ArrayList<Connector> getConnectedTo() {
        return connectedTo;
    }

    public void setConnectedTo(ArrayList<Connector> connectedTo) {
        this.connectedTo = connectedTo;
    }

    public boolean isCenter() {
        return center;
    }

    public void setCenter(boolean center) {
        this.center = center;
    }

    public ArrayList<Element> getContent() {
        return content;
    }

    public void setContent(ArrayList<Element> content) {
        this.content = content;
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

    public Group getGraphics() {
        return graphics;
    }

    public void setGraphics(Group graphics) {
        this.graphics = graphics;
    }

    public boolean getIsRendered() {
        return isRendered;
    }

    public void setisRendered(boolean isRendered) {
        this.isRendered = isRendered;
    }

    public CustomNode(String name, String nameColor, String backgroundColor, String border, String borderColor, ArrayList<Connector> connectedTo, boolean center, ArrayList<Element> content, String media, String text, Group graphics, boolean isRendered) {
        // super.setId(active.getIdCounter());
        // active.setIdCounter(active.getIdCounter() + 1);
        this.name = name;
        this.nameColor = nameColor;
        this.backgroundColor = backgroundColor;
        this.border = border;
        this.borderColor = borderColor;
        this.connectedTo = connectedTo;
        this.center = center;
        this.content = content;
        this.media = media;
        this.text = text;
        this.graphics = graphics;
        this.isRendered = isRendered;

    }

    public CustomNode(){
        // super.setId(active.getIdCounter());
        // active.setIdCounter(active.getIdCounter() + 1);
        this.name = "Add name";
        this.nameColor = "black";
        this.backgroundColor = "white";
        this.border = "solid";
        this.borderColor = "black";
        this.connectedTo = new ArrayList<>();
        this.center = false;
        this.content = new ArrayList<>();
        this.media = "";
        this.text = "Add text";
        this.graphics = new Group();
        this.isRendered = false;
    }

    @Override
    public String toString() {
        ArrayList<Object> returnList = new ArrayList<>();
        returnList.add("Node");
        returnList.add(name);
        returnList.add(nameColor);
        returnList.add(backgroundColor);
        returnList.add(border);
        returnList.add(borderColor);
        returnList.add(connectedTo);
        returnList.add(center);
        returnList.add(content);
        returnList.add(media);
        returnList.add(text);
        returnList.add(graphics);
        returnList.add(isRendered);
        return returnList.toString();
    }


}
