// Skynet: the Chasm
import java.util.*;

class Player {
    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int R = in.nextInt();       // the length of the road before the gap.
        int G = in.nextInt();       // the length of the gap.
        int L = in.nextInt();       // the length of the landing platform.
        boolean beforeJump = true;
        int step = 0;
        // game loop
        while (true) {
            int S = in.nextInt();   // the motorbike's speed.
            int X = in.nextInt();   // the position on the road of the motorbike.
            System.err.println("gap:" + G + ", length:" + R + ", pos: " + X + ", speed:" + S);
            if (beforeJump) {
                if ((X+S) <= R) {
                    if (S < (G+1)) {
                        System.out.println("SPEED");
                    } else if (S > (G+1)) {
                        System.out.println("SLOW");
                    } else {
                        System.out.println("WAIT");
                    }
                } else {
                    System.out.println("JUMP");
                    beforeJump = false;
                }
            } else {
                System.out.println(S > 0 ? "SLOW" : "WAIT");
            }
            step++;
        }
    }
}