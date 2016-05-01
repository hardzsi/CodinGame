// APU:Improvement Phase 1024
import java.util.*;

class Player {
    static int[] gridXY = {0, 0};                               // Number of grid cells on X,Y axis
    static ArrayList<Relation> relations = new ArrayList<>();   // Relations bw.neighboring nodes w/ no/sg/db link bw.them
    static ArrayList<Node> nodes = new ArrayList<>();           // List of all nodes
    static StringBuffer output = new StringBuffer();            // Store lines to output solution

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        gridXY[0] = in.nextInt();                               // Store width
        in.nextLine();
        gridXY[1] = in.nextInt();                               // Store height
        in.nextLine();
        for (int y = 0; y < gridXY[1]; ++y) {                   // Fill nodes list line by line
            String line = in.nextLine();                        // A line: 'width' number of chars, each 1-8  or '.'
                                                                // representing node with aimed number of links or no node
            for (int x = 0; x < line.length(); ++x) {
                if (line.charAt(x) != '.') {                    // Add node to nodes list
                    nodes.add(new Node(x, y, Character.
                        getNumericValue(line.charAt(x))));
                }
            }
        }
        displayGrid("\n", 1, "\n");                             // Display grid of nodes with aimed number of links
        collectRelations(nodes.get(0));                         // Fill relations list collecting relations recursively
        initNodeNeighbors();                                    // Set neighbors for all nodes
        // ###########
        // ## Logic ##
        // ##############################################################################################################
        connectTypeA(true);                                     // Establish type A connections (run once)
        connectTypeBCD(false);                                  // Establish all possible type B, C and D connections
        // Conserve lists for reverting their states if needed
        String outputClone = output.toString();
        ArrayList<Node> nodesClone = nodes;
        ArrayList<Relation> relationsClone = relations;

        ArrayList<Node> checkedNodes = new ArrayList<>();       // Store nodes w/ missing links that were already checked
        ArrayList<Relation> checkedRels = new ArrayList<>();    // Store relations that were already checked
        if (hasIncompleteNodes()) { debug("Type E started"); }
        while (hasIncompleteNodes()) {                          // We should establish an E type connection
            // Revert nodes, relations and output
            //debug("<< reverting state of nodes, relations and output");
            output.setLength(0); output.append(outputClone);
            nodes = copyNodes(nodesClone);
            relations = copyRelations(relationsClone, nodes);
            // Get a checkable node and relation
            Node node = null;                                   // Checkable node
            Relation relation = null;                           // Checkable relation
            for (int missing = 1; missing < 7 && node == null; ++missing) {
                for (Node nd : getIncompleteNonCheckedNodesMissingLinks(missing, checkedNodes)) {
                    ArrayList<Relation> rels = getIncompleteNonCheckedNonCrossingRelationsOf(nd, checkedRels);
                    if (!rels.isEmpty()) {                      // Pick 1st suitable relation if exist, then exit loop
                        node = nd;
                        relation = rels.get(0);
                        checkedRels.add(new Relation(relation, nodes));
                        if (rels.size() == 1) { checkedNodes.add(new Node(nd)); }
                        break;
                    } else {                                    // If no suitable relations, store node and pick next one
                        checkedNodes.add(new Node(nd));
                    }
                }
            }
            if (node == null) {
                // Revert nodes, relations and output
                debug("<< reverting state of nodes, relations and output");
                output.setLength(0); output.append(outputClone);
                nodes = copyNodes(nodesClone);
                relations = copyRelations(relationsClone, nodes);
                debug("Type E finished: giving up, no more nodes to check\n");
                break;
            }
            connectTypeE(node, relation, false);                 // Establish type E connection
            connectTypeBCD(false);                               // Establish all possible type B, C and D connections
        } // Type E
        
        // If still have incomplete nodes: find all incomplete nodes having 2 missing links and 2 unlinked relations.
        // Link both unlinked relations then establish possible type B-C-D connections to find out if we could finish
        if (hasIncompleteNodes()) {
            debug("Type F started");
            // Collect candidates to 'checkNodes' list
            checkedNodes.clear();
            for (Node node : nodes) {
                if (!node.isComplete() && node.missingLinks() == 2 &&
                    getIncompleteUnlinkedRelationsOf(node).size() == 2) {
                    checkedNodes.add(node);
                }
            }
            Node node;
            ArrayList<Node> checkedNodesClone;
            while (true) {
                node = checkedNodes.remove(0);
                // Connect both unlinked relation of node
                for (Relation relation : getIncompleteUnlinkedRelationsOf(node)) {
                    relation.setLinks(1);
                    Node neighbor = relation.getNeighbor(node);
                    neighbor.setLinks(neighbor.links() + 1);
                    //debug("F out:" + relation.asOutputString());
                    output.append(relation.asOutputString()).append("\n");
                }
                node.setLinks(node.links() + 2);
                connectTypeBCD(false);                          // Establish all possible type B, C and D connections
                if (!hasIncompleteNodes()) { break; }
                // Revert nodes, relations and output
                //debug("<< reverting state of nodes, relations and output");
                output.setLength(0); output.append(outputClone);
                nodes = copyNodes(nodesClone);
                relations = copyRelations(relationsClone, nodes);                
                if (checkedNodes.isEmpty()) {
                        debug("Type F finished: giving up, no more nodes to check\n");
                        break;
                }
                // Rebuild checkedNodes list from new nodes
                checkedNodesClone = copyNodes(checkedNodes);
                checkedNodes.clear();
                for (Node nd : checkedNodesClone) {
                    checkedNodes.add(nodes.get(nodes.indexOf(nd)));
                }
            }
        } // Type F
        
        // If still have incomplete nodes, it should be 'CG' or 'Multiple solutions 2'
        
        // Solve 'Multiple solutions 2'
        if (hasIncompleteNodes()) {
            debug("Solving 'Multiple solutions 2' started");            
            connectTypeD(4, 4, false);                          // Estab. D conns w/ 4 aimed links & 4 unlinked relations
            connectTypeBCD(false);                              // Establish all possible type B, C and D connections
            if (hasIncompleteNodes()) {
                // Revert nodes, relations and output
                debug("<< reverting state of nodes, relations and output");
                output.setLength(0); output.append(outputClone);
                nodes = copyNodes(nodesClone);
                relations = copyRelations(relationsClone, nodes);            
            }
        }

        // Solve 'CG': connect adjacent non-linked non-crossing relations with a single link
        if (hasIncompleteNodes()) {
            debug("\nSolving 'CG' started");
            ArrayList<Relation> rels = new ArrayList<>();       // Collect all adjacent non-linked non-crossing relations
            for (Node node : getIncompleteNodes()) {
                for (Relation relation : getIncompleteUnlinkedRelationsOf(node)) {
                    if (relation.getDistance() == 1 && !rels.contains(relation)) {
                        rels.add(relation);
                    }
                }
            }
            for (Relation relation : rels) { connectRel(relation, true); } // Connect all adjacents
            connectTypeBCD(true);                               // Connect the remaining
        }

        debug("\noutput:");                                     // Two coords and an int: a node, one of its
        System.out.println(output.toString());                  // neighbors, number of links connecting them
    } // main() ########################################################################################################


    // A: Connect nodes where single or double links possible to all neighbors
    static void connectTypeA(boolean canDisplay) {
        boolean connect;
        do {
            Node node;
            connect = false;
            // Connect nodes where double links needed to all neighbors
            if ((node = getMatchingNode(8, 4)) != null) {       // Get 1st incomplete node matching aimed links& neighbors
                incrementLinksTo(2, node, canDisplay); connect = true; // Inc.links of node,all its neighbors& connections
            } else if ((node = getMatchingNode(6, 3)) != null) {
                incrementLinksTo(2, node, canDisplay); connect = true;
            } else if ((node = getMatchingNode(4, 2)) != null) {
                incrementLinksTo(2, node, canDisplay); connect = true;
            } else if ((node = getMatchingNode(2, 1)) != null) {
                incrementLinksTo(2, node, canDisplay); connect = true;
            // Connect nodes where at least single link possible to all neighbors
            } else if ((node = getMatchingNode(7, 4)) != null) {
                incrementLinksTo(1, node, canDisplay); connect = true;
            } else if ((node = getMatchingNode(5, 3)) != null) {
                incrementLinksTo(1, node, canDisplay); connect = true;
            } else if ((node = getMatchingNode(3, 2)) != null) {
                incrementLinksTo(1, node, canDisplay); connect = true;
            } else if ((node = getMatchingNode(1, 1)) != null) {
                incrementLinksTo(1, node, canDisplay); connect = true;
            }
        } while (connect);                                      // Until connection ocured
    }

    // B: Connect nodes that only one incomplete relation left
    // Return number of established connections    
    static int connectTypeB(boolean canDisplay) {
        boolean connect;
        int connections = 0;
        do {
            connect = false;
            // Collect incomplete nodes that only one incomplete
            // relation left. Note: list empty if no such found
            ArrayList<Node> singleIncompleteRelationNodes = new ArrayList<>();
            for (Node node : getIncompleteNodes()) {
                if (getIncompleteNonCrossedRelationsOf(node).size() == 1) {
                    singleIncompleteRelationNodes.add(node);    // Add node only if it has 1 incomp.non-crossing relation
                }
            }
            if (!singleIncompleteRelationNodes.isEmpty()) {
                Node node = singleIncompleteRelationNodes.get(0); // Pick first node
                Relation relation =
                    getIncompleteNonCrossedRelationsOf(node).get(0); // Should be only one
                Node neighbor = relation.getNeighbor(node);
                int relationLinks = relation.getLinks();
                int increment = (int)Math.min(Math.min          // Determine possible link increment,limiting it to 2
                    (node.missingLinks(), neighbor.missingLinks()), 2 - relationLinks);
                node.setLinks(node.links() + increment);
                neighbor.setLinks(neighbor.links() + increment);
                relation.setLinks(increment);
                if (canDisplay) { debug("B out:" + relation.asOutputString()); }
                output.append(relation.asOutputString()).append("\n");
                relation.setLinks(relationLinks + increment);   // This should be complete
                connect = true;
                connections++;
            }
        } while (connect);                                      // Until connection occured
        return connections;
    }

    // C: Connect second link to relations of such nodes
    // that 2 relations left with 1-1 missing link
    // Return number of established connections
    static int connectTypeC(boolean canDisplay) {
        int connections = 0;
        for (Node node : nodes) {
            if (node.missingLinks() == 2) {                     // Find nodes having 2 missing links - Note: these nodes
                                                                // can have 1-4 incomplete relation(s) with 0 or 1 link
                ArrayList<Relation> rels = new ArrayList<>();   // Collect nodes' all incomplete relations having 1 link
                for (Relation relation : relations) {
                    if (relation.hasNode(node) && !relation.isComplete() &&
                            relation.getLinks() == 1) {
                        rels.add(relation);
                    }
                }
                if (rels.size() == 2) {                         // Found a node that 2 relations left
                    node.setLinks(node.links() + 2);
                    for (Relation relation : rels) {            // Let's connect both neighbors with second link
                        Node neighbor = relation.getNeighbor(node);
                        if (canDisplay) { debug("C out:" + relation.asOutputString()); }
                        output.append(relation.asOutputString()).append("\n");
                        neighbor.setLinks(neighbor.links() + 1);
                        relation.setLinks(2);
                    }
                    connections += 2;
                }
            }
        }
        return connections;
    }

    // D: Connect single link to non-linked nodes with 'aimed' aimed
    // links that has 'unlinked' non-crossed, unlinked relations left
    // Return number of established connections
    static int connectTypeD(int aimed, int unlinked, boolean canDisplay) {
        int connections = 0;
        for (Node node : nodes) {
            if (!node.isComplete() && node.aimedLinks() == aimed && node.isUnlinked()) {
                ArrayList<Relation> rels = new ArrayList<>();
                for (Relation relation : relations) {
                    if (relation.hasNode(node) && !relation.isComplete() &&
                      !isCrossed(relation) && relation.isUnlinked()) {
                        rels.add(relation);
                    }
                }
                if (rels.size() == unlinked) {
                    node.setLinks(node.links() + unlinked);
                    for (Relation relation : rels) {            // Let's connect all neighbors with 1-1 link
                        relation.setLinks(1);
                        Node neighbor = relation.getNeighbor(node);
                        neighbor.setLinks(neighbor.links() + 1);
                        if (canDisplay) { 
                            debug("D (" + aimed + "-" + unlinked + "-1) out:" + relation.asOutputString());
                        }
                        output.append(relation.asOutputString()).append("\n");
                    }
                    connections += unlinked;
                }
            }
        }
        return connections;
    }

    // B-C-D: Establish possible Type B, C and D connections
    // until there is at least 1 new connection from any type
    static void connectTypeBCD(boolean canDisplay) {
        if (canDisplay) { debug("Type B+C+D started"); }
        int connections;
        do {
            connections = 0;
            if (hasIncompleteNodes()) { connections += connectTypeB(canDisplay); }
            if (hasIncompleteNodes()) { connections += connectTypeC(canDisplay); }
            if (hasIncompleteNodes()) { connections += connectTypeD(3, 2, canDisplay); }
            if (hasIncompleteNodes()) { connections += connectTypeD(5, 3, canDisplay); }
        } while (connections > 0);
    }

    // E: Complete an incomplete non-crossing relation
    // of node with maximum number of links
    static void connectTypeE(Node node, Relation relation, boolean canDisplay) {
        Node neighbor = relation.getNeighbor(node);
        int relationLinks = relation.getLinks();
        int increment = (int)Math.min(Math.min                  // Determine possible link increment,limiting it to 2
            (node.missingLinks(), neighbor.missingLinks()), 2 - relationLinks);
        node.setLinks(node.links() + increment);
        neighbor.setLinks(neighbor.links() + increment);
        relation.setLinks(increment);                           // One of its nodes become complete, so does relation
        if (canDisplay) { debug("E out:" + relation.asOutputString()); }
        output.append(relation.asOutputString()).append("\n");
        relation.setLinks(relationLinks + increment);           // Should be complete
    }

    // REL: Complete a non-linked non-crossing relation with a single link
    static void connectRel(Relation relation, boolean canDisplay) {
        Node[] nodeAB = relation.getNodes();
        nodeAB[0].setLinks(nodeAB[0].links() + 1);
        nodeAB[1].setLinks(nodeAB[1].links() + 1);
        relation.setLinks(1);
        if (canDisplay) { debug("REL out:" + relation.asOutputString()); }
        output.append(relation.asOutputString()).append("\n");
    }

    // Return true if relation crosses a single or double link
    static boolean isCrossed(Relation rel) {
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
    }

    // Increment links of specified node, all its neighbors
    // and connections to the specified value
    static void incrementLinksTo(int increment, Node node, boolean canDisplay) {
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
            if (canDisplay) { debug("A out:" + relation.asOutputString()); }
            output.append(relation.asOutputString()).append("\n");
            relation.setLinks(increment);
        }
    }

    // Return first incomplete node that match aimed links and neighbors
    // and has at least one nonlinked relation - or null if not found such
    static Node getMatchingNode(int aimed, int neighbors) {
        for (Node node : nodes) {
            if (!node.isComplete() && node.aimedLinks() == aimed &&
                    node.neighbors() == neighbors && hasUnlinkedRelationOf(node)) {
                return node;
            }
        }
        return null;
    }

    static boolean hasIncompleteNodes() { return !getIncompleteNodes().isEmpty(); }

    // Return incomplete nodes: where number of links less than aimed
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

    // Return true if node has at least one non-linked relation
    static boolean hasUnlinkedRelationOf(Node node) {
        boolean result = false;
        for (Relation relation : relations) {
            if (relation.hasNode(node) && relation.isUnlinked()) {
                result = true;
                break;
            }
        }
        return result;
    }

    // Return all relations of a node - or empty list of no such
    static ArrayList<Relation> getRelationsOf(Node node) {
        ArrayList<Relation> result = new ArrayList<>();
        for (Relation relation : relations) {
            if (relation.hasNode(node)) { result.add(relation); }
        }
        return result;
    }

    // Return incomplete, unlinked relations of a node - or empty list of no such
    static ArrayList<Relation> getIncompleteUnlinkedRelationsOf(Node node) {
        ArrayList<Relation> result = new ArrayList<>();
        for (Relation relation : relations) {
            if (relation.hasNode(node) && !relation.isComplete() &&
              relation.isUnlinked()) {
                result.add(relation);
            }
        }
        return result;
    }

    // Return incomplete non-crossed relations
    // of node - or empty list if none found
    static ArrayList<Relation> getIncompleteNonCrossedRelationsOf(Node node) {
        ArrayList<Relation> result = new ArrayList<>();
        for (Relation relation : relations) {
            if (relation.hasNode(node) && !relation.isComplete() &&
              !isCrossed(relation)) {
                result.add(relation);
            }
        }
        return result;
    }

    // Return incomplete non-crossing relations of node that not found
    // in checked - or empty list if none found
    static ArrayList<Relation> getIncompleteNonCheckedNonCrossingRelationsOf(Node node, ArrayList<Relation> checked) {
        ArrayList<Relation> result = new ArrayList<>();
        for (Relation relation : relations) {
            if (relation.hasNode(node) && !relation.isComplete() &&
              !isCrossed(relation) && !checked.contains(relation)) {
                result.add(relation);
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

    // Display a generic arraylist or "EMPTY LIST!" if list empty
    static <T> void debug(String str, ArrayList<T> list) {
        if (!list.isEmpty()) {
            System.err.println(str);
            for (T element : list) { debug(element.toString()); }
        } else {
            str += "EMPTY LIST!";
            System.err.println(str);
        }
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

} // class Player ####################################################################################################

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
    public boolean isUnlinked()          { return links == 0; }
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
    public int      getDistance() { 
        return isVertical() ? Math.abs(nodeA.getY() - nodeB.getY()) :
                              Math.abs(nodeA.getX() - nodeB.getX());
    }
    public boolean  hasNode(Node node) {
        return node.equals(nodeA) || node.equals(nodeB);
    }
    public boolean  isComplete()   { return nodeA.isComplete() ||
                                           nodeB.isComplete() || links == 2; }
    public boolean  isVertical()   { return nodeA.getX() == nodeB.getX(); }
    public boolean  isCrossable()  { return crossable; }
    public boolean  isUnlinked()   { return links == 0; }

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
        return "relation " + nodeA + " " + (links == 0 ? "x" :  // [] represents number of links: 0|1|2
            links == 1 ? "-" : "=") + " " + nodeB + " | dist:" + getDistance();
    }

    private Node nodeA;                                         // Nodes of relation
    private Node nodeB;
    private int links;                                          // Actual links between nodes
    private boolean crossable;                                  // True if crossing possible for relation
} // class Relation