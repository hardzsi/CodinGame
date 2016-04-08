// Power of Thor
import java.util.*;

class Player {
    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int LX = in.nextInt(); // the X position of the light of power
        int LY = in.nextInt(); // the Y position of the light of power
        int TX = in.nextInt(); // Thor's starting X position
        int TY = in.nextInt(); // Thor's starting Y position

        // game loop
        while (true) {
            int E = in.nextInt(); // Level of Thor's remaining energy, representing the number of moves he can still make.
            String move = "";
            int diffX = Math.abs(LX-TX);
            int diffY = Math.abs(LY-TY);
            if (LY < TY) {
                move = "N";
                TY -= 1;
            } else if (LY > TY) {
                if (diffY * 2 > diffX) {
                    move = "S";
                    TY += 1;
                }
            }
            if (LX > TX) {
                move += "E";
                TX += 1;
            } else if (LX < TX) {
                move += "W";
                TX -= 1;
            }
            System.out.println(move); // A single line providing the move to be made: N NE E SE S SW W or NW
        }
    }
}