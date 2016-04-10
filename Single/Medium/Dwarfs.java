// Dwarfs standing on the shoulders of giants
import java.util.*;

class Solution {
    static List<Integer> steps = new ArrayList<>();         // Store longest succession of influences: steps of all possible
                                                            // routes starting from top (parentless) to bottom (childless) nodes
    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();                               // Number of relationships of influence
        System.err.println(n + " relationships");
        Set<Node>          nodes = new HashSet<>();         // Set of created nodes (Note: removeAll() may slow in List)
        Map<Integer, Node> nodesMap = new HashMap<>();      // Store nodes in a map with id as key for quicker access

        // Build graph storing adjacency relations in nodes - storing nodes in a map as well
        for (int i = 0; i < n; i++) {
            int x = in.nextInt();                           // Relationship of influence between two people (x influences y)
            int y = in.nextInt();
            System.err.println("x:" + x + ", y:" + y);
            Node upper = nodesMap.get(x);                   // Get upper node from nodes map
            if (upper == null) {                            // Create and store new upper node if not found among nodes
                upper = new Node(x);
                nodes.add(upper);
                nodesMap.put(x, upper);
            }
            Node lower = nodesMap.get(y);                   // Get lower node from nodes map
            if (lower == null) {                            // Create and store new lower node if not found among nodes
                lower = new Node(y);
                lower.addParent(upper);                     // Add upper node as parent to lower node
                nodes.add(lower);
                nodesMap.put(y, lower);
            } else {                                        // Found lower node among nodes
                lower.addParent(upper);                     // Add upper node as parent to lower node
            }
            upper.addChild(lower);                          // Add lower node as child to upper node
        }

        // Collect steps needed from top (parentless) nodes
        for (Node node : nodes) {
            if (!node.hasParents()) {
                System.err.println("\ndetermining steps from top node " + node.getId());
                getSteps(node, 0);
            }
        }

        // Listing steps sorted
        Collections.sort(steps);
        System.err.println("\nsteps sorted:");
        for (Integer step : steps) { System.err.println("" + step); }
        
        System.err.println("\noutput:");
        System.out.println(steps.get(steps.size()-1) + 1);  // Number of people involved in the longest succession of influences
    }// main()

    // Determine and store steps needed from given node
    static void getSteps(Node node, int step) {
        Set<Node> children = node.getChildren();
        if (children.size() != 0) {
            for (Node child : children) {
                getSteps(child, step + 1);
            }
        } else {
            steps.add(step);
        }
    }// getSteps()

}// class Solution

// Sortable node class with id and arbitrary number of parents and children
class Node implements Comparable<Node> {
    public Node(int id) { this.id = id; }                   // Constructor
    // parents
    public Set<Node>    getParents() { return parents; }
    public boolean      hasParents() {                      // Returns true if node has at least one parent
        return parents.size() > 0;
    }    
    public void         addParent(Node parent) {            // Add parent node if it wasn't among parents (as it is set)
        parents.add(parent);
    }
    public boolean      removeParent(Node parent) {         // Returns true if parents contained parent
        return parents.remove(parent);
    }
    // children
    public Set<Node>    getChildren() { return children; }
    public boolean      hasChildren() {                     // Returns true if node has at least one child
        return children.size() > 0;
    }
    public void         addChild(Node child) {              // Add child node if it wasn't among children
        children.add(child);
    }
    public boolean      removeChild(Node child) {           // Returns true if children contained child
        return children.remove(child);
    }
    // other
    public int          getId() { return id; }              // Returns id
    public boolean      isEndNode() {                       // Returns true if node has only one parent or child
        return (parents.isEmpty() && children.size() == 1) ||
               (children.isEmpty() && parents.size() == 1);
    }
    @Override
    public int          compareTo(Node node) { return id - node.getId(); }
    
    @Override
    public String       toString() {                        // Return "node id (children:ids  parents:ids) | level:level"
        StringBuffer pBuf = new StringBuffer("none");
        if (parents.size() > 0) {
            pBuf.setLength(0);
            for (Node parent : parents) {
                pBuf.append(parent.getId()).append(",");
            }
            pBuf.deleteCharAt(pBuf.length() - 1);
        }
        StringBuffer cBuf = new StringBuffer("none");
        if (children.size() > 0) {
            cBuf.setLength(0);
            for (Node child : children) {
                cBuf.append(child.getId()).append(",");
            }
            cBuf.deleteCharAt(cBuf.length() - 1);
        }
        return "node " + id + " (children:" + cBuf.toString() + "  parents:" + pBuf.toString() + ")";
    }// toString()

    private int         id = -1;                            // id
    private Set<Node>   parents = new HashSet<>();          // parent nodes
    private Set<Node>   children = new HashSet<>();         // children nodes
}// class Node