// Indiana - Level 1
import java.util.*;

class Player {
    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int W = in.nextInt();                   // Number of columns
        int H = in.nextInt();                   // Number of rows
        System.err.println("columns x rows (W x H):" + W + " x " + H);
        in.nextLine();

        Map<Integer, String[]> rooms = new HashMap<>(); // Store room types and paths through it: K:type, V:"IN-OUT"
        rooms.put( 0, new String[] {"" });              // IN,OUT: T:TOP, B:BOTTOM, L:LEFT, R:RIGHT, X:block
        rooms.put( 1, new String[] {"TB", "LB", "RB"});
        rooms.put( 2, new String[] {"LR", "RL"});
        rooms.put( 3, new String[] {"TB"});
        rooms.put( 4, new String[] {"TL", "RB", "LX"});
        rooms.put( 5, new String[] {"TR", "LB", "RX"});
        rooms.put( 6, new String[] {"TX", "LR", "RL"});
        rooms.put( 7, new String[] {"TB", "RB"});
        rooms.put( 8, new String[] {"LB", "RB"});
        rooms.put( 9, new String[] {"LB", "TB"});
        rooms.put(10, new String[] {"TL", "LX"});
        rooms.put(11, new String[] {"TR", "RX"});
        rooms.put(12, new String[] {"RB"});
        rooms.put(13, new String[] {"LB"});

        String[] line = new String[H];          // A line (row) as individual string elements
        int[][] maze = new int[H][W];           // Maze of rooms (rows x columns)
        for (int i = 0; i < H; i++) {
            String LINE = in.nextLine();        // Represents a line in the grid and contains W integers.
            line = LINE.split(" ");
            for (int j = 0; j < line.length; ++j) {
                maze[i][j] = Integer.parseInt(line[j]);               
            }
        }                                       // Each integer represents one room of a given type.
        int EX = in.nextInt();                  // The coordinate along the X axis of the exit
        in.nextLine();                          // (not useful for this first mission, but must be read).

        // game loop
        while (true) {
            int XI = in.nextInt();              // XI, YI: Indy's current position on the grid
            int YI = in.nextInt();
            String POS = in.next();             // Indy enters from: TOP, LEFT or RIGHT
            in.nextLine();
            System.err.println("XI:" + XI + ", YI:" + YI + ", room type:" + maze[YI][XI]);
            String[] moves = rooms.get(maze[YI][XI]);
            String heading = "";
            for (String move : moves) {
                if (move.startsWith(POS.substring(0, 1))) {
                    heading = move;
                    System.err.println("heading:" + heading);
                }
            }
            switch(heading.substring(1, 2)) {
                case "B": ++YI; break;
                case "L": --XI; break;
                case "R": ++XI; break;
            }
            System.out.println(XI + " " + YI);  // One line containing X Y coordinates of room in which
        }                                       // you believe Indy will be on the next turn.
    }
}