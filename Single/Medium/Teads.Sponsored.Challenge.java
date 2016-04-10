// Teads Sponsored Challenge (2015.06.15)
// Algorithm B: Build adjacency relations graph for all nodes. Remove all end nodes (nodes with only one parent or child)
// from nodes set iteratively until only one or two nodes left in set. Iterations (step) count is the result.
import java.util.*;

class Solution {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();                               // Number of adjacency relations
        System.err.println(n + " adjacency relations");
        Set<Node<Integer>> nodes = new HashSet<>();         // Set of created nodes (Note: removeAll() too slow in List)
        Map<Integer, Node<Integer>>
                            nodesMap = new HashMap<>();     // Store nodes in a map with id as key for quicker access
        // Build graph storing adjacency relations in nodes
        for (int i = 0; i < n; i++) {
            int xi = in.nextInt();                          // ID of a person which is adjacent to yi
            int yi = in.nextInt();                          // ID of a person which is adjacent to xi
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
                nodes.add(lower);
                nodesMap.put(yi, lower);
            } else {                                        // Found lower node among nodes
                lower.addParent(upper);                     // Add upper node as parent to lower node
            }
            upper.addChild(lower);                          // Add lower node as child to upper node
        }

        int step = 0;
        do {
            if (nodes.size() > 2) {
                Set<Node<Integer>> removals =               // Store nodes to be removed in one step. This is necessary, 
                    new HashSet<>();                        // as during the for cycle nodes cannot be removed from set
                // Mark end nodes
                for (Node<Integer> node : nodes) {
                    if (node.isEndNode()) { node.mark(); }
                }
                // Remove marked nodes and modify their neighbor accordingly
                for (Node<Integer> node : nodes) {
                    if (node.isMarked()) {                  // for marked nodes...
                        if (!node.hasChildren()) {          // that has only parent
                            List<Node<Integer>> parents = new ArrayList<>(node.getParents());
                            Node<Integer> parent = parents.get(0);
                            parent.removeChild(node);
                        } else {                            // that has only child
                            List<Node<Integer>> children = new ArrayList<>(node.getChildren());
                            Node<Integer> child = children.get(0);
                            child.removeParent(node);
                        }
                        removals.add(node);                 // Add node to be removed
                    }
                }
                nodes.removeAll(removals);                  // Remove all removable nodes from set
            } else {                                        // If nodes set contains 2 or less nodes, we're finished
                nodes.clear();
            }
            step++;
        } while(nodes.size() > 1);
        
        System.out.println(step);
    }
}// class Solution

// Generic node class with id, marked flag and any number of parents and children
class Node<T> {
    public Node(T id) { this.id = id; }                     // Constructor
    // parents
    public Set<Node<T>> getParents() { return parents; }
    public void         addParent(Node<T> parent) {         // Add parent node if it wasn't among parents (as it is set)
        parents.add(parent);
    }
    public boolean      removeParent(Node<T> parent) {      // Returns true if parents contained parent
        return parents.remove(parent);
    }
    // children
    public Set<Node<T>> getChildren() { return children; }
    public boolean      hasChildren() {                     // Returns true if node has at least one child
        return children.size() > 0;
    }
    public void         addChild(Node<T> child) {           // Add child node if it wasn't among children
        children.add(child);
    }
    public boolean      removeChild(Node<T> child) {        // Returns true if children contained child
        return children.remove(child);
    }
    // other
    public void         mark() { marked = true; }           // Set marked flag
    public boolean      isMarked() { return marked; }       // Returns true if marked flag is set
    public T            getId() { return id; }              // Returns id
    public boolean      isEndNode() {                       // Returns true if node has only one parent or child
        return (parents.isEmpty() && children.size() == 1) ||
               (children.isEmpty() && parents.size() == 1);
    }

    private T           id = null;                          // id
    private boolean     marked = false;                     // marked flag
    private Set<Node<T>> parents = new HashSet<>();         // parent nodes
    private Set<Node<T>> children = new HashSet<>();        // children nodes
}// class Node<>