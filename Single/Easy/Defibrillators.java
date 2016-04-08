// Defibrillators
import java.util.*;

class Solution {
    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        String LON = in.next();                     // User's longitude (in degrees)
        in.nextLine();
        String LAT = in.next();                     // User's latitude (in degrees)
        in.nextLine();
        int N = in.nextInt();                       // Number of defibrillators
        in.nextLine();

        // Storing all fields
        String[][] data = new String[N][6];         // All fields as:ID, name, address, phone, longitude(deg), latitude(deg)
        for (int i = 0; i < N; i++) {
            String DEFIB = in.nextLine();
            String[] field = DEFIB.split(";");
            for (int j = 0; j < field.length; ++j) {
                data[i][j] = field[j];
            }
        }
        for (String[] d : data) {
            for (String f : d) { System.err.print(f + " | "); }
            System.err.println();
        }

        // Determine distance from user to each defibrillators
        double[] dist = new double[N];
        double[] sorted = new double[N];
        double lonA = toRad(LON);
        double latA = toRad(LAT);
        for (int i = 0; i < dist.length; ++i) {
            double lonB = toRad(data[i][4]);
            double latB = toRad(data[i][5]);
            double x = (lonB - lonA) * Math.cos((latA + latB) / 2);
            double y = latB - latA;
            dist[i] = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)) * 6371;
            System.err.println("dist: " + dist[i]);
        }
        System.err.println();

        // Sort distance array and identify the index of the nearest defib.
        sorted = Arrays.copyOf(dist, dist.length);
        Arrays.sort(sorted);
        double findVal = sorted[0];                 
        int index = 0;                                  // Stores array index of defib. located closest to user’s position 
        for (int i = 0; i < dist.length; ++i) {
            if (dist[i] == findVal) {
                index = i;
                break;
            }
        }
        System.out.println(data[index][1]);
    } // main()
    
    private static double toRad(String DEG) {
        return Math.toRadians(Double.parseDouble(DEG.replace(',', '.')));
    } // toRad()
}