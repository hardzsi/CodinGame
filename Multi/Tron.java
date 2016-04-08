import java.util.*;
import java.io.*;
import java.math.*;
import java.awt.Point;
import java.util.Random;
import java.util.Vector;
import java.util.HashSet;
import java.util.ArrayList;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
class Player {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);       
        // Initializing values
        String move = "LEFT";                                               // Starting direction
        String[] moves = new String[3];   
        boolean firstRound = true;
        List<HashSet<Point>> reserved = new ArrayList<HashSet<Point>>();    // Set to store all reserved grid positions
        Point actPos = new Point(-1, -1);                                   // Actual position of my bike (X1, Y1)
        // game loop
        while (true) {
            int N = in.nextInt();                                           // total number of players (2 to 4).
            int P = in.nextInt();                                           // your player number (0 to 3).   
            if (firstRound) {                                               // Initializing array of sets
                for (int i = 0; i < N; ++i) {
                    reserved.add(new HashSet<Point>()); 
                }
            }
            for (int i = 0; i < N; i++) {
                int X0 = in.nextInt(); // starting X coordinate of lightcycle (or -1)
                int Y0 = in.nextInt(); // starting Y coordinate of lightcycle (or -1)
                int X1 = in.nextInt(); // starting X coordinate of lightcycle (can be the same as X0 if you play before this player)
                int Y1 = in.nextInt(); // starting Y coordinate of lightcycle (can be the same as Y0 if you play before this player)
                Point p1 = new Point(X1, Y1);
                if (firstRound) {
                    Point p0 = new Point(X0,Y0);                   
                    reserved.get(i).add(p0);
                    System.err.println((i == P ? "Me    " : "Player") + i + ":" + coords(p0) + " added (p0)");
                }
                if (i == P) {
                    reserved.get(i).add(p1);
                    System.err.println("Me    " + i + ":" + coords(p1) + " added");                    
                    actPos.move(X1, Y1);
                } else {
                    if (p1.getX() != -1) {
                        reserved.get(i).add(p1);
                        System.err.println("Player" + i + ":" + coords(p1) + " added");   
                    } else {
                        if (reserved.get(i) != null) {
                            System.err.println("Player" + i + " died. Removing its reserved locations.");
                            reserved.set(i, null);                        
                        }
                    }
                }
            }
            if (firstRound) { firstRound = false; }                         // Not to store X0, Y0 any more
            // Determine possible next positions to step excluding bounds           
            moves = curly_moves(move);                                      // Determine strategy
            String nextMove = null;         
            Vector<String> optMove = new Vector<>();                        // Store move options
            Vector<String> safeMove = new Vector<>();                       // Store moves considered safe (not go to pipe)
            for (int i = 0; i < moves.length; ++i) {
                nextMove = checkMove(moves[i], reserved, actPos, N);       // Return next move or 'null' if cannot move
                if (nextMove != null) {
                    optMove.add(nextMove);    
                }
            }
            if (optMove == null) {
                System.err.println("OH, fuck! We lost. Waiting for timeout...");
            } else {
                for (String m : optMove) {
                    System.err.println("opt.move: " + m);
                    if (checkPipe(m, reserved, actPos, N) == "OK") {
                        safeMove.add(m);
                        System.err.println("move " + m + " considered safe");
                    }
                }
                if (safeMove.isEmpty()) {
                    move = optMove.firstElement();                          // Choose first from options                
                } else {
                    move = safeMove.firstElement();                         // Choose first safe move
                }
                System.out.println(move);                                   // A single line with UP, DOWN, LEFT or RIGHT                
            }
        } // while()
    } // main()
    
    private static String coords(Point p) {
        return "[x=" + (int)p.getX() + ",y=" + (int)p.getY() + "]";  
    }
    // Our strategy
    private static String[] curly_moves(String move) {                  // Csiga
        String[] moves = null;
        switch (move) {
            case "LEFT": moves = new String[] {"UP", "LEFT", "DOWN"}; break;
            case "RIGHT":moves = new String[] {"DOWN", "RIGHT", "UP"}; break;
            case "UP":   moves = new String[] {"RIGHT", "UP", "LEFT"}; break;
            case "DOWN": moves = new String[] {"LEFT", "DOWN", "RIGHT"}; break;
        }
        return moves;
    }
    // Return true if p reserved
    private static boolean isReserved(Point p, List<HashSet<Point>> reserved, int N) {
        boolean allocated = false;
        for (int i = 0; i < N && !allocated; ++i) {
            if (reserved.get(i) != null) {
                if (reserved.get(i).contains(p)) {
                    allocated = true;
                }    
            }
        }
        return allocated;
    }
    // Validate nextMove. If nextMove valid, returns it, otherwise returns null 
    private static String checkMove(String nextMove, List<HashSet<Point>> reserved, Point actPos, int N) {
        int X = (int)(actPos.getX());
        int Y = (int)(actPos.getY());
        if (nextMove == "LEFT") {    
            if (X != 0 && !isReserved(new Point(X - 1, Y), reserved, N)) {
                return "LEFT";
            }
        }
        if (nextMove == "RIGHT") {
            if (X != 29 && !isReserved(new Point(X + 1, Y), reserved, N)) {
                return "RIGHT";
            }
        }
        if (nextMove == "UP") {     
            if (Y != 0 && !isReserved(new Point(X, Y - 1), reserved, N)) {
                return "UP";
            }
        }
        if (nextMove == "DOWN") {     
            if (Y != 19 && !isReserved(new Point(X, Y + 1), reserved, N)) {
                return "DOWN";
            }
        }
        return null;
    }

    private static String checkPipe(String nextMove, List<HashSet<Point>> reserved, Point actPos, int N) {
        
        return "OK";
    }
}