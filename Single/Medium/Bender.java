// Bender, a depressed robot
import java.util.*;
import java.awt.Point;

class Solution {
    static char[][] map;                                                // Store symbols in map as chars

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int L = in.nextInt();                                           // Number of lines/rows (y)
        int C = in.nextInt();                                           // Number of columns (x)
        map = new char[C][L];                                           // The map (x,y)
        StringBuffer output = new StringBuffer();                       // Result moves
        debug(C + "*" + L + " map");
        in.nextLine();
        Robot bender = null;
        List<Point> teleports = new ArrayList<>();                      // Coordinates of teleport pair if appears on map
        for (int y = 0; y < L; y++) {
            String row = in.nextLine();
            char[] rowArray = row.toCharArray();
            for (int x = 0; x < rowArray.length; ++x) {
                map[x][y] = rowArray[x];
            }
            for (int x = 0; x < C; ++x) {
                switch (map[x][y]) {
                    case '@': bender=new Robot(new Point(x, y)); break; // Creating Bender at start point
                    case 'T': teleports.add(new Point(x , y)); break;
                }
            }
        }
        displayMap();
        debug(teleports.size() == 0 ? "no teleports" : "teleports:");
        for (Point teleport : teleports) { debug(teleport); }
        // Logic
        boolean moving = true;                                          // Bender moves until '$' or a loop identified
        char ch, chAt;                                                  // Char at current position & looking in direction
        int steps = 0;                                                  // Count steps for loop identification
        do {
            boolean blocked = true;
            String direction = bender.getDir();                         // Get direction
            // Determine non-blocked direction
            boolean first = true;
            do {
                // Get current position and apply modifiers first time
                Point position = bender.getPos();
                ch = map[(int)position.getX()][(int)position.getY()];   // Char at current position
                if (first) {
                    switch (ch) {
                        case 'E': direction = "EAST"; break;
                        case 'W': direction = "WEST"; break;
                        case 'N': direction = "NORTH"; break;
                        case 'S': direction = "SOUTH"; break;
                        case 'B': bender.switchBreakerMode();           // Found a beer: change breaker mode
                                  debug("switched breaker mode"); break;
                        case 'I': bender.invert(); break;               // Found an inverter
                        case 'T': int index =                           // Found a teleport: move to the other teleport
                                    teleports.indexOf(bender.getPos());
                                  Point other = teleports.get(index == 0 ? 1 : 0);
                                  debug("teleporting to " + other);
                                  bender.setPos(other);
                                  break;
                    }
                }
                ArrayList<String> dirPriority = bender.getDirPriority();                
                // Look char in direction and changes direction
                // using current priorities if encounters an obstacle
                chAt = charAt(bender.getPos(), direction);              // Char looking at direction
                if (chAt == '#' || (chAt == 'X' && !bender.isBreakerMode())) {
                    if (first) {                                        // Set direction to 1st priority first time
                        direction = dirPriority.get(0);
                        first = false;
                    } else {                                            // Set direction to next priority
                        int index = dirPriority.indexOf(direction);
                        direction = (index < dirPriority.size() - 1) ?
                            dirPriority.get(index + 1) : dirPriority.get(0);                        
                    }
                } else {
                    blocked = false;                
                }
            } while (blocked);
            // Now Bender can move to direction from current position
            debug("\nposition:" + bender.getPos() + " direction:" +
                  bender.getDir() + " inverted:" + bender.isInverted() +
                  ", breaker mode:" + bender.isBreakerMode());            
            bender.movePos(direction);
            output.append(direction).append("\n");
            switch (chAt) {
                case ' ': case 'E': case 'W': case 'N':
                case 'S': case 'B': case 'I': case 'T':
                            debug("Bender moved to " + direction +
                                " (char '" + chAt + "') " + bender.getPos());
                            break;
                case 'X':   Point pos = bender.getPos();
                            map[(int)pos.getX()][(int)pos.getY()]=' ';  // We are in breaker mode: destroy X
                            debug("Bender in breaker mode moved to " +
                                direction + " (char '" + chAt + "') " + bender.getPos());
                            break;
                case '$':   moving = false;
                            debug("Bender moved to " + direction +
                                " (char '$') suicide booth: " + bender.getPos());
                            break;
            }
            if (++steps == 1000) {                                      // After such many steps it is considered a loop
                output.setLength(0);
                output.append("LOOP");
                moving = false;
            }
        } while (moving);
        debug("\noutput:");
        System.out.println(output.toString());
    }// main()

    static void debug(Object obj) {
        System.err.println(obj.toString());
    }// debug()

    // Return char looking at direction 
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

    private Point   pos;                                        // Position as (x,y)
    private boolean inverted = false;                           // Inverted direction priority
    private boolean breaker = false;                            // Breaker mode
    private String  dir = "SOUTH";                              // Direction
    private static ArrayList<String>
                    normPriority = new ArrayList<>(Arrays.asList("SOUTH", "EAST", "NORTH", "WEST"));
    private static ArrayList<String>
                    invertedPriority = new ArrayList<>(Arrays.asList("WEST", "NORTH", "EAST", "SOUTH"));
}// class Robot