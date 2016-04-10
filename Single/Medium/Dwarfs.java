// Dwarfs standing on the shoulders of giants
import java.util.*;

class Solution {
    static List<Integer> steps = new ArrayList<>();         // Longest succession of influences: steps of all possible
                                                            // routes from top (parentless) to bottom (childless) nodes
    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();                               // Number of relationships of influence
        System.err.println(n + " relationships");
        Set<Node>          nodes = new HashSet<>();         // Set of created nodes (Note: removeAll() may slow in List)
        Map<Integer, Node> nodesMap = new HashMap<>();      // Store nodes in a map with id as key for quicker access

        // Build graph storing adjacency relations in
        // nodes, storing those in a map as well
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
                System.err.println("\ndetermining steps " +
                        "from top node " + node.getId());
                getSteps(node, 0);
            }
        }

        // Sorting steps and listing it
        Collections.sort(steps);
        System.err.println("\nsteps sorted:");
        for (Integer step : steps) {
            System.err.println("" + step);
        }

        System.err.println("\noutput:");
        System.out.println(steps.get(steps.size() - 1) + 1); // Number of people involved in longest succession of influences
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

// Simple Node class with methods necessary for solution only
class Node {
    public Node(int id) { this.id = id; }                             // Constructor

    public boolean    hasParents() { return parents.size() > 0; }     // Returns true if node has at least one parent
    public void       addParent(Node parent) { parents.add(parent); } // Add parent node if it wasn't among parents (as it is set)
    public Set<Node>  getChildren() { return children; }
    public void       addChild(Node child) { children.add(child); }   // Add child node if it wasn't among children
    public int        getId() { return id; }                          // Returns id

    private int       id = -1;                                        // id
    private Set<Node> parents = new HashSet<>();                      // parent nodes
    private Set<Node> children = new HashSet<>();                     // children nodes
}// class Node