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
        String move = "LEFT";                                               // Default direction
        boolean firstRound = true;
        List<HashSet<Point>> reserved = new ArrayList<HashSet<Point>>();    // Set to store all reserved grid positions
        Point actPos = new Point(-1, -1);                                   // Actual position of my bike (X1, Y1)
        Vector<Point> nextPos = new Vector<>();                             // Possible next positions to step
        Random rand = new Random();
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
                    }
                }
            }
            if (firstRound) { firstRound = false; }                         // Not to store X0, Y0 any more
            // Determine possible next positions to step excluding bounds           
            int X = (int)(actPos.getX());
            int Y = (int)(actPos.getY());
            if (move != "RIGHT" && X != 0) {
                nextPos.add(new Point(X - 1, Y));
            }
            if (move != "LEFT" && X != 29) {
                nextPos.add(new Point(X + 1, Y));
            }
            if (move != "DOWN" && Y != 0) {
                nextPos.add(new Point(X, Y - 1));
            }
            if (move != "UP" && Y != 19) {
                nextPos.add(new Point(X, Y + 1));
            }
            // Remove reserved locations from nextPos
            Vector<Point> removables = new Vector<>();                  // Collect points to remove
            for (Point p : nextPos) {
                for (int j = 0; j < N; ++j) {
                    if (reserved.get(j).contains(p)) {
                        removables.add(p);
                        System.err.println("removed: " + coords(p));
                    }
                }
            }
            nextPos.removeAll(removables);                              // Remove reserved points
            // Display remaining directions
            if (nextPos.size() == 0) {
                System.err.println("OH, fuck! We lost. Waiting for timeout...");
            } else {
                for (Point p : nextPos) {
                    System.err.println("nextPos: " + coords(p));
                }
                // Get a random direction out of nextPos
                Point p = nextPos.get(rand.nextInt(nextPos.size()));
                System.err.println("Our random move: " + coords(p));                    
    
                if (p.getX() != X) {
                    move = (p.getX() > X ? "RIGHT" : "LEFT"); 
                } else {
                    move = (p.getY() > Y ? "DOWN" : "UP");
                }
                nextPos.clear();
                System.out.println(move);                               // A single line with UP, DOWN, LEFT or RIGHT                
            }
        } // while()
    } // main()
    
    private static String coords(Point p) {
        return "[x=" + (int)p.getX() + ",y=" + (int)p.getY() + "]";  
    }
}