// The Descent
import java.util.*;

class Player {
    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);

        // game loop
        int[] height = new int[8];
        int maxHeight = 0;
        boolean canFire = true;
        while (true) {
            int SX = in.nextInt();
            int SY = in.nextInt();
            canFire = true;
            for (int i = 0; i < 8; i++) {
                int MH = in.nextInt(); // represents the height of one mountain, from 9 to 0. Mountain heights are provided from left to right.
                height[i] = MH;
            }
            maxHeight = 0;  // get max height
            for (int j = 0; j < height.length; ++j) {
                if (height[j] > maxHeight) {
                    maxHeight = height[j];
                }
            }
            if (height[SX] == maxHeight && canFire) {
                System.out.println("FIRE"); // either: FIRE (ship is firing its phase cannons) or HOLD (ship is not firing).
                canFire = false;
            } else {
                System.out.println("HOLD");
            }
        }
    }
}