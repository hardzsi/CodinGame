// APU:Improvement Phase 1006a (Tests 1-7,9,10 passed) 39%
import java.util.*;

class Player {
    static int width, height;                                   // Number of grid cells on X and Y axis
    static ArrayList<Relation> relations =                      // Relations between neighboring nodes
        new ArrayList<>();                                      // with no/single/double link between them
    static ArrayList<Relation> removed = new ArrayList<>();     // for crossAlink()
    static ArrayList<Node> nodes = new ArrayList<>();           // List of all nodes
    static StringBuffer output = new StringBuffer();            // Store lines to output solution

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        width = in.nextInt();
        in.nextLine();
        height = in.nextInt();
        in.nextLine();
        //HACK! (Intermediate1)
        width = 5; height = 7;
        
        /*for (int y = 0; y < height; ++y) {                      // Fill nodes list
            String line = in.nextLine();                        // Width number of chars, each: 1-8  or '.':
                                                                // node with aimed number of links or no node
            for (int x = 0; x < line.length(); ++x) {
                if (line.charAt(x) != '.') {                    // Add node to nodes list
                    nodes.add(new Node(x, y, Character.
                        getNumericValue(line.charAt(x))));
                }
            }
        }*/
        //HACK!
        nodes.clear();
        nodes.add(new Node(0, 0, 2));nodes.add(new Node(3, 0, 2));nodes.add(new Node(1, 1, 3));
        nodes.add(new Node(2, 1, 2));nodes.add(new Node(4, 1, 1));nodes.add(new Node(0, 3, 2));
        nodes.add(new Node(2, 3, 1));nodes.add(new Node(1, 4, 5));nodes.add(new Node(3, 4, 2));
        nodes.add(new Node(0, 5, 1));nodes.add(new Node(1, 6, 3));nodes.add(new Node(4, 6, 2));
        displayGrid("\n", 1, "");                               // Display grid of nodes with aimed number of links
        collectRelations(nodes.get(0));                         // Fill relations list collecting relations recursively
        // Set nodes' neighbors
        for (Node node : nodes) {
            int count = 0;                                      // Count number of neighbors of node
            for (Relation relation : relations) {
                if (relation.hasNode(node)) { ++count; }
            }
            node.setNeighbors(count);
        }
        //debug("\nnodes:", nodes); debug("");

        // ---------------<<<<<<<<<<<<<<<<< Logic >>>>>>>>>>>>>>>>>--------------------------
        ArrayList<Node> checked = new ArrayList<>();            // Nodes with one missing links that were already checked
        connectABlevels();                                      // Establish level A and B connections (run once)
        displayGrid("", 2, "\n");
        if (hasIncompleteNodes()) { connectClevels(); }         // Establish C level connections if needed
        
        // Conserve nodes, relations and output to be able
        // to revert those to current state if needed
        //debug("\n>> conserving state of nodes, relations and output...\n");
        String outputClone = output.toString();
        ArrayList<Node> nodesClone = copyNodes(nodes);
        ArrayList<Relation> relationsClone = copyRelations(relations, nodesClone);
        
        while (true) {
            if (hasIncompleteNodes()) {                         // We have D level connections...
                debug("D level");
                
                // Revert nodes, relations and output
                debug("<< reverting state of nodes, relations and output...\n");
                output.setLength(0); output.append(outputClone);
                nodes = copyNodes(nodesClone);
                relations = copyRelations(relationsClone, nodes);
                
                Node node = getFirstMissingLinkNode(checked);   // Pick first non-checked node with one missing link
                if (node != null) {
                    checked.add(node);                          // Add to checked, so next time we won't check it
                } else {
                    debug("FUCK!!! There are no more nodes to check");
                }
                //debug("incomplete relations of node:" + node.toString(), getIncompleteRelationsOf(node));
               
                connectDlevel(node);                            // Complete first incomplete relation of node
                debug("try to establish C level connections...");
                connectClevels();                               // Establish new C level connections
                if (hasIncompleteNodes()) { debug("\nincomplete nodes remained - try again with another...\n"); }
            } else {
                break;
            }
        }
        debug("output:");                                       // Two coords and an int: a node, one of its
        System.out.println(output.toString());                  // neighbors, number of links connecting them
    } // main() --------------------------------------------------------------------------------------------------

    // A,B: Establish all level A and B connections - this method should run only once
    static void connectABlevels() {
        boolean connect;
        do {
            Node node;
            connect = false;
            // A: Connect nodes where double links needed to all neighbors
            if ((node = getSpecifiedNode(8, 4, 2)) != null) {   // Get 1st incomplete node matching aimed links& neighbors
                incrementLinksTo(2, node); connect = true;      // Increment links of node,all its neighbors & connections
            } else if ((node = getSpecifiedNode(6, 3, 2)) != null) {
                incrementLinksTo(2, node); connect = true;
            } else if ((node = getSpecifiedNode(4, 2, 2)) != null) {
                incrementLinksTo(2, node); connect = true;
            } else if ((node = getSpecifiedNode(2, 1, 2)) != null) {
                incrementLinksTo(2, node); connect = true;
            // B: Connect nodes where at least single link possible to all neighbors
            } else if ((node = getSpecifiedNode(7, 4, 1)) != null) {
                incrementLinksTo(1, node); connect = true;
            } else if ((node = getSpecifiedNode(5, 3, 1)) != null) {
                incrementLinksTo(1, node); connect = true;
            } else if ((node = getSpecifiedNode(3, 2, 1)) != null) {
                incrementLinksTo(1, node); connect = true;
            } else if ((node = getSpecifiedNode(1, 1, 1)) != null) {
                incrementLinksTo(1, node); connect = true;
            }
            //if (connect) { displayGrid("", 2, "\n"); }
        } while (connect);                                      // Until connection ocured
    } // connectABlevels()

    // C: Establish actual C level connections - connect
    // those nodes that only one incomplete relation left    
    static void connectClevels() {
        boolean connect;
        do {
            connect = false;
            if (!getSingleIncompleteRelationNodes().isEmpty()) {
                Node node =
                    getSingleIncompleteRelationNodes().get(0);  // Pick first node
                debug("C - one incomplete relationed node:" + node.toString());                
                Relation relation =
                    getIncompleteRelationsOf(node).get(0);      // Should be only one
                if (!crossAlink(relation)) {                    // Connect if relation does NOT cross a link
                    Node neighbor = relation.getNeighbor(node);
                    int relationLinks = relation.getLinks();
                    int increment = (int)Math.min(Math.min      // Determine possible link increment, limiting it to 2
                        (node.missingLinks(), neighbor.missingLinks()), 2);
                    node.setLinks(node.links() + increment);
                    neighbor.setLinks(neighbor.links() + increment);
                    relation.setLinks(increment);
                    debug("C out:" + relation.asOutputString());
                    output.append(relation.asOutputString()).append("\n");
                    relation.setLinks(relationLinks + increment);// This should be complete and removed at the end
                    connect = true;
                    displayGrid("", 2, "\n");                
                } else {
                    debug("C does NOT connect, crossing found for " + relation.toString());
                }
            }
            if (connect) { cleanRelations(); }
        } while (connect);                                      // Until connection ocured
    } // connectClevels()

    // Complete first incomplete relation of node
    // by connecting it with maximum number of links
    static void connectDlevel(Node node) {
        debug("D connect node:" + node.toString());
        Relation relation = getFirstIncompleteRelation(node);
        if (!crossAlink(relation)) {                            // Connect if relation does NOT cross a link        
            Node neighbor = relation.getNeighbor(node);
            int relationLinks = relation.getLinks();
            int increment = (int)Math.min(Math.min                  // Determine possible link increment,limiting it to 2
                (node.missingLinks(), neighbor.missingLinks()), 2);
            node.setLinks(node.links() + increment);
            neighbor.setLinks(neighbor.links() + increment);
            relation.setLinks(increment);                           // One of its nodes become complete, so does relation
            debug("D out:" + relation.asOutputString());
            output.append(relation.asOutputString()).append("\n");
            relation.setLinks(relationLinks + increment);           // Should be complete and removed at the end
            //debug("D relation after:\n" + relation.toString());
            displayGrid("", 2, "\n");
            cleanRelations();
        } else {
            debug("D does NOT connect, crossing found for " + relation.toString());
        }            
    } // connectDlevel()

    // Return true if relation crosses a single or double link
    static boolean crossAlink(Relation rel) {
        if (!canCross(rel)) { return false; }
        ArrayList<Relation> checkRelations = new ArrayList<>(relations);
        checkRelations.addAll(removed);
        boolean cross = false;
        Node[] nodeAB = rel.getNodes();
        int x0AB = nodeAB[0].getX();
        int x1AB = nodeAB[1].getX();
        int y0AB = nodeAB[0].getY();
        int y1AB = nodeAB[1].getY();
        int directionAB = (x0AB == x1AB) ? 0 : 1;               // 0:vertical 1:horizontal
        for (Relation relation : checkRelations) {
            if (canCross(relation)) {
                Node[] nodeCD = relation.getNodes();
                int links = relation.getLinks();
                int x0CD = nodeCD[0].getX();
                int x1CD = nodeCD[1].getX();
                int y0CD = nodeCD[0].getY();
                int y1CD = nodeCD[1].getY();            
                int directionCD = (x0CD == x1CD) ? 0 : 1;
                // Can cross only if directions differ and relations have links
                if (!rel.equals(relation) && directionAB != directionCD && links > 0) {              
                    if (directionAB == 0 && directionCD == 1) {     // possible crossing of vertical with horizontal
                        if ((x0CD < x0AB && x1CD > x0AB) ||
                            (x0CD > x0AB && x1CD < x0AB)    ) {
                            if ((y0AB < y0CD && y1AB > y0CD) ||
                                (y0AB > y0CD && y1AB < y0CD)    ) {
                                cross = true;
                            }
                        }
                    } else {                                        // possible crossing of horizontal with vertical
                        if ((x0AB < x0CD && x1AB > x0CD) ||
                            (x0AB > x0CD && x1AB < x0CD)    ) {
                            if ((y0CD < y0AB && y1CD > y0AB) ||
                                (y0CD > y0AB && y1CD < y0AB)    ) {
                                cross = true;
                            }
                        }
                    }
                }            
            }
        }
        return cross;
    } // crossAlink()

    // Return true if crossing possible for a relation, that
    // is, when have non-neighboring coords and not at border
    static boolean canCross(Relation rel) {
        boolean result = true;
        Node[] nodeAB = rel.getNodes();
        int x0AB = nodeAB[0].getX();
        int x1AB = nodeAB[1].getX();
        int y0AB = nodeAB[0].getY();
        int y1AB = nodeAB[1].getY();
        if (x0AB == x1AB) {                                     // vertical
            if (x0AB == 0 || x0AB == width - 1) { result = false; }
            if (Math.abs(y0AB - y1AB) == 1) { result = false; }
        } else {                                                // horizontal
            if (y0AB == 0 || y0AB == height - 1) { result = false; }
            if (Math.abs(x0AB - x1AB) == 1) { result = false; }        
        }
        return result;
    } // canCross()
    
    // Return first non-checked node with one missing link --- or null if no such
    static Node getFirstMissingLinkNode(ArrayList<Node> checked) {
        Node result = null;
        for (Node node : getIncompleteNodes()) {
            if (node.missingLinks() == 1 && !checked.contains(node)) { 
                debug("found first non-checked node with one missing link:" + node.toString());                
                result = node;
                break;
            }
        }
        return result;
    } // getFirstMissingLinkNode()

    static boolean hasIncompleteNodes() { return !getIncompleteNodes().isEmpty(); } 

    // Deep copy a Node array
    static ArrayList<Node> copyNodes(ArrayList<Node> orig) {
        ArrayList<Node> copied = new ArrayList<>(orig.size());
        for (Node node : orig) {
            copied.add(new Node(node));
        }
        return copied;
    } // copyNodes()

    // Deep copy a Relation array
    static ArrayList<Relation> copyRelations(ArrayList<Relation> rels, ArrayList<Node> nodeList) {
        ArrayList<Relation> result = new ArrayList<>(rels.size());
        for (Relation relation : rels) {
            result.add(new Relation(relation, nodeList));
        }
        return result;
    } // copyRelations()

    // Increment links of specified node, all its neighbors
    // and connections to the specified value
    static void incrementLinksTo(int increment, Node node) {
        debug((node.aimedLinks() % 2 == 0 ? "A" : "B") + " found " +
            node.aimedLinks() + "," + node.neighbors() + "," + increment +
            " [aim,nb,inc] in node:" + node.toString());
        // Determine incomplete relations of node that have less links than increment
        // NOTE: specifiedRelations list remains empty if not found such ones
        ArrayList<Relation> specifiedRelations = new ArrayList<>();
        for (Relation relation : relations) {
            if (!relation.isComplete() && relation.hasNode(node) &&
                (relation.getLinks() < increment)) {
                specifiedRelations.add(relation);
            }
        }
        node.setLinks(node.links() + (specifiedRelations.size() * increment));
        for (Relation relation : specifiedRelations) {
            Node neighbor = relation.getNeighbor(node);
            neighbor.setLinks(neighbor.links() + increment);
            relation.setLinks(increment - relation.getLinks()); // Incrementing just 1 if relation
                                                                // already has a single link
            debug((node.aimedLinks() % 2 == 0 ? "A" : "B") +
                " out:" + relation.asOutputString());
            output.append(relation.asOutputString()).append("\n");
            relation.setLinks(increment);
        }
        //cleanRelations();
    } // incrementLinksTo()

    // Fill relations list recursively from first provided node
    static void collectRelations(Node node) {
        for (Node neighbor : countNeighbors(node)) {
            if (addNewRelation(new Relation(node, neighbor))) {
                collectRelations(neighbor);
            }
        }
    } // collectRelations()

    // Return list of nodes that may link to provided node
    static ArrayList<Node> countNeighbors(Node node) {
        int xNode = node.getX();
        int yNode = node.getY();
        ArrayList<Node> neighbor = new ArrayList<>();
        if (xNode != 0) {                                       // Check left neighbor
            for (int x = xNode - 1; x > -1; --x) {
                if (getNode(x, yNode) != null) {
                    neighbor.add(getNode(x, yNode));
                    break;
                }
            }
        }
        if (xNode < width - 1) {                                // Check right neighbor
            for (int x = xNode + 1; x < width; ++x) {
                if (getNode(x, yNode) != null) {
                    neighbor.add(getNode(x, yNode));
                    break;
                }
            }
        }
        if (yNode != 0) {                                       // Check up neighbor
            for (int y = yNode - 1; y > -1; --y) {
                if (getNode(xNode, y) != null) {
                    neighbor.add(getNode(xNode, y));
                    break;
                }
            }
        }
        if (yNode < height - 1) {                               // Check down neighbor
            for (int y = yNode + 1; y < height; ++y) {
                if (getNode(xNode, y) != null) {
                    neighbor.add(getNode(xNode, y));
                    break;
                }
            }
        }
        return neighbor;
    } // countNeighbors()

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
        if (relations.contains(newRelation)) { 
            found = true;
        } else {
            relations.add(newRelation);
        }
        return !found;
    } // addNewRelation()

    // Return first incomplete relation from 'relations'
    // list that contains node or null if node not found
    static Relation getFirstIncompleteRelation(Node node) {
        for (Relation relation : relations) {
            if (!relation.isComplete() && relation.hasNode(node)) { return relation; }
        }
        return null;
    } // getFirstIncompleteRelation()

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

    // Return list of nodes from 'nodes' where actual number of links
    // less than aimed. Empty list returned if all nodes are complete
    static ArrayList<Node> getIncompleteNodes() {
        ArrayList<Node> result = new ArrayList<>();
        for (Node node : nodes) {
            if (!node.isComplete()) { result.add(node); }
        }
        return result;
    } // getIncompleteNodes()

    // Return incomplete relations of 'relations' list
    // or empty list if none found
     static ArrayList<Relation> getIncompleteRelations() {
        ArrayList<Relation> result = new ArrayList<>();
        for (Relation relation : relations) {
            if (!relation.isComplete()) { result.add(relation); }
        }
        return result;        
    } // getIncompleteRelations()   

    // Return incomplete relations of node or empty list if none found
    static ArrayList<Relation> getIncompleteRelationsOf(Node node) {
        ArrayList<Relation> result = new ArrayList<>();
        for (Relation relation : relations) {
            if (!relation.isComplete() && relation.hasNode(node)) {
                result.add(relation);
            }
        }
        return result;        
    } // getIncompleteRelationsOf()

    // Return those incomplete nodes from 'nodes' that only one
    // incomplete relation left -- or empty list if no such
    static ArrayList<Node> getSingleIncompleteRelationNodes() {
        ArrayList<Node> result = new ArrayList<>();
        for (Node node : getIncompleteNodes()) {
            int count = 0;
            for (Relation relation : relations) {           // Counting relations that contains the (incomplete) node
                if (relation.hasNode(node)) { ++count; }
            }
            if (count == 1) { result.add(node); }           // Add node to list only if it has one relation
        }
        return result;
    } // getSingleIncompleteRelationNodes()

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
            //debug("REMOVED RELATIONS:", removable);
            removed.addAll(removable);
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
        debug(before + "GRID " + (type == 0 ? "actual" :
            (type == 1 ? "aimed" : "filtered")) + " links:");
        for (int y = 0; y < height; ++y) {
            String line = "";
            for (int x = 0; x < width; ++x) {
                Node node = getNode(x, y);
                if (node == null) {
                    line += type == 0 ? "." : "0";
                } else {
                    int actual = node.links();
                    int aimed  = node.aimedLinks();
                    line += type == 0 ? actual :
                        (type == 1 ? aimed : aimed - actual);
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
    public Node(int x, int y, int aimed) {                      // Constructor
        this.x = x; this.y = y;
        aimedLinks = aimed;
    }
    
    public Node(Node orig) {                                    // Copy constructor
        x =          orig.getX();
        y =          orig.getY();
        links =      orig.links();
        aimedLinks = orig.aimedLinks();
        neighbors =  orig.neighbors();
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

    private int x;                                              // Coordinates of node
    private int y;
    private int links = 0;                                      // Actual number of links
    private int aimedLinks;                                     // Aimed number of links
    private int neighbors = 0;                                  // Number of neighbors
} // class Node

// A relation is a pair of connecting nodes with at most two links
// Two relations considered equal _even if_ their nodes are switched
// Comparision based _only_ on number of actual links
class Relation implements Comparable<Relation> {
    Relation(Node a, Node b) { nodeA = a; nodeB = b; links=0; } // Constructor

    // Copy constructor - usig nodes in list to create a new relation
    Relation(Relation orig, ArrayList<Node> nodeList) {         
        Node[] nodeAB = orig.getNodes();
        nodeA = nodeList.get(nodeList.indexOf(nodeAB[0]));
        nodeB = nodeList.get(nodeList.indexOf(nodeAB[1]));
        links = orig.getLinks();
    }

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

    @Override public String toString() {                        // 'relation nodeA [x|-|=] nodeB' where
        return "relation " + nodeA + " " +                      // [] represents number of links: 0|1|2
            (links == 0 ? "x" : links == 1 ? "-" : "=") + " " + nodeB;
    }

    private Node nodeA;                                         // Nodes of relation
    private Node nodeB;
    private int links;                                          // Actual links between nodes
} // class Relation