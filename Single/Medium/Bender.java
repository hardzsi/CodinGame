// Bender, a depressed robot
import java.util.*;
import java.awt.Point;

class Solution {
    static char[][] map;                            // Store symbols in map as chars

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int L = in.nextInt();                       // Number of lines/rows (y)
        int C = in.nextInt();                       // Number of columns (x)
        map = new char[C][L];                       // The map (x,y)
        StringBuffer output = new StringBuffer();   // Output strings
        System.err.println(C + "*" + L + " map");
        in.nextLine();
        Point start = null;                         // Start (x,y) coordinates
        List<Point> teleports = new ArrayList<>();  // Contains (x,y) coordinates of teleport pair if appears on map
        for (int y = 0; y < L; y++) {
            String row = in.nextLine();
            char[] rowArray = row.toCharArray();
            for (int x = 0; x < rowArray.length; ++x) {
                map[x][y] = rowArray[x];
            }
            for (int x = 0; x < C; ++x) {
                switch (map[x][y]) {
                    case '@': start = new Point(x, y); break;
                    case 'T': teleports.add(new Point(x , y)); break;
                }
            }
        }
        displayMap();
        System.err.println("\nstart:" + start + "\n" + (teleports.size() == 0 ? "no teleports" : "teleports:"));
        for (Point teleport : teleports) { System.err.println(teleport); }

        Robot bender = new Robot(start);            // Alternative: Robot bender = new Robot(start, "EAST", true, true);
     
        System.err.println("\nposition:" + bender.getPos() + " direction:" + bender.getDir() +
                " inverted:" + bender.isInverted() + ", breaker mode:" + bender.isBreakerMode());

        // Logic
        boolean moving = true;                                          // Bender moves until '$' or until identifies a loop
        char ch, chAt;                                                  // Char @ current position and poisition in direction     
        int maxSteps = (C - 2) * (L - 2);                               // Maximum number of steps Bender can take
        do {
            boolean blocked = true;
            ArrayList<String> dirPriority = bender.getDirPriority();
            String direction = bender.getDir();
            // Determine non-blocked direction
            boolean first = true;
            do {
                Point position = bender.getPos();
                ch = map[(int)position.getX()][(int)position.getY()];   // Char @ current position
                if (ch == 'E' || ch == 'W' || ch == 'N' || ch == 'S') { // Change position if we stand on a char E/W/N/S
                    switch (ch) {
                        case 'E':   direction = "EAST"; break;
                        case 'W':   direction = "WEST"; break;
                        case 'N':   direction = "NORTH"; break;
                        case 'S':   direction = "SOUTH"; break;
                    }
                }
                if (ch == 'B') { bender.switchBreakerMode(); System.err.println("switched breaker mode"); }          // Found a beer: change breaker mode
                if (ch == 'I') { bender.invert(); }                     // Found an inverter
                if (ch == 'T') {                                        // Found a teleport: move to the other teleport
                    int index = teleports.indexOf(bender.getPos());
                    Point other = teleports.get(index == 0 ? 1 : 0);
                    System.err.println("teleporting to " + other);
                    bender.setPos(other);
                }
                chAt = charAt(bender.getPos(), direction);              // Char @ position found in direction from current
                System.err.println("char in direction " + direction + ": '" + chAt + "'");
                if (chAt == '#' || (chAt == 'X' && !bender.isBreakerMode())) {
                    if (first) {
                        direction = dirPriority.get(0);
                        first = false;
                    } else {
                        int index = dirPriority.indexOf(direction);
                        direction = (index < dirPriority.size() - 1) ? dirPriority.get(index + 1) : dirPriority.get(0);                        
                    }
                } else {
                    blocked = false;                
                }
            } while (blocked);
            // Now Bender can move to direction from current position
            System.err.println("\nposition:" + bender.getPos() + " direction:" + bender.getDir() +
                " inverted:" + bender.isInverted() + ", breaker mode:" + bender.isBreakerMode());            
            switch (chAt) {
                case ' ': case 'E': case 'W': case 'N': case 'S': case 'B': case 'I': case 'T':
                            bender.movePos(direction);
                            output.append(direction).append("\n");
                            System.err.println("char '" + chAt + "' , Bender moved to " + bender.getPos());
                            break;
                case 'X':   bender.movePos(direction);
                            output.append(direction).append("\n");
                            System.err.println("char '" + chAt + "' , Bender in breaker mode moved to " + bender.getPos());
                            Point position = bender.getPos();
                            map[(int)position.getX()][(int)position.getY()] = ' ';  // In breaker mode: destroy X                   
                            break;
                case '$':   bender.movePos(direction);
                            output.append(direction);
                            System.err.println("char '$', Bender moved to suicide booth: " + bender.getPos());
                            moving = false;
                            break;
                default:    System.err.println("ERROR: found unexpected char '" + chAt + "'");
                            break;
            }
            if (maxSteps-- == 0) {                              // Moved whole map w/o reaching booth, should be loop
                output.setLength(0);
                output.append("LOOP");
                moving = false;
            }
        } while (moving);
        System.err.println("\noutput:");
        System.out.println(output.toString());
    }// main()

    // Returns the char in map at position found in direction of current position 
    static char charAt(Point position, String direction) {
        int x = (int)position.getX();
        int y = (int)position.getY();
        switch (direction) {
            case "WEST": x--; break;
            case "EAST": x++; break;
            case "NORTH": y--; break;
            case "SOUTH": y++; break;
        }
        return map[x][y];
    }// charAt()

    static void displayMap() {
        for (int y = 0; y < map[0].length; ++y) {
            String row = "";
            for (int x = 0; x < map.length; ++x) {
                row += map[x][y] + " ";
            }
            System.err.println(row);
        }
    }// displayMap()
}// class Solution

class Robot {
    // Constructors
    public Robot(Point pos) { this.pos = new Point(pos); }
    public Robot(Point pos, String dir, boolean inv, boolean brk) { 
        this.pos = new Point(pos);
        this.dir = dir;
        inverted = inv;
        breaker = brk;
    }
    // Getters
    public Point    getPos() { return pos; }
    public String   getDir() { return dir; }
    public ArrayList<String>
                    getDirPriority() { return inverted? invertedPriority : normPriority; }
    public boolean  isInverted() { return inverted; }
    public boolean  isBreakerMode() { return breaker; }
    // Setters
    public void     setPos(Point pos) { this.pos = new Point(pos); }
    public void     movePos(String direction) {
        int x = (int)pos.getX();
        int y = (int)pos.getY();
        switch (direction) {
            case "WEST": x--; break;
            case "EAST": x++; break;
            case "NORTH": y--; break;
            case "SOUTH": y++; break;
        }
        pos = new Point(x, y);
        dir = direction;
    }
    public void     invert() { inverted = !inverted; }
    public void     switchBreakerMode() { breaker = !breaker; }

    private Point   pos;                // Position as (x,y)
    private boolean inverted = false;   // Inverted direction priority
    private boolean breaker = false;    // Breaker mode
    private String  dir = "SOUTH";      // Direction
    private static ArrayList<String>
                    normPriority = new ArrayList<>(Arrays.asList("SOUTH", "EAST", "NORTH", "WEST"));
    private static ArrayList<String>
                    invertedPriority = new ArrayList<>(Arrays.asList("WEST", "NORTH", "EAST", "SOUTH"));
}// class Robot