// Telephone Numbers
import java.util.*;

class Solution {
    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int N = in.nextInt();                   // Number of phone numbers
        System.err.println(N + " phone number" + (N == 1 ? "" : "s"));
        Node node, child;                       // Current node and child node
        Node start = new Node();                // Start node
        for (int i = 0; i < N; i++) {
            String telephone = in.next();       // Phone number (2-20 digits)
            System.err.println("\nphone number:" + telephone);
            node = start;            
            for (int j = 0; j < telephone.length(); ++j) {
                int id = Integer.parseInt(String.valueOf(telephone.charAt(j)));
                child = node.getChild(id);
                if (child == null) {
                    child = new Node(id);
                    //System.err.println("added child " + child);
                    node.addChild(child);
                } else {
                    //System.err.println("child is " + child);

                }
                node = child;                
            }
        }
        System.out.println(start.getCount());   // Number of elements (referencing a number) stored in the structure
    }
}

class Node {
    public Node() {}
    public Node(int id) { this.id = id; ++count; }

    public void addChild(Node child) { children.add(child); }

    public Node getChild(int id) {
        for (Node child : children) {
            if (child.id == id) { return child; }
        }
        return null;
    }
    public int getCount() { return count; }

    @Override public String toString() {
        StringBuffer buf = new StringBuffer();
        if (children.size() == 0) {
            buf.append("none");
        } else {
            for (Node child : children) {
                buf.append(child.id).append(",");
            }
            buf.deleteCharAt(buf.length() - 1);
        }
        return "node " + id + " (children:" + buf.toString() + ")";
    }

    private static int count = 0;
    private int id;
    Set<Node> children = new HashSet<>();
} // class Node