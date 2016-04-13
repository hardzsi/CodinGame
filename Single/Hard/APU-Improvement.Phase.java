// APU:Improvement Phase
import java.util.*;

class Player {
    static int[][] grid;                                // X,Y grid containing amount of links between 
                                                        // neighbouring nodes (1-8) or 0 if no node
    static int width, height;                           // Number of grid cells on X and Y axis
    static ArrayList<Relation> relations =              // Relations between neighboring nodes 
        new ArrayList<>();                              // with no/single/double link between them
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
            String line = in.nextLine();                // Width number of chars, each: 1-8  or '.':
                                                        // node with aimed number of links or no node
            for (int x = 0; x < line.length(); ++x) {
                if (line.charAt(x) == '.') {            // No node
                    grid[x][y] = 0;
                } else {                                // Add node to grid and nodes list
                    int aimedLinks = Character.
                        getNumericValue(line.charAt(x));
                    grid[x][y] = aimedLinks;
                    nodes.add(new Node(x, y, aimedLinks));
                }
            }
        }
        displayGrid();
        getRelations(getFirstNode());               // Filling the relations list

        debug("\nnodes:");
        for (Node node : nodes) {
            node.setMaxLinks(maxLinks(node));
            debug(node.toString());
        }

        debug("\nrelations:");
        for (Relation relation : relations) {       // Display relations
            debug (relation.toString());
            System.out.println(relation.getAsString() + " 1");
        }

        debug("\noutput:");
        //System.out.println("0 0 2 0 1");          // Two coords and an int: a node, one of its neighbors, number of links connecting them
        //System.out.println("2 0 2 2 1");
    }// main()

    // Determinde maximum number of links of node from relations
    static int maxLinks(Node node) {
        int count = 0;
        for (Relation relation : relations) {
            Node[] nodeAB = relation.getNodes();
            if (node.equals(nodeAB[0]) || node.equals(nodeAB[1])) count++;
        }
        return count;
    } // maxLinks()

    // Fill relations list recursively from first provided node
    static void getRelations(Node n) {
        ArrayList<Node> neighbors = getNeighbors(n);
        for (Node neighbor : neighbors) {
            if (addNewRelation(new Relation(n, neighbor))) { getRelations(neighbor); }
        }
    }// getRelations()

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
    static ArrayList<Node> getNeighbors(Node n) {
        int xNode = n.getX();
        int yNode = n.getY();
        ArrayList<Node> neighbor = new ArrayList<>();
        if (xNode > 0) {                            // Check left neighbor
            for (int x = xNode - 1; x > 0; --x) {
                if (grid[x][yNode] != 0) {
                    neighbor.add(getNode(x, yNode));
                    break;
                }
            }
        }
        if (xNode < width - 1) {                    // Check right neighbor
            for (int x = xNode + 1; x < width; ++x) {
                if (grid[x][yNode] != 0) {
                    neighbor.add(getNode(x, yNode));
                    break;
                }
            }
        }
        if (yNode > 0) {                            // Check up neighbor
            for (int y = yNode - 1; y > 0; --y) {
                if (grid[xNode][y] != 0) {
                    neighbor.add(getNode(xNode, y));
                    break;
                }
            }
        }
        if (yNode < height - 1) {                   // Check down neighbor
            for (int y = yNode + 1; y < height; ++y) {
                if (grid[xNode][y] != 0) {
                    neighbor.add(getNode(xNode, y));
                    break;
                }
            }
        }
        return neighbor;
    }// getNeighbors()
    
    // Add relation to relations list if not was already included
    // Returns true if a relation was added, false otherwise
    static boolean addNewRelation(Relation newRelation) {
        boolean found = false;
        for (Relation relation : relations) {
            if (relation.equals(newRelation)) {
                found = true;
                break;
            }
        }
        if (!found) { 
            relations.add(newRelation);
        }
        return !found;
    }// addNewRelation()
    
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

// Node class with coordinates and actual, maximum and aimed number of links to it
// Two nodes considered equal if their coordinates are the same
class Node {
    public Node(int x, int y, int aimed) {
        this.x = x; this.y = y;
        Arrays.fill(links, 0);
        links[2] = aimed;
    }
    
    @Override public boolean equals(Object other) {
        if (other == null) { return false; }
        int otherX = ((Node)other).getX();
        int otherY = ((Node)other).getY();
        if (x == otherX && y == otherY) { return true; }
        return false;
    }
    
    @Override public String toString() { return x + "," + y + " " + Arrays.toString(links); }
    
    public int getX() { return x; }
    public int getY() { return y; }
    public int getActLinks() { return links[0]; }
    public int getMaxLinks() { return links[1]; }
    public int getAimedLinks() { return links[2]; }
    public void setActLinks(int actual) { links[0] = actual; }
    public void setMaxLinks(int maximum) { links[1] = maximum; }
    public void setAimedLinks(int aimed) { links[2] = aimed; }

    private int x;                                  // Coordinates of node
    private int y;
    private int[] links = new int[3];               // Actual, maximum and aimed number of links
} // class Node

// A relation is a pair of connecting nodes with at most two links
// Two relations considered equal _even if_ their nodes are switched
// Comparision based _only_ on number of actual links
class Relation implements Comparable<Relation> {
    Relation(Node a, Node b) { nodeA = a; nodeB = b; link = 0; }

    public int getActLinks() { return link; }
    public Node[] getNodes() { return new Node[] { nodeA, nodeB }; }
    public void setActLinks(int actual) { link = actual; }
    public String getAsString() {
        return "" + nodeA.getX() + " " + nodeA.getY() + " " +
                    nodeB.getX() + " " + nodeB.getY();
    }

    @Override public int compareTo(Relation other) {
        return link - other.getActLinks();
    }

    @Override public boolean equals(Object other) {
        if (other == null) { return false; }
        Node[] node = ((Relation)other).getNodes();
        if (((nodeA.equals(node[0]) && nodeB.equals(node[1]))  ||
             (nodeA.equals(node[1]) && nodeB.equals(node[0]))) &&
             link == ((Relation)other).getActLinks()) { return true; }
        return false;
    }

    @Override public String toString() { 
        return "relation " + nodeA + " " +
            (link == 0 ? "x" : link == 1 ? "-" : "=") + " " + nodeB;
    }

    private Node nodeA;                             // Nodes of a relation
    private Node nodeB;
    private int link;                               // Actual links between
}// class Relation