// The Paranoid Android - 3446
import java.util.*;

class Player {
    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int nbFloors = in.nextInt();                    // number of floors
        int width = in.nextInt();                       // width of the area
        int nbRounds = in.nextInt();                    // maximum number of rounds
        int exitFloor = in.nextInt();                   // floor on which the exit is found
        int exitPos = in.nextInt();                     // position of the exit on its floor
        int nbTotalClones = in.nextInt();               // number of generated clones
        int nbAdditionalElevators = in.nextInt();       // ignore (always zero)
        int nbElevators = in.nextInt();                 // number of elevators

        boolean[] blocked = new boolean[nbFloors];      // Flag to indicate if floor is blocked or not
        Integer[][] data = new Integer[nbElevators][2]; // Store elevator floor & position for ordering
        for (int i = 0; i < nbElevators; i++) {
            data[i][0] = in.nextInt();                  // floor on which this elevator is found
            data[i][1] = in.nextInt();                  // position of the elevator on its floor
            debug(i + "; elevatorFloor:" + data[i][0] + ", elevatorPos:" + data[i][1]);
        }
        Arrays.sort(data, new Comparator<Integer[]>() { // Sorting 2dim. data array as per 1st dimension
            @Override
            public int compare(final Integer[] entry1, final Integer[] entry2) {
                final Integer a = entry1[0];
                final Integer b = entry2[0];
                return a.compareTo(b);
            }
        });
        int[] liftPos = new int[nbElevators];
        for (int i = 0; i < nbElevators; i++) {         // Storing 2dim data in liftPos array for convenience
            liftPos[i] = data[i][1];
        }
        debug("nbFloors:" + nbFloors + ", width:" + width + ", nbRounds:" + nbRounds + ", clones:" + nbTotalClones +
            "\nexitFloor:" + exitFloor + ", exitPos:" + exitPos + ", nbElevators:" + nbElevators);
        // game loop
        while (true) {
            int cloneFloor = in.nextInt();              // floor of the leading clone, -1 if dead
            int clonePos = in.nextInt();                // position of the leading clone on its floor, -1 if dead
            String direction = in.next();               // direction of the leading clone: LEFT or RIGHT, or NONE if dead
            String move = "WAIT";
            if (cloneFloor != -1) {                     // If no clone arrives, wait for another round
                int targetPos = (cloneFloor < nbFloors - 1) ? liftPos[cloneFloor] : exitPos;
                debug("cloneFloor:" + cloneFloor + ", targetPos:" + targetPos + ", clonePos:" + clonePos + ", direction:" + direction);
                if ((targetPos < clonePos && direction.equals("RIGHT") && !blocked[cloneFloor]) ||
                    (targetPos > clonePos && direction.equals("LEFT")  && !blocked[cloneFloor]))  {
                    move = "BLOCK";
                    blocked[cloneFloor] = true;
                }
            }
            System.out.println(move);                   // action: WAIT or BLOCK
        }
    } // main()

    private static void debug(String s) {
        System.err.println(s);
        return;
    } // debug()
} // class Player()