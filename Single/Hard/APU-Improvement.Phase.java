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

    // Fill links list recursively from first provided node
    static void getLinks(Node n) {
        ArrayList<Node> connectingNodes = getConnectingNodes(n);
        for (Node cn : connectingNodes) {
            if (addNewLink(new Link(n, cn))) { getLinks(cn); }
        }
    }// getLinks()

    // Return node from nodes list that has same coordinates
    // as the provided ones. Return null if not found
    static Node getNode(int x, int y) {
        for (Node node : nodes) {
            if (node.getX() == x && node.getY() == y) return node;
        }
        return null;
    } // getNode()
    
    // Return top leftmost node or null if not found in nodes list
    static Node getFirstNode() {
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {            
                if (grid[x][y] != 0) return getNode(x, y);
            }
        }
        return null;
    }// getFirstNode()
    
    // Return list of nodes that may link to provided node
    static ArrayList<Node> getConnectingNodes(Node n) {
        int xNode = n.getX();
        int yNode = n.getY();
        ArrayList<Node> list = new ArrayList<>();
        if (xNode > 0) {                            // Check left link
            for (int x = xNode - 1; x > 0; --x) {
                if (grid[x][yNode] != 0) {
                    list.add(getNode(x, yNode));
                    break;
                }
            }
        }
        if (xNode < width - 1) {                    // Check right link
            for (int x = xNode + 1; x < width; ++x) {
                if (grid[x][yNode] != 0) {
                    list.add(getNode(x, yNode));
                    break;
                }
            }
        }
        if (yNode > 0) {                            // Check up link
            for (int y = yNode - 1; y > 0; --y) {
                if (grid[xNode][y] != 0) {
                    list.add(getNode(xNode, y));
                    break;
                }
            }
        }
        if (yNode < height - 1) {                   // Check down link
            for (int y = yNode + 1; y < height; ++y) {
                if (grid[xNode][y] != 0) {
                    list.add(getNode(xNode, y));
                    break;
                }
            }
        }
        return list;
    }// getConnectingNodes()
    
    // Add link to links list if not was already included
    // Returns true if a link was added, false otherwise
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

// Node class with coordinates and aimed number of connections
// Two nodes considered equal if their coordinates are the same
class Node {
    public Node(int x, int y, int aC) {
        this.x = x; this.y = y;
        aimedConnections = aC;
    }
    
    @Override public boolean equals(Object other) {
        if (other == null) { return false; }
        int otherX = ((Node)other).getX();
        int otherY = ((Node)other).getY();
        if (x == otherX && y == otherY) { return true; }        
        //int otherAC = ((Node)other).getAimedConnections();
        //if (x == otherX && y == otherY && aimedConnections == otherAC) { return true; }
        return false;
    }
    
    @Override public String toString() { return x + "," + y + " [ac:" + aimedConnections + "]"; }
    
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
    Link(Node a, Node b) { nodeA = a; nodeB = b; connection = 0; }

    public int getConnection() { return connection; }
    public Node[] getNodes() { return new Node[] { nodeA, nodeB }; }
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
        Node[] node = ((Link)other).getNodes();
        if (((nodeA.equals(node[0]) && nodeB.equals(node[1]))  ||
             (nodeA.equals(node[1]) && nodeB.equals(node[0]))) &&
             connection == ((Link)other).getConnection()) { return true; }
        return false;
    }

    @Override public String toString() { 
        return "link " + nodeA + " " +
            (connection == 0 ? "x" : connection == 1 ? "-" : "=") + " " + nodeB;
    }

    private Node nodeA;
    private Node nodeB;
    private int connection;
}// class Link