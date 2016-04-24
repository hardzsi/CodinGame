// APU:Improvement Phase 1015a (Tests 1-7,9,10 passed) 54%
import java.util.*;

class Player {
    static int[] gridXY = {0, 0};                               // Number of grid cells on X and Y axis
    static ArrayList<Relation> relations =                      // Relations between neighboring nodes
        new ArrayList<>();                                      // with no/single/double link between them
    static ArrayList<Node> nodes = new ArrayList<>();           // List of all nodes
    static StringBuffer output = new StringBuffer();            // Store lines to output solution

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        gridXY[0] = in.nextInt();                               // Store width
        in.nextLine();
        gridXY[1] = in.nextInt();                               // Store height
        in.nextLine();
        // comment out for() to hack...
        for (int y = 0; y < gridXY[1]; ++y) {                   // Fill nodes list
            String line = in.nextLine();                        // Width number of chars, each: 1-8  or '.':
                                                                // node with aimed number of links or no node
            for (int x = 0; x < line.length(); ++x) {
                if (line.charAt(x) != '.') {                    // Add node to nodes list
                    nodes.add(new Node(x, y, Character.
                        getNumericValue(line.charAt(x))));
                }
            }
        }
        //HACK! (Intermediate2) 
        /*gridXY[0]=4; gridXY[1]=4; nodes.clear();
        nodes.add(new Node(0,0,2));nodes.add(new Node(0,1,5));nodes.add(new Node(0,3,1));nodes.add(new Node(1,0,4));
        nodes.add(new Node(1,1,7));nodes.add(new Node(1,3,4));nodes.add(new Node(2,2,1));nodes.add(new Node(3,0,3));
        nodes.add(new Node(3,1,3));nodes.add(new Node(3,2,4));nodes.add(new Node(3,3,4));*/
        displayGrid("\n", 1, "\n");                             // Display grid of nodes with aimed number of links
        collectRelations(nodes.get(0));                         // Fill relations list collecting relations recursively
        initNodeNeighbors();                                    // Set neighbors for all nodes
        //debug("\nnodes:", nodes); debug("");
        //debug("crossable relations:"); for (Relation rel : relations) {if (rel.isCrossable()) {debug(rel);}}

        // ################################################### Logic #####################################################
        // ###############################################################################################################
        connectABlevels();                                      // Establish level A and B connections (run once)
        if (hasIncompleteNodes()) { debug("C level"); connectClevels(false); } // Establish C level connections if needed
        displayGrid("\n", 2, "\n");     
        // Conserve lists for reverting their states if necessary
        debug(hasIncompleteNodes() ? ">> conserving state of nodes,relations and output" : "done!");
        String outputClone = output.toString();
        ArrayList<Node> nodesClone = nodes;
        ArrayList<Relation> relationsClone = relations;

        ArrayList<Node> checkedNodes = new ArrayList<>();       // Store nodes with missing links that were already checked
        while (true) {
            if (hasIncompleteNodes()) {                         // We should use a D level connection
                debug("\nNEW ROUND...");
                // Revert nodes, relations and output
                debug("<< reverting state of nodes, relations and output");
                output.setLength(0); output.append(outputClone);
                nodes = copyNodes(nodesClone);
                relations = copyRelations(relationsClone, nodes);

                //debug("checkedNodes:"); for (Node c : checkedNodes) { debug(c.toString()); }
                Node node = null;
                for (int missing = 1; missing < 7 && node == null; ++missing) {
                    for (Node n : getIncompleteNonCheckedNodesMissingLinks(missing, checkedNodes)) {
                        node = n;
                        /*debug("found first non-checked node with " + missing + " missing link" +
                                (missing > 1 ? "s:" : ":") + node);
                        debug("incomplete relations:", getIncompleteRelationsOf(node));*/
                        checkedNodes.add(node);
                        break;
                    }
                }
                if (node == null) { output.append("FUCK!!! There are no more nodes to check"); break; }
                debug("D level"); connectDlevel(node);          // Complete first incomplete relation of node
                debug("C level"); connectClevels(true);         // Establish new C level connections
                if (hasIncompleteNodes()) { debug("incomplete nodes remained - try again with another..."); }
            } else {
                break;
            }
        }
        debug("output:");                                       // Two coords and an int: a node, one of its
        System.out.println(output.toString());                  // neighbors, number of links connecting them
    } // main() -------------------------------------------------------------------------------------------------

    // C: Establish actual C level connections - connect
    // those nodes that only one incomplete relation left
    static void connectClevels(boolean canDisplayGrid) {
        boolean connect;
        do {
            connect = false;
            if (!getSingleIncompleteRelationNodes().isEmpty()) {
                Node node =
                    getSingleIncompleteRelationNodes().get(0);  // Pick first node
                Relation relation =
                    getIncompleteRelationsOf(node).get(0);      // Should be only one
                if (!crossAlink(relation)) {                    // Connect if relation does NOT cross a link
                    debug("connecting " + relation);
                    Node neighbor = relation.getNeighbor(node);
                    int relationLinks = relation.getLinks();
                    int increment = (int)Math.min(Math.min      // Determine possible link increment,limiting it to 2
                        (node.missingLinks(), neighbor.missingLinks()), 2 - relationLinks);
                    node.setLinks(node.links() + increment);
                    neighbor.setLinks(neighbor.links() + increment);
                    relation.setLinks(increment);
                    debug("C out:" + relation.asOutputString());
                    output.append(relation.asOutputString()).append("\n");
                    relation.setLinks(relationLinks + increment);// This should be complete and removed at the end
                    connect = true;
                    if (canDisplayGrid) { displayGrid("", 2, "\n"); }
                } else {
                    debug("C does NOT connect, crossing found for " + relation);
                }
            } else {
                debug("single incomplete relation nodes is empty - no connection");
            }
        } while (connect);                                      // Until connection ocured
    }

    // Complete first incomplete relation of node
    // by connecting it with maximum number of links
    static void connectDlevel(Node node) {
        Relation relation = getFirstIncompleteRelation(node);   // this may be faster than getIncompleteRelationsOf()...
        //Relation relation = getIncompleteRelationsOf(node).get(0);
        if (!crossAlink(relation)) {                            // Connect if relation does NOT cross a link
            debug("connecting " + relation);
            Node neighbor = relation.getNeighbor(node);
            int relationLinks = relation.getLinks();
            int increment = (int)Math.min(Math.min              // Determine possible link increment,limiting it to 2
                (node.missingLinks(), neighbor.missingLinks()), 2 - relationLinks);
            node.setLinks(node.links() + increment);
            neighbor.setLinks(neighbor.links() + increment);
            relation.setLinks(increment);                       // One of its nodes become complete, so does relation
            debug("D out:" + relation.asOutputString());
            output.append(relation.asOutputString()).append("\n");
            relation.setLinks(relationLinks + increment);       // Should be complete and removed at the end
            //displayGrid("", 2, "\n");
        } else {
            debug("D does NOT connect, crossing found for " + relation);
        }
    }

    // Return true if relation crosses a single or double link
    static boolean crossAlink(Relation rel) {
        if (!rel.isCrossable()) { return false; }
        ArrayList<Relation> checkRelations = new ArrayList<>(relations);
        boolean cross = false;
        Node[] nodeAB = rel.getNodes();
        int xA = nodeAB[0].getX(); int xB = nodeAB[1].getX();
        int yA = nodeAB[0].getY(); int yB = nodeAB[1].getY();
        int directionAB = rel.isVertical() ? 0 : 1;             // 0:vertical 1:horizontal
        for (Relation relation : checkRelations) {
            if (relation.isCrossable()) {
                Node[] nodeCD = relation.getNodes();
                int links = relation.getLinks();
                int xC = nodeCD[0].getX(); int xD = nodeCD[1].getX();
                int yC = nodeCD[0].getY(); int yD = nodeCD[1].getY();            
                int directionCD = relation.isVertical() ? 0 : 1;
                // Can cross only if directions differ and relations have links
                if (!rel.equals(relation) && directionAB != directionCD && links > 0) {              
                    if (directionAB == 0 && directionCD == 1) { // possible crossing of vertical with horizontal
                        if ((xC < xA && xD > xA) || (xC > xA && xD < xA)) {
                            if ((yA < yC && yB > yC) || (yA > yC && yB < yC)) {
                                cross = true;
                            }
                        }
                    } else {                                    // possible crossing of horizontal with vertical
                        if ((xA < xC && xB > xC) || (xA > xC && xB < xC)) {
                            if ((yC < yA && yD > yA) || (yC > yA && yD < yA)) {
                                cross = true;
                            }
                        }
                    }
                }            
            }
        }
        return cross;
    } // crossAlink()

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
        } while (connect);                                      // Until connection ocured
    }

    // Increment links of specified node, all its neighbors
    // and connections to the specified value
    static void incrementLinksTo(int increment, Node node) {
        debug((node.aimedLinks() % 2 == 0 ? "A" : "B") + " found " +
            node.aimedLinks() + "," + node.neighbors() + "," + increment +
            " [aim,nb,inc] in node:" + node);
        // Determine incomplete relations of node that have less links than increment
        ArrayList<Relation> specifiedRelations = new ArrayList<>(); // NOTE: may remains empty if not found such ones
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
            relation.setLinks(increment - relation.getLinks()); // Incrementing just 1 if relation has a single link
            debug((node.aimedLinks() % 2 == 0 ? "A" : "B") +
                " out:" + relation.asOutputString());
            output.append(relation.asOutputString()).append("\n");
            relation.setLinks(increment);
        }
    }

    // Return first incomplete relation from 'relations'
    // list that contains node or null if node not found
    static Relation getFirstIncompleteRelation(Node node) {
        for (Relation relation : relations) {
            if (!relation.isComplete() && relation.hasNode(node)) { return relation; }
        }
        return null;
    }

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
    }

    static boolean hasIncompleteNodes() { return !getIncompleteNodes().isEmpty(); }

    // Return nodes where number of links less than aimed
    // Empty list returned if all nodes complete
    static ArrayList<Node> getIncompleteNodes() {
        ArrayList<Node> result = new ArrayList<>();
        for (Node node : nodes) {
            if (!node.isComplete()) { result.add(node); }
        }
        return result;
    }

    // Return non-checked incomplete nodes having the given
    // number of missing links - or empty list if non such found
    static ArrayList<Node> getIncompleteNonCheckedNodesMissingLinks(int missing, ArrayList<Node> checked) {
        ArrayList<Node> result = new ArrayList<>();
        for (Node node : nodes) {
            if (!node.isComplete() && !checked.contains(node) &&
                node.missingLinks() == missing) { result.add(node); }
        }
        return result;
    }

    // Return incomplete relations - or empty list if none found
     static ArrayList<Relation> getIncompleteRelations() {
        ArrayList<Relation> result = new ArrayList<>();
        for (Relation relation : relations) {
            if (!relation.isComplete()) { result.add(relation); }
        }
        return result;        
    }  

    // Return incomplete relations of node or empty list if none found
    static ArrayList<Relation> getIncompleteRelationsOf(Node node) {
        ArrayList<Relation> result = new ArrayList<>();
        for (Relation relation : relations) {
            if (!relation.isComplete() && relation.hasNode(node)) {
                result.add(relation);
            }
        }
        return result;        
    }

    // Return those incomplete nodes from 'nodes' that only one
    // incomplete relation left -- or empty list if no such
    static ArrayList<Node> getSingleIncompleteRelationNodes() {
        ArrayList<Node> result = new ArrayList<>();
        for (Node node : getIncompleteNodes()) {
            if (getIncompleteRelationsOf(node).size() == 1) {   // Add node only if it has one incomplete relation
                result.add(node);
            }
        }
        return result;
    }

    // Fill relations list recursively from first provided node
    static void collectRelations(Node node) {
        for (Node neighbor : countNeighbors(node)) {
            if (addNewRelation(new Relation(node, neighbor, gridXY))) {
                collectRelations(neighbor);
            }
        }
    }

    // Return list of nodes that may link to provided node
    static ArrayList<Node> countNeighbors(Node node) {
        int xNode = node.getX(); int yNode = node.getY();
        int width = gridXY[0];   int height = gridXY[1];
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
    }

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
    }

    // Initialize neighbors of all nodes by counting & setting those
    static void initNodeNeighbors() {
        for (Node node : nodes) {
            int count = 0;                                          // Count number of neighbors of node
            for (Relation relation : relations) {
                if (relation.hasNode(node)) { ++count; }
            }
            node.setNeighbors(count);
        }
    }

    // Deep copy a Node array
    static ArrayList<Node> copyNodes(ArrayList<Node> orig) {
        ArrayList<Node> copied = new ArrayList<>(orig.size());
        for (Node node : orig) {
            copied.add(new Node(node));
        }
        return copied;
    }

    // Deep copy a Relation array
    static ArrayList<Relation> copyRelations(ArrayList<Relation> rels, ArrayList<Node> nodeList) {
        ArrayList<Relation> result = new ArrayList<>(rels.size());
        for (Relation relation : rels) {
            result.add(new Relation(relation, nodeList));
        }
        return result;
    }

    static void debug(String str) { System.err.println(str); }
    
    // Display a generic arraylist
    static <T> void debug(String str, ArrayList<T> list) {
        System.err.println(str);
        for (T element : list) { debug(element.toString()); }
    }
    
    // Display grid with 0:actual, 1:aimed, 2:filtered
    // (aimed-actual where actual<>aimed) number of links
    static void displayGrid(String before, int type, String after) {
        debug(before + "GRID " + (type == 0 ? "actual" :
            (type == 1 ? "aimed" : "filtered")) + " links:");
        for (int y = 0; y < gridXY[1]; ++y) {
            String line = "";
            for (int x = 0; x < gridXY[0]; ++x) {
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
    }

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
    // Constructor
    public Relation(Node a, Node b, int[] gridXY) {
        nodeA = a;
        nodeB = b;
        links = 0;
        // Determine if crossable: has non-neighboring nodes and not at border
        crossable = false;
        int xA = nodeA.getX(); int xB = nodeB.getX();
        int yA = nodeA.getY(); int yB = nodeB.getY();
        if (isVertical()) {
            if (xA > 0 && xA < gridXY[0] - 1 && Math.abs(yA - yB) > 1)
                    { crossable = true; }
        } else {
            if (yA > 0 && yA < gridXY[1] - 1 && Math.abs(xA - xB) > 1)
                    { crossable = true; }
        }  
    } 

    // Copy constructor - usig nodes in list to create a new relation
    public Relation(Relation orig, ArrayList<Node> nodeList) {         
        Node[] nodeAB = orig.getNodes();
        nodeA = nodeList.get(nodeList.indexOf(nodeAB[0]));
        nodeB = nodeList.get(nodeList.indexOf(nodeAB[1]));
        links = orig.getLinks();
        crossable = orig.isCrossable();
    }
    
    public Node[]   getNodes() { return new Node[] { nodeA, nodeB }; }
    public int      getLinks() { return links; }      
    public Node     getNeighbor(Node node) {
        return node.equals(nodeB) ? nodeA : nodeB;
    }
    public boolean  hasNode(Node node) {
        return node.equals(nodeA) || node.equals(nodeB);
    }
    public boolean  isComplete()   { return nodeA.isComplete() ||
                                           nodeB.isComplete() || links == 2; }
    public boolean  isVertical()   { return nodeA.getX() == nodeB.getX(); }
    public boolean  isHorizontal() { return nodeA.getY() == nodeB.getY(); }
    public boolean  isCrossable()  { return crossable; }
    
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
    private boolean crossable;                                  // True if crossing possible for relation
} // class Relation