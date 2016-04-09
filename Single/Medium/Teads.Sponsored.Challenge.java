// Teads Sponsored Challenge
import java.util.*;

class Solution {
    static final boolean DEBUG = true;
    
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();                               // Number of adjacency relations
        disp(n + " adjacency relations");
        List<Node<Integer>> nodes = new ArrayList<>();      // List of created nodes
        List<Integer> ids = new ArrayList<>();              // List of node ids
        List<Integer> steps = new ArrayList<>();            // Steps needed to propagate the whole ad starting at ids
        // Store adjacenty relations in nodes, also node ids
        for (int i = 0; i < n; i++) {
            int xi = in.nextInt();                          // ID of a person which is adjacent to yi
            int yi = in.nextInt();                          // ID of a person which is adjacent to xi
            debug(xi + " - " + yi);
            if (!ids.contains(xi)) { ids.add(xi); }
            if (!ids.contains(yi)) { ids.add(yi); }
            Node<Integer> upper = getNode(xi, nodes);       // Get upper node form nodes array
            if (upper == null) {                            // Create and store new upper node if not found among nodes
                upper = new Node<>(null, xi);
                nodes.add(upper);
            } else {
                //debug("upper" + upper + " found in nodes - its children might be re-levelled");
            }
            Node<Integer> lower = getNode(yi, nodes);       // Get lower node form nodes array
            if (lower == null) {                            // Create and store new lower node if not found among nodes
                lower = new Node<>(upper, yi);
                lower.setLevel(upper.getLevel() + 1);
                nodes.add(lower);
            } else {                                        // Found lower node among nodes
                lower.addParent(upper);                     // Add upper node as parent to it (lower has no parent)
                if (lower.getLevel() != (upper.getLevel() + 1)) {
                    debug("lowernode " + lower.getId() + " level: " + lower.getLevel() + " -> " + (upper.getLevel() + 1));
                    lower.setLevel(upper.getLevel() + 1);       // Set lower's level to its parent + 1
                    debug("lower" + lower + " - its children must be re-levelled");
                    reLevelChildren(lower, nodes);                    
                }
            }
            upper.addChild(lower.getId());                  // Add lower node as child to upper node
        }
        //Collections.sort(ids);
        /*
        // Display ids
        debug("\nnode ids sorted:");
        for (Integer id : ids) {
            debug(id.toString());
        }*/
        // Display nodes
        debug("\nnodes:");
        for (Node<Integer> node : nodes) {
            debug(node.toString());
        }

        debug("\ndetermining steps for all ids...");
        // Determine the steps needed spreading from id and store these in steps array
        for (Integer id : ids) {
            Node<Integer> current = getNode(id, nodes);
            if (current.hasChildren()) {                    // Speedup: don't determine steps for nodes without children
                markNeighbors(current, nodes);
                steps.add(getSteps(id, nodes));
                // Reset mark flags of all nodes
                for (Node<Integer> node : nodes) {
                    node.clearMark();
                }
            }
        }
        Collections.sort(steps);
        // Display steps
        debug("\ndetermined steps:");
        for (Integer stp : steps) {
            debug(stp.toString());
        }
        disp("\noutput:");
        System.out.println(steps.get(0));                   //  Minimal amount of steps required to propagate the ad
    }// main()

    // Re-levelling all child of current node according its level 
    static <T> void reLevelChildren(Node<T> node, List<Node<T>> nodes) {
        List<Node<T>> children = new ArrayList<>();
        children = node.getChildren();
        for (Node<T> child : children) {
            reLevelNode(child, node.getLevel() + 1);
        }
    }// reLevelChildren()

    // Re-level a node to given level plus its children recursively
    static <T> void reLevelNode(Node<T> node, int level) {
        debug("re-level node " + node.getId() + " to level " + level);
        node.setLevel(level);
        
    }// reLevelNode()

    // Determine steps needed to propagate the whole ad from given start
    // node id via collecting marked nodes and also mark their neighbors
    static <T> Integer getSteps(T id, List<Node<T>> nodes) {
        List<Node<T>> markedNodes = new ArrayList<>();
        int step = 1;          
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
        } while (!isAllNodesMarked(nodes));
        return step;
    }// getSteps()

    // Mark node and all its neighbors (parent and children)
    // Warning: existence of node is NOT checked!
    static <T> void markNeighbors(Node<T> node, List<Node<T>> nodes) {
        //debug("markNeighbors called for " + node);
        node.mark();                                        // Mark the node
        //debug("node " + node.getId() + " marked");
        if (node.hasChildren()) {                           // Mark node's children
            //debug("node has children");
            List<Node<T>> children = node.getChildren();
            for (Node<T> c : children) {
                Node<T> child = getNode(c.getId(), nodes);
                child.mark();
                //debug("child node " + child.getId() + " marked:" + child.isMarked());
            }
        }
        if (node.getParent() != null) {                     // Mark node's parent
            node.getParent().mark();
            //debug("node's parent is " + node.getParent() + " marked:" + node.getParent().isMarked());
        } else {
            //debug("node has no parent");
        }
    }// markNeighbors()

    // Return true if all nodes are marked
    static <T> boolean isAllNodesMarked(List<Node<T>> nodes) {
        boolean allMarked = true;
        for (Node<T> node : nodes) {
            if (!node.isMarked()) {
                allMarked = false;
            }
        }
        return allMarked;
    }// isAllNodesMarked()

    // Rerurn node if found in nodes array, otherwise null
    static <T> Node<T> getNode(T id, List<Node<T>> nodes) {
        for (Node<T> node : nodes) {
            if (node.getId().equals(id)) {
                return node;
            }
        }
        return null;
    }// getNode()

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
    }// Constructor
    public T        getId() { return id; }                  // Getters
    public int      getLevel() { return level; }
    public Node<T>  getParent() { return parent; }
    public List<Node<T>> getChildren() { return children; }
    // Return true if node has at least one child
    public boolean  hasChildren() { return children != null; }
    /*// Return child node if found among children, otherwise null
    public Node<T>  getChild(T id) {           
        Node<T> ret = null;
        if (children != null && hasChild(id)) {
            for (Node<T> child : children) {
                if (child.getId().equals(id)) {
                    ret = child;
                }
            }
        }
        return ret;
    }// getChild()*/
    // Return true if child with id is among children    
    public boolean  hasChild(T id) {
        boolean ret = false;
        if (children != null) {
            for (Node<T> child : children) {
                if (child.getId().equals(id)) {
                    ret = true;
                }
            }        
        }
        return ret;
    }// hasChild()
    // Add child with id and return child node, if
    // node wasn't among children, otherwise return null
    public Node<T>  addChild(T id) {
        Node<T> child = null;
        if (children == null) {
            children = new ArrayList<>();
        }
        if (!hasChild(id)) {
            child = new Node<T>(parent, id);
            children.add(child);
        }
        return child;
    }// addChild()
    // Add parent node if parent was null, otherwise leave parent intact
    public void     addParent(Node<T> p) {
        if (parent == null) { parent = p; }
    }// addParent()
    public void     setLevel(int lev) { level = lev; }
    // Set and remove marked flag
    public void     mark() { marked = true; }
    public void     clearMark() { marked = false; }
    // Return true if marked flag is set
    public boolean  isMarked() { return marked; }
    // Return string as 'node id (parent:id, children:ids)'
    // or as 'node id (children:ids  parent:id)'
    @Override
    public String   toString() {
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
        //return "node " + id + " (parent:" + p + ", children:" + buf.toString() + ")";
        return "node " + id + " (children:" + buf.toString() + "  parent:" + p + ") | level:" + level;
    }// toString()

    private T id = null;                                    // id
    private int level = 0;                                  // level (root = 0)
    private boolean marked = false;                         // marked flag
    private Node<T> parent = null;                          // parent node
    private List<Node<T>> children = null;                  // children nodes
}// class Node<>