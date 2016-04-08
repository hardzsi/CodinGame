// Horse-racing Duals
import java.util.*;

class Solution {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int N = in.nextInt();
        int[] str = new int[N];                     // Strength of horses
        for (int i = 0; i < N; i++) {
            int Pi = in.nextInt();
            str[i] = Pi;
            System.err.println("strength: " + str[i]);
        }
        Arrays.sort(str);
        int diff = Math.abs(str[0] - str[1]);       // Difference at start 
        for (int i = 1; i < str.length - 1; ++i) {
            if (Math.abs(str[i] - str[i+1]) < diff) {
                diff = Math.abs(str[i] - str[i+1]);
            } 
        }
        System.out.println(diff);
    }
}