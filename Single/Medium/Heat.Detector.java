// Heat Detector
import java.util.*;

class Player {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int W = in.nextInt();               // Width of building
        int H = in.nextInt();               // Height of building
        int N = in.nextInt();               // Number of turns before game over
        int X0 = in.nextInt();              // Batman's start position
        int Y0 = in.nextInt();
        
        System.err.println("WxH = " + W + "x" + H);
        String[] directions = new String[] {"U", "UR", "R", "DR", "D", "DL", "L", "UL"};
        int[][] coords = new int[][] {{0, -1}, {1, -1}, {1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0}, {-1, -1}};
        int[] coord = new int[] {X0, Y0};
        int[] move = new int[] {W / 2, H / 2};
        // game loop
        while (true) {
            String BOMB_DIR = in.next();    // Direction of bombs from Batman's current location (U, UR, R, DR, D, DL, L or UL)
            int index = -1;
            for (int i = 0; i < directions.length; ++i) {
                if (directions[i].equals(BOMB_DIR)) {
                    index = i;
                }
            }
            coord[0] += (coords[index][0] * move[0]);
            coord[1] += (coords[index][1] * move[1]);
            if (coord[0] < 0) { coord[0] = 0; }
            if (coord[0] > W - 1) { coord[0] = W - 1; }
            if (coord[1] < 0) { coord[1] = 0; }
            if (coord[1] > H - 1) { coord[1] = H - 1; }
            System.out.println("" + coord[0] + " " + coord[1]);      // Location of next window Batman should jump to
            move[0] -= move[0] / 2;
            move[1] -= move[1] / 2;       
        }
    }
}