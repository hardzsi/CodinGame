// The Gift
import java.util.*;

class Solution {
    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int N = in.nextInt();                   // Number of participants
        int C = in.nextInt();                   // Price of gift

        StringBuffer output = new StringBuffer();
        int sum = 0;
        int budget[] = new int[N];
        for (int i = 0; i < N; i++) {
            int B = in.nextInt();               // Budget
            budget[i] = B;
            sum += B;
        }
        System.err.println(N + " participants, price of gift:" + C + ", sum:" + sum);        
        if (C > sum) {
            output.append("IMPOSSIBLE");
        } else {
            Arrays.sort(budget);
            for (int i = 0; i < budget.length; ++i) {
                int avg = (int)(C / (budget.length - i));
                if (budget[i] < avg) {
                    C -= budget[i];
                    output.append(budget[i]);
                } else {
                    C -= avg;
                    output.append(avg);
                }
                output.append("\n");
            }
        }
        System.err.println("output:");
        System.out.println(output.toString());
    }// main()
}