// Teads Sponsored Challenge
import java.util.*;

class Solution {
    static final boolean DEBUG = true;
    
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();                       // Number of adjacency relations
        debug(n + " adjacency relations:");
        //Integer[][] adjacents = new Integer[n][2];  // Relations to be sorted
        //HashSet<Integer> IDs = new HashSet<>();     // Set of IDs (enable quick check if an ID exists or not)
        ArrayList<Node<Integer>> nodes =
            new ArrayList<>();                      // List of created nodes
        // Store adjacenty relations in nodes
        for (int i = 0; i < n; i++) {
            int xi = in.nextInt();                  // ID of a person which is adjacent to yi
            int yi = in.nextInt();                  // ID of a person which is adjacent to xi
            //adjacents[i][0] = xi; adjacents[i][1] = yi;
            //IDs.add(xi); IDs.add(yi);               // Adds element to set only if it is not already present
            debug(xi + " - " + yi);
            
            Node<Integer> upper = getNode(xi, nodes);
            if (upper == null) {
                upper = new Node<>(null, xi);
                nodes.add(upper);
            }
            Node<Integer> lower = new Node<>(upper, yi);
            nodes.add(lower);
            upper.addChild(lower.getId());
            //debug( xi + " - " + yi + " :: " + upper + " | " + lower);
        }
        // Display nodes
        for (Node<Integer> node : nodes) {
            debug("" + node);
        }

        /*{
        // Sort adjacents array as per first values or second values if first values are the same
        Arrays.sort(adjacents, new Comparator<Integer[]>() {
            @Override
            public int compare(Integer[] a, Integer[] b) {
                return a[0] == b[0] ? (a[1] == b[1] ? 0 : (a[1] < b[1] ? -1 : 1)) : (a[0] < b[0] ? -1 : 1);
            }
        });
        debug("\nAdj. relations sorted:");
        for (Integer[] adj : adjacents) {
            debug(adj[0] + " " + adj[1]);
        }
        }*/
        /*debug("\nNode ids stored in set:");
        for (Integer id : IDs) {
            debug("" + id);
        }*/
        
        int startNode = 0;
        markNeighbors(startNode, nodes);
        int round = 1;
        ArrayList<Node<Integer>> markedNodes;
        while (!isAllNodesMarked(nodes)) {
            markedNodes = getMarkedNodes(nodes);
            for (Node<Integer> markedNode : markedNodes) {
                debug("marked node:" + markedNode);
                markNeighbors(markedNode.getId(), nodes);
            }
            markedNodes.clear();
            round++;
            debug("rounds:" + round);
        }
        debug("rounds:" + round);
        
        System.err.println();
        System.out.println("2");                    // Minimal amount of steps required to completely propagate the ad
    }// main()

    // Mark all neighbors (parent and children) of node with id
    static <T> void markNeighbors(T id, ArrayList<Node<T>> nodes) {
        Node<T> node = getNode(id, nodes);
        node.mark();                                // Mark node
        debug("node " + node.getId() + " marked");
        if (node.hasChildren()) {                   // Mark node's children
            debug("node has children");
            ArrayList<Node<T>> children = node.getChildren();
            for (Node<T> child : children) {
                child.mark();
                debug("child node " + child.getId() + " marked:" + child.isMarked());
            }
        }
        if (node.getParent() != null) {             // Mark node's parent
            node.getParent().mark();
            debug("node's parent is " + node.getParent() + "; it was marked");
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
        for (Node<T> node : nodes) { debug("" + node + "; node is marked? " + node.isMarked()); }
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
}// class Solution

// Generic node class with id and flag to be marked
class Node<T> {
    public Node(Node<T> parent, T id) {
        this.parent = parent;
        this.id = id;
    }// Constructor
    // Getters
    public T         getId() { return id; }
    public Node<T>   getParent() { return parent; }
    public ArrayList<Node<T>> getChildren() { return children; }
    // Return true if node has at least one child
    public boolean   hasChildren() { return children != null; }
    // Return child node if found among children, otherwise null
    public Node<T>   getChild(T id) {           
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
    public boolean   hasChild(T id) {
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
    public Node<T>   addChild(T id) {
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
    public void      mark() { marked = true; }
    public void      clearMark() { marked = false; }
    // Return true if marked flag is set
    public boolean   isMarked() { return marked; }
    // Return string as 'node id (parent:id, children:ids)' or 'node id (children:ids  parent:id)'
    @Override
    public String    toString() {
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

    private T id = null;                            // Node id
    private boolean marked = false;                 // Marked flag
    private Node<T> parent = null;                  // Parent node
    private ArrayList<Node<T>> children = null;     // Children nodes
}// class Node<>