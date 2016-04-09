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
        List<Integer>       steps = new ArrayList<>();      // Steps needed to propagate the whole ad starting at a node
        Map<Integer, Node<Integer>> nodesMap = new HashMap<>(); // Store nodes in map for quicker access

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
                    reLevelNode(lower, upper, nodesMap, upper.getLevel() + 1); // Re-level node's all children and other parents
                }
            }
            upper.addChild(lower);                          // Add lower node as child to upper node
        }
        // Set how deep we will dig as trying to find lowest spread count (steps)
        digLevel = Math.round(DIG_PERCENT * numLevels(nodes));
        //digLevel = n < 500 ? numLevels(nodes) : Math.round(DIG_PERCENT * numLevels(nodes));      
        // Display how many nodes have given amount of parents 
        /*int[] count = new int[10];
        for (Node<Integer> node : nodes) {
            int p = node.getParents().size();
            count[p]++;
        }
        for (int i = 0; i < count.length; ++i) {
            disp(i + " parents: " + count[i]);
        }*/
        // Display nodes
        /*debug("\n" + nodes.size() + " nodes:");
        for (Node<Integer> node : nodes) {
            disp(node.toString());
        }*/
        disp("\n" + numLevels(nodes) + " levels, digLevel:" + digLevel + ", " + nodes.size() + " nodes in nodes array");

        debug("\ndetermining steps for all ids within digLevel...");
        // Determine then store steps needed spreading from current node
        for (Node<Integer> current : nodes) {
            debug("check: " + current);
            if (current.hasChildren() &&                    // Speedup: don't determine steps for nodes without children
                current.getLevel() <= digLevel) {           // and determine steps only for nodes standing below digLevel
                //debug("starting markNeighbors");
                markNeighbors(current, nodesMap);
                //debug("markNeighbors finished, adding " + current.getId() + " to steps:");
                steps.add(getSteps(current.getId(), nodes, nodesMap));
                debug("resetting mark flags");
                // Reset mark flags of all nodes
                for (Node<Integer> node : nodes) {
                    node.clearMark();
                }
            }
        }
        Collections.sort(steps);                            // Sorting steps to get the minimum step amount (first one)
        // Display steps
        /*debug("\ndetermined steps:");
        for (Integer stp : steps) {
            debug(stp.toString());
        }*/
        disp("\noutput:");
        System.out.println(steps.get(0));                   //  Minimal amount of steps required to propagate the ad
    }// main()

    // Re-level a node to the level given as argument, as well as all its children recursively
    static <T> void reLevelNode(Node<T> node, Node<T> blocked, Map<T, Node<T>> nodesMap, int level) {
        //disp("re-levelling " + node + "  to level " + level + " , upper is:" + upper.getId());
        node.setLevel(level);
        if (node.hasParents()) {
            Set<Node<T>> parents = node.getParents();
            debug("node " + node.getId() + " has " + parents.size() + " parents");
            for (Node<T> parent : parents) {
                if (!parent.equals(blocked)) {
                    debug("parent is:" + parent.getId() + ", blocked is:" + blocked.getId());
                    //reLevelNode(getNode(parent.getId(), nodes), blocked, nodes, node.getLevel() - 1);                
                }
            }
        }
        if (node.hasChildren()) {
            Set<Node<T>> children = node.getChildren();
            for (Node<T> child : children) {
                //disp(" -> ");
                //reLevelNode(getNode(child.getId(), nodes), blocked, nodes, node.getLevel() + 1);
                reLevelNode(nodesMap.get(child.getId()), blocked, nodesMap, node.getLevel() + 1);
            }
        }
    }// reLevelNode()

    // Determine steps needed to propagate the whole ad from given start node
    static <T> Integer getSteps(T id, List<Node<T>> nodes, Map<T, Node<T>> nodesMap) {
        debug("starting getSteps with node " + id);
        List<Node<T>> markedNodes = new ArrayList<>();      // Collect marked nodes per iteration - Set<> would be faster?
        int step = 1;
        boolean allMarked;
        do {
            // Collect marked nodes
            for (Node<T> node : nodes) {
                if (node.isMarked()) { markedNodes.add(node); }
            }
            // Mark neighbors of marked nodes (cannot be concatenated with for() cycle above)
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
            debug("step " + step);
        } while (!allMarked);
        return step;
    }// getSteps()

    // Mark node and all its neighbors (parents and children)
    // Warning: existence of node NOT checked, but come from nodes array so it should exist
    static <T> void markNeighbors(Node<T> node, Map<T, Node<T>> nodesMap) {
        node.mark();                                        // Mark the node
        Set<Node<T>> neighbors = node.getChildren();        // Put all neighbors in a set
        neighbors.addAll(node.getParents());
        for (Node<T> neighbor : neighbors) {                // Mark node's parents and children
            nodesMap.get(neighbor.getId()).mark();
            //getNode(neighbor.getId(), nodes).mark();
        }
    }// markNeighbors()

    // Return number of levels (numbering starts from 0)
    static <T> int numLevels(List<Node<T>> nodes) {
        int lowest = 0;
        for (Node<T> node : nodes) {
            lowest = Math.max(lowest, node.getLevel());
        }
        return lowest + 1;
    }// numLevels()

    // Display string to err either if DEBUG true or always
    static void debug(String s) { if (DEBUG) { System.err.println(s); } }
    static void disp(String s) { System.err.println(s); }
}// class Solution


// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< Node class >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Generic node class with any number of parents and children, also id and marked flag
class Node<T> {
    public Node(T id) { this.id = id; }                     // Constructor

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