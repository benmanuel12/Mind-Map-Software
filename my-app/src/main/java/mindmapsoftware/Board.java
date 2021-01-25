package mindmapsoftware;

import java.util.ArrayList;
import java.io.Serializable;

public class Board implements Serializable{

    //final static long serialVersionUID;
    private String name;
    private ArrayList<Element> content;
    // private int idCounter = 0;

    // public int getIdCounter() {
    //     return this.idCounter;
    // }

    // public void setIdCounter(int idCounter) {
    //     this.idCounter = idCounter;
    // }

    public Board(String name, ArrayList<Element> content) {
        this.name = name;
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Element> getContent() {
        return content;
    }

    public void setContent(ArrayList<Element> content) {
        this.content = content;
    }

    public String toString(){
        return this.content.toString();
    }




}
