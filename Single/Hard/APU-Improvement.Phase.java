// APU:Improvement Phase 0924c (Tests 1-4,9,10 passed)
import java.util.*;

class Player {
    static int width, height;                                   // Number of grid cells on X and Y axis
    static ArrayList<Relation> relations =                      // Relations between neighboring nodes 
        new ArrayList<>();                                      // with no/single/double link between them
    static ArrayList<Node> nodes = new ArrayList<>();           // List of all nodes

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        width = in.nextInt();
        in.nextLine();
        height = in.nextInt();
        in.nextLine();
        //HACK! width = 3; height = 2;
        
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
        //HACK! nodes.clear();
        //nodes.add(new Node(0, 0, 3));nodes.add(new Node(1, 0, 4));nodes.add(new Node(2, 0, 3));
        //nodes.add(new Node(0, 1, 3));nodes.add(new Node(1, 1, 4));nodes.add(new Node(2, 1, 3));

        displayGrid("\n", 1, "");                               // Display grid of nodes wit aimed number of links
        getRelations(nodes.get(0));                             // Fill relations list
        for (Node node : nodes) {                               // Set nodes' neighbors
            node.setNeighbors(countNeighbors(node));
        }
        debug("\nnodes:", nodes);
        debug("\nrelations:", relations);
        //debug("\nrelations having 1 neighbor:", getAdjacentRelations(1));
        //debug("\nrelations having 2 neighbors:", getAdjacentRelations(2));

        debug("\noutput:");
        //System.out.println("0 0 2 0 1");                      // Two coords and an int: a node, one of its neighbors,
        //System.out.println("2 0 2 2 1");                      // number of links connecting them
        /*do {
            // 1: Establish double link with all nodes having 2 neighbors and their aimed links = 4
            for (Node node : getFilteredNodes(nodes)) {
                if (node.neighbors() == 2 && node.aimedLinks() == 4) {
                    ArrayList<Relation> adjRelations = getAdjacentRelations(node);
                    if (adjRelations.size() != 2) debug("FUCK! How???");
                    Relation relationA = adjRelations.get(0);
                    Relation relationB = adjRelations.get(1);
                    Node neighborA = relationA.getNeighbor(node);
                    Node neighborB = relationB.getNeighbor(node);
                    node.setLinks(node.aimedLinks());                    
                    neighborA.setLinks(neighborA.links() + 2);
                    neighborB.setLinks(neighborB.links() + 2);
                    relationA.setLinks(2);
                    relationB.setLinks(2);
                    debug("1A: " + relationA.asOutputString() + " | " + relationA);
                    debug("1B: " + relationB.asOutputString() + " | " + relationB);
                    displayGrid("", 2, "\n");
                    System.out.println(relationA.asOutputString());
                    System.out.println(relationB.asOutputString());
                    relations.remove(relationA);
                    relations.remove(relationB);
                    break;
                }
            }
            cleanRelations();
            
            // 2: Establish single or double link (aimed) with all nodes having only one neighbor
            for (Node node : getFilteredNodes(nodes)) {
                if (node.neighbors() == 1) {
                    Relation relation = getFirstRelation(node);
                    Node neighbor = relation.getNeighbor(node);
                    int aimed = node.aimedLinks();              // single or double link
                    node.setLinks(aimed);
                    neighbor.setLinks(neighbor.links() + aimed);
                    relation.setLinks(aimed);
                    debug("2: " + relation.asOutputString() + " | " + relation);
                    displayGrid("", 2, "\n");
                    System.out.println(relation.asOutputString());
                    relations.remove(relation);
                    break;
                }
            }
            cleanRelations();

            // 3: Establish single link with all neighbors of a node where aimed >= neighbors
            for (Node node : getFilteredNodes(nodes)) {
                if(node.aimedLinks() >= node.neighbors() &&
                            node.links() < node.aimedLinks()) {
                    for (Relation relation : getAdjacentRelations(node)) {
                        Node neighbor = relation.getNeighbor(node);
                        int increment = (int)Math.min(Math.min  // Determine possible link increment, limiting it to 2
                            (node.missingLinks(), neighbor.missingLinks()), 2);
                        node.setLinks(node.links() + increment);
                        neighbor.setLinks(neighbor.links() + increment);
                        relation.setLinks(increment);
                        debug("3: " + relation.asOutputString() + " | " + relation);
                        displayGrid("", 2, "\n");                       
                        System.out.println(relation.asOutputString());
                        relations.remove(relation);
                        if (cleanRelations()) { break; }                     
                    }
                }
            }
            cleanRelations2();
        } while (!getFilteredNodes(nodes).isEmpty()); // logic*/
        
        do {
            Node node;
            if ((node = getSpecifiedNode(8, 4, 2)) != null) {   // 1st incomplete node matching aimed links & neighbors
                debug("found 8,4,2 in node:" + node.toString());
                incrementLinksTo(2, node);                      // Increment links of node, all its neighbors
                                                                // and connections to the specified value
            } else if ((node = getSpecifiedNode(6, 3, 2)) != null) {
                debug("found 6,3,2 in node:" + node.toString());
                incrementLinksTo(2, node);
            } else if ((node = getSpecifiedNode(4, 2, 2)) != null) {
                debug("found 4,2,2 in node:" + node.toString());
                incrementLinksTo(2, node);
            } else if ((node = getSpecifiedNode(2, 1, 2)) != null) {
                debug("found 2,1,2 in node:" + node.toString());
                incrementLinksTo(2, node); 
            } else if ((node = getSpecifiedNode(7, 4, 1)) != null) {
                debug("found 7,4,1 in node:" + node.toString());
                incrementLinksTo(1, node);
            } else if ((node = getSpecifiedNode(5, 3, 1)) != null) {
                debug("found 5,3,1 in node:" + node.toString());
                incrementLinksTo(1, node);
            } else if ((node = getSpecifiedNode(3, 2, 1)) != null) {
                debug("found 3,2,1 in node:" + node.toString());
                incrementLinksTo(1, node);
            } else if ((node = getSpecifiedNode(2, 2, 1)) != null) {
                debug("found 2,2,1 in node:" + node.toString());
                incrementLinksTo(1, node); 
            } else if ((node = getSpecifiedNode(1, 1, 1)) != null) {
                debug("found 1,1,1 in node:" + node.toString());
                incrementLinksTo(1, node); 
            } else {
                debug("Specified rules did not catch" +
                      " the following nodes:", getIncompleteNodes());
                for (Node n : getIncompleteNodes()) {
                    if(n.aimedLinks() >= n.neighbors()) {
                        Relation relation = getFirstRelation(n);
                        if (relation != null && !relation.isComplete()) {
                             Node neighbor = relation.getNeighbor(n);
                            int increment = (int)Math.min(Math.min  // Determine possible link increment, limiting it to 2
                                (n.missingLinks(), neighbor.missingLinks()), 2);
                            n.setLinks(n.links() + increment);
                            neighbor.setLinks(neighbor.links() + increment);
                            relation.setLinks(increment);                      
                            if (relation.getLinks() > 0) {
                                debug("out:" + relation.asOutputString());
                                System.out.println(relation.asOutputString());                             
                            }                       
                        }
                    }
                }
                displayGrid("", 2, "\n");               
            }
            cleanRelations();
        } while(!getIncompleteNodes().isEmpty());
    } // main() --------------------------------------------------------------------------------------------------

    // Fill relations list recursively from first provided node
    static void getRelations(Node node) {
        for (Node neighbor : getNeighbors(node)) {
            if (addNewRelation(new Relation(node, neighbor))) {
                getRelations(neighbor);
            }
        }
    } // getRelations()

    // Return list of nodes that may link to provided node
    static ArrayList<Node> getNeighbors(Node node) {
        int xNode = node.getX();
        int yNode = node.getY();
        ArrayList<Node> neighbor = new ArrayList<>();
        if (xNode != 0) {                                   // Check left neighbor
            for (int x = xNode - 1; x > -1; --x) {
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
        if (yNode != 0) {                                   // Check up neighbor
            for (int y = yNode - 1; y > -1; --y) {
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
    } // getNeighbors()

    // Return node from nodes list that has same coordinates
    // as the provided ones. Return null if not found
    static Node getNode(int x, int y) {
        for (Node node : nodes) {
            if (node.getX() == x && node.getY() == y) return node;
        }
        return null;
    } // getNode()

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

    // Count number of neighbors of a node from its relations
    static int countNeighbors(Node node) {
        int count = 0;
        for (Relation relation : relations) {
            if (relation.hasNode(node)) { ++count; }
        }
        return count;
    } // countNeighbors()

    // Return first relation from relations list
    // that contains node or null if node not found
    static Relation getFirstRelation(Node node) {
        for (Relation relation : relations) {
            if (relation.hasNode(node)) { return relation; }
        }
        return null;
    } // getFirstRelation()

    // Return first incomplete node that match aimed links and 
    // neighbors, also having less links than aimed number of
    // connections to be established -- or null if no matching
    static Node getSpecifiedNode(int aimed, int neighbors, int increment) {
        for (Node node : nodes) {
            if (!node.isComplete() && (node.aimedLinks() == aimed) &&
                (node.neighbors() == neighbors) &&
                (node.links() < (node.neighbors() * increment))) {
                return node;
            }
        }
        return null;
    } // getSpecifiedNode()

    // Return incomplete relations of node that have less links
    // than increment -- or empty list if not found such ones
    static ArrayList<Relation> getSpecifiedRelations(Node node, int increment) {
        ArrayList<Relation> result = new ArrayList<>();
        for (Relation relation : relations) {
            if (!relation.isComplete() && relation.hasNode(node) &&
                (relation.getLinks() < increment)) {
                result.add(relation);
            }
        }
        return result;
    } // getSpecifiedRelations()

    // Increment links of specified node, all its neighbors
    // and connections to the specified value
    static void incrementLinksTo(int increment, Node node) {
        ArrayList<Relation> specifiedRelations = getSpecifiedRelations(node, increment);
        debug("relations to increment:", specifiedRelations);
        node.setLinks(node.neighbors() * increment);
        for (Relation relation : specifiedRelations) {
            Node neighbor = relation.getNeighbor(node);
            neighbor.setLinks(neighbor.links() + increment);
            relation.setLinks(increment - relation.getLinks());             // Incrementing just 1 if relation
                                                                            // already has a single link
            debug("out:" + relation.asOutputString());
            System.out.println(relation.asOutputString());
            relation.setLinks(increment);
        }
        displayGrid("", 2, "\n");
    } // incrementLinksTo()

    // Return list of nodes from 'nodes' where actual number of links
    // less than aimed. Empty list returned if all nodes are complete
    static ArrayList<Node> getIncompleteNodes() {
        ArrayList<Node> result = new ArrayList<>();
        for (Node node : nodes) {
            if (!node.isComplete()) { result.add(node); }
        }
        return result;
    } // getIncompleteNodes()
    
    // Return list of nodes where actual and aimed number of nodes differ
    static ArrayList<Node> getFilteredNodes(ArrayList<Node> nodeList) {
        ArrayList<Node> result = new ArrayList<>();
        for (Node node : nodeList) {
            if (node.links() != node.aimedLinks()) {
                result.add(node);
            }
        }
        return result;
    } // getFilteredNodes()
    
    // Return relations from relations list that
    // has a node with num number of neighbors
    static ArrayList<Relation> getAdjacentRelations(int num) {
        ArrayList<Relation> adjacents = new ArrayList<>();
        for (Relation relation : relations) {
            Node[] nodeAB = relation.getNodes();
            if (nodeAB[0].neighbors() == num || nodeAB[1].neighbors() == num) {
                adjacents.add(relation);
            }
        }
        return adjacents;
    } // getAdjacentRelations()

    // Return all relations from relations list that contains node,
    // returned list therefore contains all adjacents of node
    static ArrayList<Relation> getAdjacentRelations(Node node) {
        ArrayList<Relation> adjacents = new ArrayList<>();
        for (Relation relation : relations) {
            if (relation.hasNode(node)) { adjacents.add(relation); }
        }
        return adjacents;        
    } // getAdjacentRelations()

    // Remove relations from relations list that become complete:
    // whose actual links equals to aimed links for both its nodes
    // or have double links. Returns true if removal happend
    static boolean cleanRelations() {
        if (relations.isEmpty()) { return false; }
        ArrayList<Relation> removable = new ArrayList<>();
        for (Relation relation : relations) {                       // Collect
            if (relation.isComplete()) { removable.add(relation); }
        }
        if (!removable.isEmpty()) {                                 // Remove
            relations.removeAll(removable);
            return true;
        }
        return false;
    } // cleanRelations()

    static void debug(String str) { System.err.println(str); }
    
    // Display a generic arraylist
    static <T> void debug(String str, ArrayList<T> list) { 
        System.err.println(str);
        for (T element : list) { debug(element.toString()); }
    } // debug()
    
    // Display grid with 0:actual, 1:aimed, 2:filtered
    // (aimed-actual where actual<>aimed) number of links
    static void displayGrid(String before, int type, String after) {
        debug(before + "GRID " + (type == 0 ? "actual" : (type == 1 ? "aimed" : "filtered")) + " links:");
        for (int y = 0; y < height; ++y) {
            String line = "";
            for (int x = 0; x < width; ++x) {
                Node node = getNode(x, y);
                if (node == null) {
                    line += type == 0 ? "." : "0";
                } else {
                    int actual = node.links();
                    int aimed  = node.aimedLinks();
                    line += type == 0 ? actual : (type == 1 ? aimed : aimed - actual);
                }
            }
            debug(line);
        }
        if (after.equals("\n")) debug("");
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
    
    public int  getX()                   { return x; }
    public int  getY()                   { return y; }
    public int  links()                  { return links; }
    public int  aimedLinks()             { return aimedLinks; }
    public int  missingLinks()           { return aimedLinks - links; }
    public int  neighbors()              { return neighbors; }
    public boolean isComplete()          { return links == aimedLinks; }    
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
    
    public boolean isComplete() { return nodeA.isComplete() || nodeB.isComplete() || links == 2; }
    
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