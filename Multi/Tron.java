// Rank 277
import java.awt.Point;
import java.util.Scanner;
import java.util.Vector;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;

class Player {
    // Global variables defined for convenient access
    static List<HashSet<Point>> reserved = new ArrayList<HashSet<Point>>(); // Set to store all reserved grid positions    
    static int N = 0;                                                       // total number of players (2 to 4).    
    static Point actP = new Point(-1, -1);                                         // Actual point/position of my bike [X1,Y1]

    // Store next move with number of valid steps possible from that move
    static class Step {
        Step(String aMove, int aCount) { move = aMove; count = aCount; } 
        String  getMove()  { return move; }
        int     getCount() { return count; }
        @Override
        public String toString() { return move + " (" + count + ")"; }
        private String move;                                                // next move
        private int count;                                                  // number of valid steps from next move
    } // class Step()

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        // Initializing values
        String move = "LEFT";                                               // Starting direction
        String[] moves = new String[3];                                     // Order of next moves according to strategy
        boolean firstRound = true;

        // game loop
        while (true) {
            N = in.nextInt();                                               // total number of players (2 to 4).
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
                    actP.move(X1, Y1);                                      // Move my bike to [X1,Y1] position
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
            if (firstRound) { firstRound = false; }                         // Store [X0,Y0] coords only first time
            // Determine possible next positions to step excluding bounds
            moves = getCurlyMoves(move);                                    // Set strategy
            Vector<String> validMoves = getValidMoves(moves);               // Store valid moves
            if (validMoves.isEmpty()) {
                System.err.println("OH, fuck! We lost. Waiting for timeout...");
            } else {                                                        // We have at least 1 valid move
				Vector<String> safeMoves = getSafeMoves(validMoves);
				move = safeMoves.firstElement();
				System.out.println(move);
			}
        } // while()
    } // main()

    // Return coordinates of p as string - used for displaying debug info
    private static String coords(Point p) {
        return "[x=" + (int)p.getX() + ",y=" + (int)p.getY() + "]";
    }

    // Returns clockwise curly moving order from current move (our strategy)
    private static String[] getCurlyMoves(String move) {
        String[] moves = null;
        switch (move) {
            case "LEFT" : moves = new String[] {"UP", "LEFT", "DOWN"}; break;
            case "RIGHT": moves = new String[] {"DOWN", "RIGHT", "UP"}; break;
            case "UP"   : moves = new String[] {"RIGHT", "UP", "LEFT"}; break;
            case "DOWN" : moves = new String[] {"LEFT", "DOWN", "RIGHT"}; break;
        }
        return moves;
    }

    // Returns true if p is reserved
    private static boolean isReserved(Point p) {
        boolean allocated = false;
        for (int i = 0; i < N && !allocated; ++i) {
            if (reserved.get(i) != null) {
                if (reserved.get(i).contains(p)) { allocated = true; }
            }
        }
        return allocated;
    }
    
    // Returns true if p is within game grid
    private static boolean onGrid(Point p) {
        int X = (int)(p.getX());
        int Y = (int)(p.getY());
        return (X >= 0 && X < 30 && Y >= 0 && Y < 20) ? true : false;
    }

    // Returns position of next move as a point
    private static Point getP(String move, Point p) {
        int X = (int)(p.getX());
        int Y = (int)(p.getY());
        switch(move) {
            case "LEFT" : return new Point(X - 1, Y);
            case "RIGHT": return new Point(X + 1, Y);
            case "UP"   : return new Point(X, Y - 1);
            case "DOWN" : return new Point(X, Y + 1);
        }
        return null;
    }

    // Returns valid moves (if there are any) from moves, otherwise returns an empty vector
    private static Vector<String> getValidMoves (String[] moves) {
        Vector<String> validMoves = new Vector<>();
        for (String move : moves) {
            if (onGrid(getP(move, actP)) && !isReserved(getP(move, actP))) {
                validMoves.add(move);
            }
        }
        return validMoves;
    }

    // Returns moves ordered by safetiness (first is the most safe)
    private static Vector<String> getSafeMoves(Vector<String> moves) {
        Vector<Player.Step> steps = new Vector<>();     // Store next moves with valid steps possible from those moves
        Vector<String> safeMoves = new Vector<>();
        for (String move : moves) {
            String[] nextMoves = getCurlyMoves(move);
			int validCount = 0;
			Point p = getP(move, actP);
			for (String m : nextMoves) {
				if (onGrid(getP(m, p)) && !isReserved(getP(m, p))) {    
					validCount++;
				}
			}
			steps.add(new Step(move, validCount));
        }      
		for(Step step : steps) {                        // Put moves with 3 or 2 valid directions to safeMoves
			if (step.getCount() >= 2) {
				safeMoves.add(step.getMove());
			}
		}
		for(Step step : steps) {                        // Put moves with 1 valid direction to safeMoves
			if (step.getCount() == 1) {
				safeMoves.add(step.getMove());
			}
		}
		for(Step step : steps) {                        // Put moves with 0 valid directions to safeMoves
			if (step.getCount() == 0) {
				safeMoves.add(step.getMove());
			}
		}
		System.err.println("Steps:");
		for (Step step : steps) {
		    System.err.println(step);
		}
		if (steps.size() == 2) {
		    int count1 = steps.get(0).getCount();
		    int count2 = steps.get(1).getCount();
		    if ((count1 == 1 && count2 == 3) || (count2 == 1 && count1 == 3)) {
    		    System.err.println("Corner detected - entering corner...");
    		    safeMoves.clear();
    		    safeMoves.add(count1 == 1 ? steps.get(0).getMove() : steps.get(1).getMove());		        
		    }
		}
		System.err.println("Safe moves:");
		for (int i = 0; i < safeMoves.size(); ++i) {
		    System.err.println(safeMoves.get(i));
		}
        return safeMoves;
    }
}