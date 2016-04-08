// Mars Lander - Level1
import java.util.*;

class Player {
    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int N = in.nextInt();           // Number of points used to draw the surface of Mars
        for (int i = 0; i < N; i++) {   // Linking all points together in a sequence form the surface of Mars
            int LAND_X = in.nextInt();  // X coordinate of a surface point (0 to 6999)
            int LAND_Y = in.nextInt();  // Y coordinate of a surface point
        }
        while (true) {
            int X = in.nextInt();
            int Y = in.nextInt();
            int HS = in.nextInt();      // Horizontal speed (m/s), can be negative
            int VS = in.nextInt();      // Vertical speed (m/s), can be negative
            int F = in.nextInt();       // Quantity of remaining fuel (l)
            int R = in.nextInt();       // Rotation angle (degrees): -90 to 90
            int P = in.nextInt();       // Thrust power: 0 to 4

            System.out.println("0 " + (VS <= -40 ? 4 : 0)); // R P. R:desired rotation angle, P:desired thrust power
        }
    }
}