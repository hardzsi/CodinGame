// Temperatures
import java.util.*;

class Solution {
    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int N = in.nextInt();           // the number of temperatures to analyse
        in.nextLine();
        String TEMPS = in.nextLine();   // the N temperatures expressed as integers ranging from -273 to 5526
        int result = 10000;
        int res = 0;
        int t = 0;
        if (N == 0) {
            System.out.println(0);
        } else {
            String[] temperatures = TEMPS.trim().split(" ");
            int[] temps = new int[temperatures.length];
            for (int i = 0; i < temps.length; ++i) {
                temps[i] = Integer.parseInt(temperatures[i]);
                System.err.println(temps[i]);
            }
            for (int i = 0; i < temps.length; ++i) {
                res = Math.abs(result);
                t = Math.abs(temps[i]);
                if (res == t) {
                    if (result < temps[i]) { result = temps[i]; }    
                } else if (res > t) {
                    result = temps[i];
                }
            }
            System.out.println(result);
        }
    }
}