package mindmapsoftware;

import java.io.*;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class MindMapSoftware {

    private static Board active;

    // This returns rather baffling results as nodes that contain other nodes are returned in their entirity including the recursive nodes within them
    // I hope for the code that displays the results to abstract over this and just display relevant data for each node and connector
    public static ArrayList<Element> search(String input, ArrayList<Element> targetList){
        ArrayList<Element> results = new ArrayList<>();

        for (Element element : targetList){
            if (element instanceof Node){
                Node node = (Node) element;
                if ((node.getName().toLowerCase().contains(input.toLowerCase())) || (node.getText().toLowerCase().contains(input.toLowerCase()))){
                    results.add(node);
                }
                if (node.getContent().size() != 0){
                    // The node contains more nodes and connectors
                    results.addAll(search(input, node.getContent()));
                }
            } else if (element instanceof Connector){
                Connector connector = (Connector) element;
                if (connector.getLabel().toLowerCase().contains(input.toLowerCase())){
                    results.add(connector);
                }
            } 
        }   
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
    }

    public static void main(String[] args){
        Board testBoard = new Board("testBoard", new ArrayList<Element>());
        Node Node_1 = new Node();
        Node Node_2 = new Node();
        testBoard.getContent().add(Node_1);
        ArrayList<Element> temp = new ArrayList<>();
        temp.add(Node_2);
        Node_1.setContent(temp);
        Node_1.setName("Stan");
        Node_2.setName("Stan");
        active = testBoard;
        System.out.println(active.getContent());
        System.out.println("Results: " + search("Stan", active.getContent()));
    }

    private void linkFinder(String text){
        ArrayList<Integer> startPoints = new ArrayList<>();
        for (int i; i<text.length(); i++){
            if (text.charAt(i) == '[') {
                startPoints.add(i);
            }
        }

        for (int i : startPoints) {
            String tempText = text.substring(i);
            if ((tempText.indexOf(']') > 0) && (tempText.indexOf('(') > tempText.indexOf(']')) && (tempText.indexOf(')') > tempText.indexOf('('))){
                //highlight the area between the [ ]
                // delete the area between the ( )
                // attempt to make it a hyper link and bind it to the area between the [ ]
                // otherwise assume its a file link and try to bind the area between the [ ] to the relevant files 
            }
        }

        }
    }
}
