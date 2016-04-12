// Code vs Zombies 11.28 22:55
import java.util.*;

class Player {
    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int defendHumanID = -1;      
        // game loop
        while (true) {
            String out = "";
            int[] xEdge = {16000, 0};                       // xMin, xMax
            int[] yEdge = {9000, 0};                        // yMin, yMax
            int x = in.nextInt();
            int y = in.nextInt();
            System.err.println("player x:" + x + " y:" + y + "\n" );
            int humanCount = in.nextInt();
            System.err.println(humanCount + " human(s):");
            int[][] humans = new int[humanCount][3];        // 2nd: ID, x, y
            for (int i = 0; i < humanCount; i++) {
                humans[i][0] = in.nextInt();                // ID
                humans[i][1] = in.nextInt();                // x
                humans[i][2] = in.nextInt();                // y
                System.err.println("ID:" + humans[i][0] +
                                   " x:" + humans[i][1] +
                                   " y:" + humans[i][2]);
                if (humans[i][1] < xEdge[0]) { xEdge[0] = humans[i][1]; }
                if (humans[i][1] > xEdge[1]) { xEdge[1] = humans[i][1]; }
                if (humans[i][2] < yEdge[0]) { yEdge[0] = humans[i][2]; }
                if (humans[i][2] > yEdge[1]) { yEdge[1] = humans[i][2]; }
            }
            System.err.println("\nx (min-max): " + xEdge[0] + "-" + xEdge[1]);
            System.err.println("y (min-max): " + yEdge[0] + "-" + yEdge[1]);          
            int zombieCount = in.nextInt();
            System.err.println("\n" + zombieCount + " zombie(s):");            
            int[][] zombies = new int[zombieCount][5];      // 2nd: ID, x, y, xNext, yNext            
            for (int i = 0; i < zombieCount; i++) {
                zombies[i][0] = in.nextInt();               // ID
                zombies[i][1] = in.nextInt();               // x
                zombies[i][2] = in.nextInt();               // y
                zombies[i][3] = in.nextInt();               // xNext
                zombies[i][4] = in.nextInt();               // yNext
                System.err.println("ID:" + zombies[i][0] +
                                   " x:" + zombies[i][1] +
                                   " y:" + zombies[i][2]);               
            }

            int xAvg = xEdge[0] + (int)((xEdge[1] - xEdge[0]) / 2);
            int yAvg = yEdge[0] + (int)((yEdge[1] - yEdge[0]) / 2);                      
            if (humanCount == 2) {
                if (defendHumanID < 0) { defendHumanID = (int)(Math.random() * 2); }
                System.err.println("\ndefendHumanID:" + defendHumanID);
                int xDefend = (humans[0][0] == defendHumanID ? humans[0][1] : humans[1][1]);
                int yDefend = (humans[0][0] == defendHumanID ? humans[0][2] : humans[1][2]);
                int xOffset = (int)(Math.abs(xAvg - xDefend) / 2);
                int yOffset = (int)(Math.abs(yAvg - yDefend) / 2);
                xAvg += (xDefend > xAvg ? xOffset : -xOffset);
                yAvg += (yDefend > yAvg ? yOffset : -yOffset);
                out = xAvg + " " + yAvg;
            } else {
                out = xAvg + " " + yAvg;
            }
            System.out.println(out); // Your destination coordinates
        }
    } // main()
    
    static int closestZombie(int humanID, int[][] humans, int[][] zombies) {
        return 0;
    }
}