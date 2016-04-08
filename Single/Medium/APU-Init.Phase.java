// APU: Init Phase
import java.util.*;

enum Look {RIGHT, DOWN}

class Player {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int width = in.nextInt();               // Number of cells on the X axis
        in.nextLine();
        int height = in.nextInt();              // Number of cells on the Y axis
        in.nextLine();
        //System.err.println("height:" + height + ", width:" + width);
        boolean[][] nodes = new boolean[height][width];
        String[] coords = new String[getNodeCount(nodes)];
        for (int i = 0; i < height; i++) {
            String line = in.nextLine();        // Width characters, each either 0 or .
            addLine(line, i, nodes);            // Add a line to nodes array
        }
        System.err.println(dispNodes(nodes));
        coords = getCoords(nodes);              // Get coordinate triplets for output
        for (String coord: coords) {
            System.out.println(coord);          // Three coords:a node and its right and bottom neighbors
        }
    }// main()

    // Return the output coordinate triplets
    static String[] getCoords(boolean[][] nodes) {
        String[] coords = new String[getNodeCount(nodes)];
        Arrays.fill(coords, "");
        int node = 0;        
        for (int row = 0; row < nodes.length; ++row) {
            for (int col = 0; col < nodes[row].length; ++col) {
                if (nodes[row][col] == true) {
                    coords[node] += getCoord(nodes, row, col);
                    node++;
                }
            }
        }
        return coords;
    }// getCoords()  

    // Return an output coordinate triplet (one line per node) in "col row" order
    static String getCoord(boolean[][] nodes, int row, int col) {
        int height = nodes.length;
        int width = nodes[0].length;            // True only for constant-width arrays
        StringBuffer coord = new StringBuffer("");
        coord.append(col).append(" ").append(row).append(" ");
                                                // Check next node to the right
        coord.append(getNeighbor(nodes, row, col, Look.RIGHT)).append(" ");
                                                // Check next node to the bottom
        coord.append(getNeighbor(nodes, row, col, Look.DOWN));
        return coord.toString();
    }// getCoord()

    // Return neighbor node coordinates or "-1 -1" if no neighbor
    static String getNeighbor(boolean[][] nodes, int row, int col, Look direction) {
        StringBuffer neighbor = new StringBuffer("-1 -1");
        if (direction.equals(Look.RIGHT)) {
            for (int i = col + 1; i < nodes[row].length; ++i) {
                if (nodes[row][i] == true) {
                    neighbor.setLength(0);
                    neighbor.append(i).append(" ").append(row);
                    break;
                }
            }
        }
        if (direction.equals(Look.DOWN)) {
            for (int i = row + 1; i < nodes.length; ++i) {
                if (nodes[i][col] == true) {
                    neighbor.setLength(0);
                    neighbor.append(col).append(" ").append(i);
                    break;
                }
            }
        }        
        return neighbor.toString();
    }// getNeighbor()

    // Add a line to nodes array 
    static void addLine(String line, int row, boolean[][] nodes) {
        for (int col = 0; col < nodes[row].length; ++col) {
            nodes[row][col] = line.charAt(col) == '0' ? true : false;
        }
    }// addLine()

    // Return number of nodes in nodes array
    static int getNodeCount(boolean[][] nodes) {
        int count = 0;
        for (boolean[] row : nodes) {
            for (boolean node : row) {
                count += (node == true)? 1 : 0;
            }
        }
        return count;
    }// getNodeCount() 

    // Return the visualized nodes array
    static String dispNodes(boolean[][] nodes) {
        StringBuffer buf = new StringBuffer(getNodeCount(nodes) + " nodes in array:\n");
        int count = 0;
        for (int row = 0; row < nodes.length; ++row) {
            for (int col = 0; col < nodes[row].length; ++col) {
                buf.append(nodes[row][col] == true ? "N" : ".");
                count += nodes[row][col] == true ? 1 : 0;
            }
            buf.append("\n");
        }
        return buf.toString();
    }// dispNodes()
}