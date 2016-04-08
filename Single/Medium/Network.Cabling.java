// Network Cabling
import java.util.*;

class Solution {
    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int N = in.nextInt();                       // Number of buildings
        List<Point> buildings = new ArrayList<>();  // List of all buildings (with X,Y coords)
        List<Integer> axisY = new ArrayList<>();    // List of Y coords of all buildings
        int minX = 0, maxX = 0;                     // X boundaries (minX can be negative!)
        // Store coords of buildings and all Y coords
        // as lists, determine minX and maxX as well
        for (int i = 0; i < N; i++) {
            int X = in.nextInt();                   // X,Y: coordinates of a building
            int Y = in.nextInt();
            if (i == 0) {
                minX = maxX = X;
            } else {
                minX = Math.min(minX, X);
                maxX = Math.max(maxX, X);
            }
            buildings.add(new Point(X, Y));
            axisY.add(Y);
        }
        Collections.sort(buildings);                // Sort buildings along Y axis
        Collections.sort(axisY);                    // Sort Y axes
        int minY = buildings.get(0).getY();         // Same as: axisY.get(0)
        int maxY = buildings.get(buildings.size() - 1).getY();  // Same as: axisY.get(axisY.size() - 1)
        System.err.println(N + " buildings at X:" + minX +
            " to " + maxX + " | Y:" + minY + " to " + maxY);
        // Check only a limited number of Y axes from
        // the middle of list if Y exceed a given value
        if (axisY.size() > 1000) {
            int toRemove = (int)(axisY.size() / 2.02);
            axisY.subList(0, toRemove).clear();
            axisY.subList(axisY.size() - toRemove, axisY.size()).clear();
            System.err.println("We check only " + axisY.size() + " Y values");            
        }
        // Calculate total cable length from each
        // building to main cable located at Y
        long[] totals = new long[N];                // Cable length totals from each Y coord
        int idx = 0;                                // Note: a total can exceed int range!
        for (Integer y : axisY) {                   // For each Y coord where main cable can be...
            long total = 0;
            for (Point building : buildings) {
                total += (long)Math.abs(building.getY() - y);
            }
            totals[idx++] = total;
        }
        // Pick minimal length from totals
        long minLength = totals[0];
        for (int i = 1; i < idx; ++i) {
            minLength = totals[i] < minLength ? totals[i] : minLength;
        }
        System.out.println(N == 1 ? 0 : (minLength + (maxX - minX)));
    }
} // class Solution

class Point implements Comparable<Point> {
    public Point(int x, int y) { this.x = x; this.y = y; }
    
    @Override public int compareTo(Point point) {
        int result = (int)Math.signum((double)(y - point.y));
        return result == 0 ? (int)Math.signum((double)(x - point.x)) : result;
    }
    
    @Override public String toString() { return x + "," + y; }
    
    public int getX() { return x; }
    public int getY() { return y; }

    private int x;
    private int y;
} // class Point