package mindmapsoftware;

import java.util.ArrayList;
import java.io.Serializable;

public class Board implements Serializable{
    private String name;

    public Board(String name, ArrayList<Element> content) {
        this.name = name;
        this.content = content;
    }

    private ArrayList<Element> content;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList getContent() {
        return content;
    }

    public void setContent(ArrayList<Element> content) {
        this.content = content;
    }

    public String toString(){
        return this.content.toString();
    }




}
