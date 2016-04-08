// The Paranoid Android opt3 - 2000
import java.util.*;
class Player {
    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int levels = in.nextInt();  // floors num
        int width = in.nextInt();   // area width
        in.nextInt(); in.nextInt(); // max num of rounds | floor where exit is
        int exitPos = in.nextInt(); // exit pos
        in.nextInt(); in.nextInt(); // clones num | other lifts
        int lifts = in.nextInt();   // lifts num
        boolean[] blocked = new boolean[levels]; // floor blocked yet or no
        Integer[][] data = new Integer[lifts][2];// sortable lift data
        for (int i = 0; i < lifts; i++) {
            data[i][0] = in.nextInt(); // floor where lift is
            data[i][1] = in.nextInt(); // lift pos on its floor
        }
        Arrays.sort(data, new Comparator<Integer[]>() { // sort data by 1st dim
            @Override
            public int compare(final Integer[] arg1, final Integer[] arg2) {
                final Integer a = arg1[0];
                final Integer b = arg2[0];
                return a.compareTo(b);
            }
        });
        // game loop
        while (true) {
            int cloneLev = in.nextInt();// floor of lead clone, -1 if dead
            int clonePos = in.nextInt();// lead clone pos, -1 if dead
            String dir = in.next();     // lead clone dir: LEFT,RIGHT or NONE if dead
            String move = "WAIT";
            if (cloneLev != -1) {       // If no clone arrives, wait next round
                int targetPos = (cloneLev < levels - 1) ? data[cloneLev][1] : exitPos;
                if ((targetPos < clonePos && dir.equals("RIGHT") && !blocked[cloneLev]) ||
                    (targetPos > clonePos && dir.equals("LEFT")  && !blocked[cloneLev])) {
                    move = "BLOCK";
                    blocked[cloneLev] = true;
                }
            }
            System.out.println(move); // WAIT or BLOCK
        }
    }
}