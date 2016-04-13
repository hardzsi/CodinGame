// APU:Improvement Phase
import java.util.*;

class Player {
    static int width, height;                               // Number of grid cells on X and Y axis
    static ArrayList<Relation> relations =                  // Relations between neighboring nodes 
        new ArrayList<>();                                  // with no/single/double link between them
    static ArrayList<Node> nodes = new ArrayList<>();       // List of all nodes

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        width = in.nextInt();
        in.nextLine();
        height = in.nextInt();
        in.nextLine();
        debug("grid:" + width + "x" + height);
        for (int y = 0; y < height; ++y) {                  // Filling the grid
            String line = in.nextLine();                    // Width number of chars, each: 1-8  or '.':
                                                            // node with aimed number of links or no node
            for (int x = 0; x < line.length(); ++x) {
                if (line.charAt(x) != '.') {                // Add node to nodes list
                    nodes.add(new Node(x, y, Character.
                        getNumericValue(line.charAt(x))));
                }
            }
        }

        displayGrid(1);                                     // Display grid of nodes with aimed & actual number of links
        displayGrid(0);
        getRelations(nodes.get(0));                         // Fill relations list
        for (Node node : nodes) {                           // Set nodes' neighbors
            node.setNeighbors(countNeighbors(node));
        }
        debug("\nnodes:", nodes);
        debug("\nrelations:", relations);
        //debug("\nrelations with a single neighbor:",
        //    getAdjacentRelations(1, relations));

        // Logic
        do {
            ArrayList<Node> filteredNodes = getFilteredNodes(nodes);
            debug("\nfiltered nodes:", filteredNodes);
            // Output relations with a single neighbor            
            for (Node node : filteredNodes) {
                if (node.getNeighbors() == 1) {
                    Relation relation = getFirstRelation(node, relations);
                    Node neighbor = relation.getNeighbor(node);
                    debug("node:" + node.toString() + " | neighbor:" + neighbor.toString());                
                    node.setLinks(node.getAimedLinks());
                    neighbor.setLinks(neighbor.getLinks() + node.getAimedLinks());
                    relation.setLinks(node.getAimedLinks());
                    System.out.println(relation.asOutputString());
                    relations.remove(relation);
                }
            }
        } while (getFilteredNodes(nodes).size() > 0);        

        debug("\noutput:");
        //System.out.println("0 0 2 0 1");                  // Two coords and an int: a node, one of its neighbors,
        //System.out.println("2 0 2 2 1");                  // number of links connecting them
    } // main() --------------------------------------------------------------------------------------------------

    // Fill relations list recursively from first provided node
    static void getRelations(Node n) {
        ArrayList<Node> neighbors = getNeighbors(n);
        for (Node neighbor : neighbors) {
            if (addNewRelation(new Relation(n, neighbor))) {
                getRelations(neighbor);
            }
        }
    } // getRelations()

    // Count number of neighbors of a node from its relations
    static int countNeighbors(Node node) {
        int count = 0;
        for (Relation relation : relations) {
            if (relation.hasNode(node)) { ++count; }
        }
        return count;
    } // countNeighbors()

    // Return node from nodes list that has same coordinates
    // as the provided ones. Return null if not found
    static Node getNode(int x, int y) {
        for (Node node : nodes) {
            if (node.getX() == x && node.getY() == y) return node;
        }
        return null;
    } // getNode()
    
    // Return first relation from 'rels' that contains node or null if not found node
    static Relation getFirstRelation(Node node, ArrayList<Relation> rels) {
        for (Relation relation : rels) {
            if (relation.hasNode(node)) { return relation; }
        }
        return null;
    } // getFirstRelation()
    
    // Return list of nodes where actual and aimed number of nodes differ
    static ArrayList<Node> getFilteredNodes(ArrayList<Node> nodeList) {
        ArrayList<Node> result = new ArrayList<>();
        for (Node node : nodeList) {
            if (node.getLinks() != node.getAimedLinks()) {
                result.add(node);
            }
        }
        return result;
    } // getFilteredNodes()
    
    // Return list of nodes that may link to provided node
    static ArrayList<Node> getNeighbors(Node n) {
        int xNode = n.getX();
        int yNode = n.getY();
        ArrayList<Node> neighbor = new ArrayList<>();
        if (xNode > 0) {                                    // Check left neighbor
            for (int x = xNode - 1; x > 0; --x) {
                if (getNode(x, yNode) != null) {
                    neighbor.add(getNode(x, yNode));
                    break;
                }
            }
        }
        if (xNode < width - 1) {                            // Check right neighbor
            for (int x = xNode + 1; x < width; ++x) {
                if (getNode(x, yNode) != null) {
                    neighbor.add(getNode(x, yNode));
                    break;
                }
            }
        }
        if (yNode > 0) {                                    // Check up neighbor
            for (int y = yNode - 1; y > 0; --y) {
                if (getNode(xNode, y) != null) {
                    neighbor.add(getNode(xNode, y));
                    break;
                }
            }
        }
        if (yNode < height - 1) {                           // Check down neighbor
            for (int y = yNode + 1; y < height; ++y) {
                if (getNode(xNode, y) != null) {
                    neighbor.add(getNode(xNode, y));
                    break;
                }
            }
        }
        return neighbor;
    }// getNeighbors()
    
    // Return number of nodes from 'nodes' having 'num' number of neighbors
    static int countAdjacentNodes(int num, ArrayList<Node> nodes) {
        int count = 0;
        for (Node node : nodes) {
            if (node.getNeighbors() == num) { ++count; }
        }
        return count;
    } // countAdjacentNodes()

    // Return relations from 'rels' that has a node with 'num' number of neighbors
    static ArrayList<Relation> getAdjacentRelations(int num, ArrayList<Relation> rels) {
        ArrayList<Relation> adjacents = new ArrayList<>();
        for (Relation relation : rels) {
            Node[] nodeAB = relation.getNodes();
            if (nodeAB[0].getNeighbors() == num || nodeAB[1].getNeighbors() == num) {
                adjacents.add(relation);
            }
        }
        return adjacents;
    } // getAdjacentRelations()
    
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
    } // addNewRelation()
    
    static void debug(String str) { System.err.println(str); }
    
    // Display generic ArrayLists
    static <T> void debug(String str, ArrayList<T> list) { 
        System.err.println(str);
        for (T element : list) { debug(element.toString()); }
    } // debug()
    
    // Display grid with 0:actual,1:aimed,2:filtered (actual<>aimed) number of links
    static void displayGrid(int type) {
        debug("\n" + (type == 0 ? "actual" : (type == 1 ? "aimed" : "filtered")) + " links:");
        for (int y = 0; y < height; ++y) {
            String line = "";
            for (int x = 0; x < width; ++x) {
                Node node = getNode(x, y);
                if (node == null) {
                    line += type == 0 ? "." : "0";
                } else {
                    int actual = node.getLinks();
                    int aimed = node.getAimedLinks();
                    line += type == 0 ? actual : (type == 1 ? aimed : aimed - actual);
                }
            }
            debug(line);
        }
    } // displayGrid()
} // class Player -------------------------------------------------------

// Node class with coordinates, actual and aimed number of links
// leading to it as well as number of neighbors it can be linked to
// Two nodes considered equal if their coordinates are the same
class Node {
    public Node(int x, int y, int aimed) {
        this.x = x; this.y = y;
        aimedLinks = aimed;
    }
    
    @Override public boolean equals(Object other) {
        if (other == null) { return false; }
        if (x == ((Node)other).getX() &&
            y == ((Node)other).getY()) { return true; }
        return false;
    }
    
    @Override public String toString() {
        return x + "," + y + " (links:" + links + ", aimed:" +
        aimedLinks + ", neighbors:" + neighbors + ")";
    }
    
    public int getX()           { return x; }
    public int getY()           { return y; }
    public int getLinks()       { return links; }
    public int getAimedLinks()  { return aimedLinks; }
    public int getNeighbors()   { return neighbors; }
    public void setLinks(int actual)     { links = actual; }
    public void setAimedLinks(int aimed) { aimedLinks = aimed; }
    public void setNeighbors(int n)      { neighbors = n; }

    private int x;                                  // Coordinates of node
    private int y;
    private int links = 0;                          // Actual number of links
    private int aimedLinks;                         // Aimed number of links
    private int neighbors = 0;                      // Number of neighbors
    //private int[] links = new int[3];               // Actual, maximum and aimed number of links
} // class Node

// A relation is a pair of connecting nodes with at most two links
// Two relations considered equal _even if_ their nodes are switched
// Comparision based _only_ on number of actual links
class Relation implements Comparable<Relation> {
    Relation(Node a, Node b) { nodeA = a; nodeB = b; links = 0; }

    public int getLinks() { return links; }
    
    public Node[] getNodes() { return new Node[] { nodeA, nodeB }; }
    
    public Node getNeighbor(Node node) {
        return node.equals(nodeB) ? nodeA : nodeB;
    }
    
    public boolean hasNode(Node node) {
        return node.equals(nodeA) || node.equals(nodeB);
    }
    
    public void setLinks(int actualLinks) { links = actualLinks; }
    
    public String asOutputString() {
        return "" + nodeA.getX() + " " + nodeA.getY() + " " +
                    nodeB.getX() + " " + nodeB.getY() + " " + links;
    }

    @Override public int compareTo(Relation other) {
        return links - other.getLinks();
    }

    @Override public boolean equals(Object other) {
        if (other == null) { return false; }
        Node[] node = ((Relation)other).getNodes();
        if (((nodeA.equals(node[0]) && nodeB.equals(node[1]))  ||
             (nodeA.equals(node[1]) && nodeB.equals(node[0]))) &&
             links == ((Relation)other).getLinks()) { return true; }
        return false;
    }

    @Override public String toString() {            // "relation nodeA [x|-|=] nodeB" where [] represents number of links
        return "relation " + nodeA + " " +
            (links == 0 ? "x" : links == 1 ? "-" : "=") +
            " " + nodeB;
    }

    private Node nodeA;                             // Nodes of relation
    private Node nodeB;
    private int links;                              // Actual links between nodes
} // class Relation