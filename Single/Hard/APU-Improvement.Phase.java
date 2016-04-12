// APU:Improvement Phase
import java.util.*;

class Player {
    static int[][] grid;                                // X,Y grid containing amount of links between 
                                                        // neighbouring nodes (1-8) or 0 if no node
    static int width, height;                           // Number of grid cells on X and Y axis
    static ArrayList<Link> links = new ArrayList<>();   // Links of neighboring nodes with no/single/double
                                                        // connection between them
    static ArrayList<Node> nodes = new ArrayList<>();   // List of all nodes
    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        width = in.nextInt();
        in.nextLine();
        height = in.nextInt();
        in.nextLine();
        debug("width: " + width + ", height:" + height);
        grid = new int[width][height];
        for (int y = 0; y < height; ++y) {              // Filling the grid
            String line = in.nextLine();                // Width number of chars, each: 1-8 or '.'
            for (int x = 0; x < line.length(); ++x) {
                if (line.charAt(x) == '.') {            // No node
                    grid[x][y] = 0;
                } else {                                // Add node to grid and nodes list
                    int aimedConnections = Character.
                        getNumericValue(line.charAt(x));
                    grid[x][y] = aimedConnections;
                    nodes.add(new Node(x, y, aimedConnections));
                }
            }
        }
        displayGrid();
        getLinks(getFirstNode());                   // Filling the links list

        debug("\nnodes:");
        for (Node node : nodes) {
            debug(node.toString());
        }

        debug("\nlinks:");
        for (Link link : links) {                   // Display links
            debug (link.toString());
            System.out.println(link.getAsString() + " 1");
        }
        
        debug("\noutput:");
        //System.out.println("0 0 2 0 1");          // Two coords and an int: a node, one of its neighbors, number of links connecting them
        //System.out.println("2 0 2 2 1");
    }// main()

    // Fill links list recursively from first provided node point
    static void getLinks(Point p) {
        ArrayList<Point> connectingNodes = getConnectingNodes(p);
        for (Point cp : connectingNodes) {
            if (addNewLink(new Link(p, cp))) { getLinks(cp); }
        }
    }// getLinks()
    
    // Return top leftmost node in grid or null if grid empty
    static Point getFirstNode() {
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                if (grid[x][y] != 0) return new Point(x, y);
            }
        }
        return null;
    }// getFirstNode()
    
    // Return list of nodes that may link to provided node
    static ArrayList<Point> getConnectingNodes(Point p) {
        int xNode = p.getX();
        int yNode = p.getY();
        ArrayList<Point> list = new ArrayList<>();
        if (xNode > 0) {                            // Check left link
            for (int x = xNode - 1; x > 0; --x) {
                if (grid[x][yNode] != 0) {
                    list.add(new Point(x, yNode));
                    break;
                }
            }
        }
        if (xNode < width - 1) {                    // Check right link
            for (int x = xNode + 1; x < width; ++x) {
                if (grid[x][yNode] != 0) {
                    list.add(new Point(x, yNode));
                    break;
                }
            }
        }
        if (yNode > 0) {                            // Check up link
            for (int y = yNode - 1; y > 0; --y) {
                if (grid[xNode][y] != 0) {
                    list.add(new Point(xNode, y));
                    break;
                }
            }
        }
        if (yNode < height - 1) {                   // Check down link
            for (int y = yNode + 1; y < height; ++y) {
                if (grid[xNode][y] != 0) {
                    list.add(new Point(xNode, y));
                    break;
                }
            }
        }
        return list;
    }// getConnectingNodes()
    
    // Add link to links list if not was already included
    // Returns true if a link was added, flase otherwise
    static boolean addNewLink(Link newLink) {
        boolean found = false;
        for (Link link : links) {
            if (link.equals(newLink)) {
                found = true;
                break;
            }
        }
        if (!found) { 
            links.add(newLink);
        }
        return !found;
    }// addNewLink()
    
    static void debug(String str) { System.err.println(str); }

    static void displayGrid() {
        for (int y = 0; y < grid[0].length; ++y) {
            String line = "";
            for (int x = 0; x < grid.length; ++x) {
                line += grid[x][y];
            }
            debug(line);
        }
    }// displayGrid()
}

// Simple Point class with integer coordinate values
// Note: java.awt.Point uses double coordinate values
class Point {
    public Point(int x, int y) { this.x = x; this.y = y; }
    
    @Override public boolean equals(Object other) {
        if (other == null) { return false; }
        int otherX = ((Point)other).getX();
        int otherY = ((Point)other).getY();
        if (x == otherX && y == otherY) { return true; }
        return false;
    }
    
    @Override public String toString() { return x + "," + y; }
    
    public int getX() { return x; }
    public int getY() { return y; }

    private int x;
    private int y;
} // class Point

// Node class with coordinates and aimed number of connections
class Node {
    public Node(int x, int y, int aC) {
        this.x = x; this.y = y;
        aimedConnections = aC;
    }
    
    @Override public boolean equals(Object other) {
        if (other == null) { return false; }
        int otherX = ((Node)other).getX();
        int otherY = ((Node)other).getY();
        int otherAC = ((Node)other).getAimedConnections();
        if (x == otherX && y == otherY && aimedConnections == otherAC) { return true; }
        return false;
    }
    
    @Override public String toString() { return x + "," + y + " | aimed connections: " + aimedConnections; }
    
    public int getX() { return x; }
    public int getY() { return y; }
    public int getAimedConnections() { return aimedConnections; }

    private int x;
    private int y;
    private int aimedConnections;
} // class Node

// Pair of connecting nodes with at most two connections
// Note: comparision based _only_ on number of connections
// Two links considered equal _even if_ their nodes are switched
class Link implements Comparable<Link> {
    Link(Point a, Point b) { nodeA = a; nodeB = b; connection = 0; }

    public int getConnection() { return connection; }
    public Point[] getNodes() { return new Point[] { nodeA, nodeB }; }
    public void setConnection(int c) { connection = c; }
    public String getAsString() {
        return "" + nodeA.getX() + " " + nodeA.getY() + " " +
                    nodeB.getX() + " " + nodeB.getY();
    }

    @Override public int compareTo(Link other) {
        return connection - other.getConnection();
    }

    @Override public boolean equals(Object other) {
        if (other == null) { return false; }
        Point[] nodes = ((Link)other).getNodes();
        if (((nodeA.equals(nodes[0]) && nodeB.equals(nodes[1]))  ||
             (nodeA.equals(nodes[1]) && nodeB.equals(nodes[0]))) &&
             connection == ((Link)other).getConnection()) { return true; }
        return false;
    }

    @Override public String toString() { 
        return "link (" + nodeA + " " +
            (connection == 0 ? "x" : connection == 1 ? "-" : "=") + " " + nodeB + ")";
    }

    private Point nodeA;
    private Point nodeB;
    private int connection;
}// class Link