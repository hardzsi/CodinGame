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

        // Store adj.relations, levels and ids of nodes
        for (int i = 0; i < n; i++) {
            int xi = in.nextInt();                          // ID of a person which is adjacent to yi
            int yi = in.nextInt();                          // ID of a person which is adjacent to xi
            debug(xi + " - " + yi);
            ids.add(xi); ids.add(yi);
            Node<Integer> upper = getNode(xi, nodes);       // Get upper node from nodes array
            if (upper == null) {                            // Create and store new upper node if not found among nodes
                upper = new Node<>(xi);
                nodes.add(upper);
            }
            Node<Integer> lower = getNode(yi, nodes);       // Get lower node from nodes array
            if (lower == null) {                            // Create and store new lower node if not found among nodes
                lower = new Node<>(yi);
                lower.addParent(upper);                     // Add upper node as parent to lower node                
                lower.setLevel(upper.getLevel() + 1);
                nodes.add(lower);
            } else {                                        // Found lower node among nodes
                lower.addParent(upper);                     // Add upper node as parent to lower node
                if (lower.getLevel() != (upper.getLevel() + 1)) {
                    // Set lower's level to its parent+1 and
                    // re-level all its children accordingly
                    reLevelNode(lower, upper, nodes, upper.getLevel() + 1); 
                }
            }
            //lower.addParent(upper);                         // Add upper node as parent to lower node
            upper.addChild(lower);                          // Add lower node as child to upper node
        }
        digLevel = Math.round(DIG_PERCENT * numLevels(nodes));
        //digLevel = n < 500 ? numLevels(nodes) : Math.round(DIG_PERCENT * numLevels(nodes));      
        // Display nodes
        /*debug("\n" + nodes.size() + " nodes:");
        for (Node<Integer> node : nodes) {
            debug(node.toString());
        }*/
        disp("\n" + numLevels(nodes) + " levels, digLevel:" + digLevel + ", " + nodes.size() + " nodes in nodes array");

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
    static <T> void reLevelNode(Node<T> node, Node<T> upper, List<Node<T>> nodes, int level) {
        disp("re-levelling " + node + "  to level " + level + " , upper is:" + upper.getId());
        node.setLevel(level);
        /*if (node.hasParents()) {
            Set<Node<T>> parents = node.getParents();
            for (Node<T> parent : parents) {
                reLevelNode(getNode(parent.getId(), nodes), upper, nodes, node.getLevel() - 1);
            }
        }*/
        if (node.hasChildren()) {
            Set<Node<T>> children = node.getChildren();
            for (Node<T> child : children) {
                reLevelNode(getNode(child.getId(), nodes), upper, nodes, node.getLevel() + 1);
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
            Set<Node<T>> children = node.getChildren();
            for (Node<T> c : children) {
                Node<T> child = getNode(c.getId(), nodes);
                child.mark();
            }
        }
        if (node.hasParents()) {                           // Mark node's parents
            Set<Node<T>> parents = node.getParents();
            for (Node<T> p : parents) {
                Node<T> parent = getNode(p.getId(), nodes);
                parent.mark();
            }
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

    // Display string to err either if DEBUG true or always
    static void debug(String s) {
        if (DEBUG) { System.err.println(s); }
    }
    static void disp(String s) { System.err.println(s); }
}// class Solution


// <<<<<<<<<<<<<<<<< Node class >>>>>>>>>>>>>>>>>>>
// Generic node class with id and flag to be marked
class Node<T> {
    public Node(T id) {                                     // Constructor
        this.id = id;
    }

    // parents
    public Set<Node<T>> getParents() { return parents; }
    public boolean      hasParents() { return parents.size() > 0; }
    public void         addParent(Node<T> parent) {         // Add parent node if node wasn't among parents
        if (!parents.contains(parent)) {
            parents.add(parent);
        }
    }

    // children
    public Set<Node<T>> getChildren() { return children; }
    public boolean      hasChildren() { return children.size() > 0; }   // True if node has at least one child
    public void         addChild(Node<T> child) {           // Add child node if node wasn't among children
        if (!children.contains(child)) {
            children.add(child);
        }
    }

    // level
    public int      getLevel() { return level; }
    public void     setLevel(int lev) { level = lev; }

    // mark
    public void     mark() { marked = true; }               // Set marked flag
    public void     clearMark() { marked = false; }         // Remove marked flag
    public boolean  isMarked() { return marked; }           // Return true if marked flag is set

    // other
    public T        getId() { return id; }    
    @Override 
    public String   toString() {                            // Return 'node id (children:ids  parents:ids) | level:level'
        StringBuffer pBuf = new StringBuffer("none");
        if (parents.size() > 0) {
            pBuf.setLength(0);
            Object[] pArr = parents.toArray();
            for (int i = 0; i < pArr.length; ++i) {
                pBuf.append(((Node<T>)pArr[i]).getId()).append(i < parents.size() - 1? "," : "");
            }
        }
        StringBuffer cBuf = new StringBuffer("none");
        if (children.size() > 0) {
            cBuf.setLength(0);
            Object[] cArr = children.toArray();
            for (int i = 0; i < cArr.length; ++i) {
                cBuf.append(((Node<T>)cArr[i]).getId()).append(i < children.size() - 1? "," : "");
            }
        }
        return "node " + id + " (children:" + cBuf.toString() + "  parents:" + pBuf.toString() + ") | level:" + level;
    }// toString()

    private T       id = null;                              // id
    private int     level = 0;                              // level (root = 0)
    private boolean marked = false;                         // marked flag
    private Set<Node<T>> parents = new HashSet<>();         // parent nodes
    private Set<Node<T>> children = new HashSet<>();        // children nodes
}// class Node<>