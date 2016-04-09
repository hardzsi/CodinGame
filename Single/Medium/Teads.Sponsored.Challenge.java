// Teads Sponsored Challenge
import java.util.*;

class Solution {
    static final boolean DEBUG = false;
    
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();                               // Number of adjacency relations
        disp(n + " adjacency relations:");
        ArrayList<Node<Integer>> nodes = new ArrayList<>(); // List of created nodes
        // Store adjacenty relations in nodes
        for (int i = 0; i < n; i++) {
            int xi = in.nextInt();                          // ID of a person which is adjacent to yi
            int yi = in.nextInt();                          // ID of a person which is adjacent to xi
            disp(xi + " - " + yi);
            Node<Integer> upper = getNode(xi, nodes);       // Upper node always found among nodes except first time
            if (upper == null) {
                upper = new Node<>(null, xi);
                nodes.add(upper);
            }
            Node<Integer> lower = new Node<>(upper, yi);    // Lower node always a new one
            nodes.add(lower);
            upper.addChild(lower.getId());
            //debug( xi + " - " + yi + " :: " + upper + " | " + lower);
        }
        // Display nodes
        for (Node<Integer> node : nodes) {
            disp("" + node);
        }
        disp("");
        
        int startNode = 5;
        disp("start node:" + startNode);
        markNeighbors(getNode(startNode, nodes), nodes);
        int round = 1;
        ArrayList<Node<Integer>> markedNodes;
        do {
            debug("call getMarkedNodes:");
            markedNodes = getMarkedNodes(nodes);
            for (Node<Integer> marked : markedNodes) {
                debug("marked node:" + marked);
                markNeighbors(marked, nodes);
            }
            markedNodes.clear();
            round++;
            debug("round:" + round);
        } while (!isAllNodesMarked(nodes));
        disp("round:" + round);
        
        disp("");
        System.out.println("2");                            //  Minimal amount of steps required
    }// main()                                                  to completely propagate the ad

    // Mark node and all its neighbors (parent and children)
    // Warning: existence of node is NOT checked!
    static <T> void markNeighbors(Node<T> node, ArrayList<Node<T>> nodes) {
        debug("markNeighbors called for " + node);
        node.mark();                                        // Mark the node
        debug("node " + node.getId() + " marked");
        if (node.hasChildren()) {                           // Mark node's children
            debug("node has children");
            ArrayList<Node<T>> children = node.getChildren();
            for (Node<T> c : children) {
                Node<T> child = getNode(c.getId(), nodes);
                child.mark();
                debug("child node " + child.getId() + " marked:" + child.isMarked());
            }
        }
        if (node.getParent() != null) {                     // Mark node's parent
            node.getParent().mark();
            debug("node's parent is " + node.getParent() + " marked:" + node.getParent().isMarked());
        } else {
            debug("node has no parent");
        }
    }// markNeighbors()

    // Reset mark flags of all nodes
    static <T> void clearMarks(ArrayList<Node<T>> nodes) {
        for (Node<T> node : nodes) {
            node.clearMark();
        }
    } // clearMarks()

    // Return true if all nodes are marked
    static <T> boolean isAllNodesMarked(ArrayList<Node<T>> nodes) {
        boolean allMarked = true;
        for (Node<T> node : nodes) {
            if (!node.isMarked()) {
                allMarked = false;
            }
        }
        return allMarked;
    }// isAllNodesMarked()

    // Rerurn node if found in nodes array, otherwise null
    static <T> Node<T> getNode(T id, ArrayList<Node<T>> nodes) {
        for (Node<T> node : nodes) {
            if (node.getId().equals(id)) {
                return node;
            }
        }
        return null;
    }// getNode()

    // Return list of marked nodes - an empty list if no nodes marked
    static <T> ArrayList<Node<T>> getMarkedNodes(ArrayList<Node<T>> nodes) {
        for (Node<T> node : nodes) { debug("" + node + " marked:" + node.isMarked()); }
        ArrayList<Node<T>> markedNodes = new ArrayList<>();
        for (Node<T> node : nodes) {
            if (node.isMarked()) {
                markedNodes.add(node);
                debug("node " + node.getId() + " added to marked nodes");
            }
        }
        return markedNodes;
    }// getMarkedNodes()

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

// Generic node class with id and flag to be marked
class Node<T> {
    public Node(Node<T> parent, T id) {                     // Constructor
        this.parent = parent;
        this.id = id;
    }// Constructor
    public T        getId() { return id; }        // Getters
    public Node<T>  getParent() { return parent; }
    public ArrayList<Node<T>> getChildren() { return children; }
    // Return true if node has at least one child
    public boolean  hasChildren() { return children != null; }
    // Return child node if found among children, otherwise null
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
    }// getChild()
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
    // Add child with id and return child node, if node wasn't among children, otherwise return null
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
    // Set and remove marked flag
    public void     mark() { marked = true; }
    public void     clearMark() { marked = false; }
    // Return true if marked flag is set
    public boolean  isMarked() { return marked; }
    // Return string as 'node id (parent:id, children:ids)' or 'node id (children:ids  parent:id)'
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
        return "node " + id + " (children:" + buf.toString() + "  parent:" + p + ")";
    }// toString()

    private T id = null;                                    // Node id
    private boolean marked = false;                         // Marked flag
    private Node<T> parent = null;                          // Parent node
    private ArrayList<Node<T>> children = null;             // Children nodes
}// class Node<>