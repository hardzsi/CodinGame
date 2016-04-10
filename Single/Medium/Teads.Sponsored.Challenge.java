// Teads Sponsored Challenge
import java.util.*;

class Solution {
    static final boolean DEBUG = false;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();                               // Number of adjacency relations
        disp(n + " adjacency relations");
        int digLevel = 0;                                   // Step amount determined only for nodes with a lower level
        List<Node<Integer>> nodes = new ArrayList<>();      // List of created nodes
        List<Integer>       steps = new ArrayList<>();      // Steps needed to propagate the whole ad starting at a node
        Map<Integer, Node<Integer>>
                            nodesMap = new HashMap<>();     // Store nodes in a map with id as key for quicker access
        // Build graph storing adjacency relations in nodes
        for (int i = 0; i < n; i++) {
            int xi = in.nextInt();                          // ID of a person which is adjacent to yi
            int yi = in.nextInt();                          // ID of a person which is adjacent to xi
            //debug(xi + " - " + yi);
            Node<Integer> upper = nodesMap.get(xi);         // Get upper node from nodes map
            if (upper == null) {                            // Create and store new upper node if not found among nodes
                upper = new Node<>(xi);
                nodes.add(upper);
                nodesMap.put(xi, upper);
            }
            Node<Integer> lower = nodesMap.get(yi);         // Get lower node from nodes map
            if (lower == null) {                            // Create and store new lower node if not found among nodes
                lower = new Node<>(yi);
                lower.addParent(upper);                     // Add upper node as parent to lower node
                lower.setLevel(upper.getLevel() + 1);
                nodes.add(lower);
                nodesMap.put(yi, lower);
            } else {                                        // Found lower node among nodes
                lower.addParent(upper);                     // Add upper node as parent to lower node
                if (lower.getLevel() != (upper.getLevel() + 1)) {
                    reLevelNode(lower, upper, nodesMap, upper.getLevel() + 1); // Re-level node's children & other parents
                }
            }
            upper.addChild(lower);                          // Add lower node as child to upper node
        }
        // Set how deep we will dig as trying to find lowest spread count (steps)
        digLevel = n > 10000 ? Math.round(0.155f * numLevels(nodes)) : Math.round(0.5f * numLevels(nodes));

        disp("\n" + numLevels(nodes) + " levels, digLevel:" + digLevel + ", " + nodes.size() + " nodes in nodes array");
        //debug("\nNodes:"); for (Node<Integer> node : nodes) { debug(node.toString()); }

        // Determine then store steps needed spreading from current node
        for (Node<Integer> current : nodes) {
            if (current.hasChildren() &&                    // Speedup: don't determine steps for nodes without children
                current.getLevel() <= digLevel) {           // and determine steps only for nodes standing above digLevel
                markNeighbors(current, nodesMap);
                steps.add(getSteps(current.getId(), nodes, nodesMap));
                // Reset mark flags of all nodes
                for (Node<Integer> node : nodes) { node.clearMark(); }
            }
        }
        Collections.sort(steps);                            // Sort steps to get the minimum step amount (the first one)
        disp("\noutput:");
        System.out.println(steps.get(0));                   //  Minimal amount of steps required to propagate the ad
    }// main()

    // Set node to level and re-level all its parents
    // (except blocked one) and children recursively
    static <T> void reLevelNode(Node<T> node, Node<T> blocked, Map<T, Node<T>> nodesMap, int level) {
        node.setLevel(level);
        Set<Node<T>> parents = node.getParents();           // Set is empty if node has NO parents
        for (Node<T> parent : parents) {
            if (!parent.equals(blocked) && (parent.getLevel() != node.getLevel() - 1)) {
                reLevelNode(nodesMap.get(parent.getId()), blocked, nodesMap, node.getLevel() - 1);
            }
        }
        Set<Node<T>> children = node.getChildren();         // Set is empty if node has NO children
        for (Node<T> child : children) {
            reLevelNode(nodesMap.get(child.getId()), blocked, nodesMap, node.getLevel() + 1);
        }
    }// reLevelNode()

    // Determine steps needed to propagate the whole ad from given start node
    static <T> Integer getSteps(T id, List<Node<T>> nodes, Map<T, Node<T>> nodesMap) {
        Set<Node<T>> markedNodes = new HashSet<>();         // Collect marked nodes per iteration
        int step = 1;
        boolean allMarked;
        do {
            // Collect marked nodes
            for (Node<T> node : nodes) {
                if (node.isMarked()) { markedNodes.add(node); }
            }
            // Mark neighbors of marked nodes (Note: cannot be concatenated with for cycle above)
            for (Node<T> marked : markedNodes) {
                markNeighbors(marked, nodesMap);
            }
            markedNodes.clear();
            step++;
            // Check if all nodes are marked
            allMarked = true;
            for (Node<T> node : nodes) {
                if (!node.isMarked()) { allMarked = false; }
            }
        } while (!allMarked);
        return step;
    }// getSteps()

    // Mark node and all its neighbors (parents and children)
    // Note: node come from nodes array so it should exist
    static <T> void markNeighbors(Node<T> node, Map<T, Node<T>> nodesMap) {
        node.mark();                                        // Mark the node
        Set<Node<T>> neighbors = node.getChildren();        // Put all neighbors in a set
        neighbors.addAll(node.getParents());
        for (Node<T> neighbor : neighbors) {                // Mark node's parents and children
            nodesMap.get(neighbor.getId()).mark();
        }
    }// markNeighbors()

    // Return number of levels (numbering starts from 0)
    static <T> int numLevels(List<Node<T>> nodes) {
        int lowest = 0;
        for (Node<T> node : nodes) {
            lowest = Math.max(lowest, node.getLevel());     // lowest = lowest>node.getLevel()? lowest:node.getLevel()
        }
        return lowest + 1;
    }// numLevels()

    // Display string to err either if DEBUG true or always
    static void debug(String s) { if (DEBUG) { System.err.println(s); } }
    static void disp(String s) { System.err.println(s); }
}// class Solution


// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< Node class >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Generic node class with id, marked flag and any number of parents and children
class Node<T> {
    public Node(T id) { this.id = id; }                     // Constructor

    // parents
    public Set<Node<T>> getParents() { return parents; }
    public void         addParent(Node<T> parent) {         // Add parent node if it wasn't among parents (as it is set)
        parents.add(parent);
    }

    // children
    public Set<Node<T>> getChildren() { return children; }
    public boolean      hasChildren() { return children.size() > 0; }   // True if node has at least one child
    public void         addChild(Node<T> child) {           // Add child node if it wasn't among children
        children.add(child);
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
    public String   toString() {                            // Return "node id (children:ids  parents:ids) | level:level"
        StringBuffer pBuf = new StringBuffer("none");
        if (parents.size() > 0) {
            pBuf.setLength(0);
            for (Node<T> parent : parents) {
                pBuf.append(parent.getId()).append(",");
            }
            pBuf.deleteCharAt(pBuf.length() - 1);
        }
        StringBuffer cBuf = new StringBuffer("none");
        if (children.size() > 0) {
            cBuf.setLength(0);
            for (Node<T> child : children) {
                cBuf.append(child.getId()).append(",");
            }
            cBuf.deleteCharAt(cBuf.length() - 1);
        }
        return "node " + id + " (children:" + cBuf.toString() + "  parents:" + pBuf.toString() + ") | level:" + level;
    }// toString()

    private T       id = null;                              // id
    private int     level = 0;                              // level (root = 0)
    private boolean marked = false;                         // marked flag
    private Set<Node<T>> parents = new HashSet<>();         // parent nodes
    private Set<Node<T>> children = new HashSet<>();        // children nodes
}// class Node<>