// Teads Sponsored Challenge
import java.util.*;

class Solution {
    static final boolean DEBUG = false;
    static final float DIG_PERCENT = 0.41f;                 // Step amount will be determined only for those nodes
                                                            // that is above digLevel (DIG_PERCENT * lowest level)
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();                               // Number of adjacency relations
        disp(n + " adjacency relations");
        int digLevel = 0;                                   // Step amount determined only for nodes with a lower level
        List<Node<Integer>> nodes = new ArrayList<>();      // List of created nodes
        HashSet<Integer>      ids = new HashSet<>();        // Set of node ids
        List<Integer>       steps = new ArrayList<>();      // Steps needed to propagate the whole ad starting at ids
        //List<Integer>       check = new ArrayList<>();
        //check.add(835); check.add(942);check.add(520);check.add(110);
        // Store adj.relations, levels and ids of nodes
        for (int i = 0; i < n; i++) {
            int xi = in.nextInt();                          // ID of a person which is adjacent to yi
            int yi = in.nextInt();                          // ID of a person which is adjacent to xi
            //debug(xi + " - " + yi);
            /*if (check.contains(xi) || check.contains(yi)) {
                debug(xi + " - " + yi);
            }*/
            ids.add(xi); ids.add(yi);
            Node<Integer> upper = getNode(xi, nodes);       // Get upper node from nodes array
            if (upper == null) {                            // Create and store new upper node if not found among nodes
                upper = new Node<>(null, xi);
                nodes.add(upper);
            }
            Node<Integer> lower = getNode(yi, nodes);       // Get lower node from nodes array
            if (lower == null) {                            // Create and store new lower node if not found among nodes
                lower = new Node<>(upper, yi);
                lower.setLevel(upper.getLevel() + 1);
                nodes.add(lower);
            } else {                                        // Found lower node among nodes
                lower.addParent(upper);                     // Add upper node as parent to it (lower has no parent)
                if (lower.getLevel() != (upper.getLevel() + 1)) {
                    // Set lower's level to its parent+1 and
                    // re-level all its children accordingly
                    reLevelNode(lower, nodes, upper.getLevel() + 1); 
                }
            }
            upper.addChild(lower);                          // Add lower node as child to upper node
        }
        digLevel = Math.round(DIG_PERCENT * numLevels(nodes));
        //digLevel = n < 500 ? numLevels(nodes) : Math.round(DIG_PERCENT * numLevels(nodes));      
        /*// Display nodes
        debug("\n" + nodes.size() + " nodes:");
        for (Node<Integer> node : nodes) {
            debug(node.toString());
        }*/
        disp("\n" + numLevels(nodes) + " levels, digLevel:" + digLevel + ", " + nodes.size() + " nodes in nodes array");

        int nodeWOparent = 0;
        for (Node<Integer> node : nodes) {
            if (node.getParent() == null) {
                disp(node.toString());
                nodeWOparent++;
            }
        }
        disp("\nThere are " + nodeWOparent + " nodes without parent!");        

        debug("\ndetermining steps for all ids within digLevel...");
        // Determine the steps needed spreading from id and store these in steps array
        for (Integer id : ids) {
            Node<Integer> current = getNode(id, nodes);
            debug("check: " + current);
            if (current.hasChildren() &&                    // Speedup: don't determine steps for nodes without children
                current.getLevel() <= digLevel) {           // and determine steps only for nodes standing below digLevel
                //debug("starting markNeighbors");
                markNeighbors(current, nodes);
                //debug("markNeighbors finished, adding " + current.getId() + " to steps:");
                steps.add(getSteps(id, nodes));
                debug("resetting mark flags");
                // Reset mark flags of all nodes
                for (Node<Integer> node : nodes) {
                    node.clearMark();
                }
            }
        }
        Collections.sort(steps);                            // Sorting steps to get the minimum step amount (first one)
        /*// Display steps
        debug("\ndetermined steps:");
        for (Integer stp : steps) {
            debug(stp.toString());
        }*/
        disp("\noutput:");
        System.out.println(steps.get(0));                   //  Minimal amount of steps required to propagate the ad
    }// main()

    // Re-level a node to the level given as argument, as well as all its children recursively
    static <T> void reLevelNode(Node<T> node, List<Node<T>> nodes, int level) {
        node.setLevel(level);
        if (node.hasChildren()) {
            List<Node<T>> children = node.getChildren();
            for (Node<T> child : children) {
                reLevelNode(getNode(child.getId(), nodes), nodes, node.getLevel() + 1);
            }
        }
    }// reLevelNode()

    // Determine steps needed to propagate the whole ad from given start
    // node id via collecting marked nodes and also mark their neighbors
    static <T> Integer getSteps(T id, List<Node<T>> nodes) {
        debug("starting getSteps with node " + id);
        List<Node<T>> markedNodes = new ArrayList<>();
        int step = 1;
        boolean allMarked = true;
        do {
            // Collect marked nodes
            for (Node<T> node : nodes) {
                if (node.isMarked()) {
                    markedNodes.add(node);
                }
            }
            // Mark neighbors of marked nodes (cannot be concatenated with for() cycle above)
            for (Node<T> marked : markedNodes) {
                markNeighbors(marked, nodes);
            }
            markedNodes.clear();
            step++;
            // Check if all nodes are marked
            allMarked = true;
            for (Node<T> node : nodes) {
                if (!node.isMarked()) {
                    allMarked = false;
                }
            }
            debug("step " + step);
        } while (!allMarked);
        return step;
    }// getSteps()

    // Mark node and all its neighbors (parent and children)
    // Warning: existence of node NOT checked, but come from nodes array so it should exist
    static <T> void markNeighbors(Node<T> node, List<Node<T>> nodes) {
        node.mark();                                        // Mark the node
        if (node.hasChildren()) {                           // Mark node's children
            List<Node<T>> children = node.getChildren();
            for (Node<T> c : children) {
                Node<T> child = getNode(c.getId(), nodes);
                child.mark();
            }
        }
        if (node.getParent() != null) {                     // Mark node's parent
            node.getParent().mark();
        }
    }// markNeighbors()

    // Rerurn node if found in nodes array, otherwise null
    static <T> Node<T> getNode(T id, List<Node<T>> nodes) {
        for (Node<T> node : nodes) {
            if (node.getId().equals(id)) {
                return node;
            }
        }
        return null;
    }// getNode()

    // Return the number of levels (counting from 0)
    static <T> int numLevels(List<Node<T>> nodes) {
        int lowest = 0;
        for (Node<T> node : nodes) {
            lowest = Math.max(lowest, node.getLevel());
        }
        return lowest + 1;
    }// numLevels()

    // Display string to err if DEBUG true
    static void debug(String s) {
        if (DEBUG) {
            System.err.println(s);
        }
    }// debug()
    // Display string to err
    static void disp(String s) {
        System.err.println(s);
    }// disp()
}// class Solution


// <<<<<<<<<<<<<<<<< Node class >>>>>>>>>>>>>>>>>>>
// Generic node class with id and flag to be marked
class Node<T> {
    public Node(Node<T> parent, T id) {                     // Constructor
        this.parent = parent;
        this.id = id;
    }

    public T        getId() { return id; }
    public int      getLevel() { return level; }
    public Node<T>  getParent() { return parent; }
    public List<Node<T>> getChildren() { return children; }

    // Return true if node has at least one child
    public boolean  hasChildren() { return children != null; }

    // Add child node if node wasn't among children
    public void addChild(Node<T> child) {      
        if (children == null) {
            children = new ArrayList<>();
        }
        if (!children.contains(child)) {
            child = new Node<T>(parent, child.getId());
            children.add(child);
        }
    }// addChild()

    public void     addParent(Node<T> p) { 
        if (parent != null) {
            System.err.println("parent " + parent.getId() + " existed, when new parent " + p.getId() + " would be added");
        }
        parent = p;
    
    }    // Add parent node. Warning: not checked if parent existed
    public void     setLevel(int lev) { level = lev; }
    public void     mark() { marked = true; }               // Set marked flag
    public void     clearMark() { marked = false; }         // Remove marked flag
    public boolean  isMarked() { return marked; }           // Return true if marked flag is set
    @Override 
    public String   toString() {                            // Return 'node id (children:ids  parent:id) | level:level'
        String p = "none";
        if (parent != null) {
            p = parent.getId().toString();
        }
        StringBuffer buf = new StringBuffer("none");
        if (children != null) {
            buf.setLength(0);
            for (int i = 0; i < children.size(); ++i) {
                buf.append(children.get(i).getId()).append(i < children.size() - 1? "," : "");
            }
        }
        return "node " + id + " (children:" + buf.toString() + "  parent:" + p + ") | level:" + level;
    }// toString()

    private T       id = null;                              // id
    private int     level = 0;                              // level (root = 0)
    private boolean marked = false;                         // marked flag
    private Node<T> parent = null;                          // parent node
    private List<Node<T>> children = null;                  // children nodes
}// class Node<>