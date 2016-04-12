// APU:Improvement Phase
import java.util.*;
import java.awt.Point;

class Player {
    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int width = in.nextInt();                   // Number of cells on X axis
        in.nextLine();
        int height = in.nextInt();                  // Number of cells on Y axis
        in.nextLine();
        debug("width: " + width + ", height:" + height);
        ArrayList<Node> nodes = new ArrayList<>();  // List of all nodes
        for (int y = 0; y < height; ++y) {
            String line = in.nextLine();            // Width chars, each a number 1-8 or '.'
            debug(line);
            for (int x = 0; x < line.length(); ++x) {
                char ch = line.charAt(x);
                if (ch != '.') {
                    int maxLinks = 8;            // Determine maximum possible number of links
                    if (x == 0 || x == line.length() - 1)   maxLinks -= 2;
                    if (y == 0 || y == height - 1)          maxLinks -= 2;
                    Node node = new Node(new Point(x, y), Character.getNumericValue(ch), maxLinks);
                    nodes.add(node);
                }
            }
        }
        for (Node node : nodes) debug(node.toString());
        
        debug("output:");
        switch(nodes.size()) {
            case 2: System.out.println(nodes.get(0).getCoordStr() + " " + nodes.get(1).getCoordStr() + " " + nodes.get(0).getTargetValue()); break;
            case 3: System.out.println(nodes.get(0).getCoordStr() + " " + nodes.get(1).getCoordStr() + " " + nodes.get(0).getTargetValue());
                    System.out.println(nodes.get(1).getCoordStr() + " " + nodes.get(2).getCoordStr() + " " + nodes.get(2).getTargetValue()); break;
            default:System.out.println("no idea"); break;
        }
        
        //System.out.println("0 0 2 0 1");            // Two coords and an int: a node, one of its neighbors, number of links connecting them
        //System.out.println("2 0 2 2 1");
    }// main()
    
    static void debug(String str) {
        System.err.println(str);
    }
}

class Link {
    Link(Node node1, Node node2, int count) {
        
    }
}// class Link

class Node {
    Node(Point p, int tVal, int maxLinks) {
        coord = p;
        targetValue = tVal;
        maxLinkCount = maxLinks;
    }
    @Override
    public String toString() {
        return "Node [" + (int)coord.getX() + "," + (int)coord.getY() + "] value:" +
        value + ", target value:" + targetValue + ", links:" +
        links.size() + ", max. number of links:" + maxLinkCount;
    }
    
    // Getters
    public Point getCoord() { return coord; }
    public String getCoordStr() { return "" + (int)coord.getX() + " " + (int)coord.getY(); }
    public int getValue() { return value; }
    public int getTargetValue() { return targetValue; }
    public int getLinkCount() { return links.size(); }
    public ArrayList<Link> getLinks() { return links; }  
    
    private Point coord;
    private int value = 0;   
    private int targetValue;
    private ArrayList<Link> links = new ArrayList<>();    
    private int maxLinkCount;
}// class Node