package mindmapsoftware;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

import static java.util.Arrays.*;

public class MindMapSoftware {

    private static Board active;

    public static ArrayList search(String input, ArrayList targetList){
        for (Element element : targetList.getContent()){
            if (element.getClass().getSimpleName() == "Node"){
                if ((element.getName().toLowerCase().contains(input.toLowerCase())) || (element.getText().toLowerCase().contains(input.toLowerCase()))){
                    results.add(element);
                }
                if (element.getContent() != null){
                    // The node contains more nodes and connectors
                    results = results.addAll(search(input, element.getContent()));
                }
            } else if (element.getClass().getSimpleName() == "Connector"){
                if (element.getLabel().toLowerCase().contains(input.toLowerCase())){
                    results.add(element);
                }
            } 
        };

        ArrayList<Node> results = new ArrayList<>();
        return results;
    }

    private static void save() {
        String filename = active.getName() + ".ser";

        try {
            FileOutputStream fileOut = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(active);
            out.close();
            fileOut.close();
            System.out.printf("Serialized data is saved in " + filename);
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    public static void load(String filename) {
        Board loadedBoard;
        try {
            FileInputStream fileIn = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            loadedBoard = (Board) in.readObject();
            active = loadedBoard;
            in.close();
            fileIn.close();
        } catch (IOException i) {
            i.printStackTrace();
            return;
        } catch (ClassNotFoundException c) {
            System.out.println("Board class not found");
            c.printStackTrace();
            return;
        }

        System.out.println("Deserialized Board is called: " + loadedBoard.getName());
        System.out.println("Contents: " + loadedBoard.getContent());
    }

    public static void main(String[] args){
        Board testBoard = new Board("testBoard", new ArrayList<Element>());
        Node Node_1 = new Node();
        Node Node_2 = new Node();
        testBoard.getContent().add(Node_1);
        testBoard.getContent().add(Node_2);
        ArrayList<Element> temp = new ArrayList<>();
        temp.add(Node_2);
        Node_1.setContent(temp);
        Node_1.setName("Stan");
        Node_2.setName("Stan");
        active = testBoard;
        search("Stan", active.getContent());
        

    }

    private void linkFinder(String text){

    }
}
