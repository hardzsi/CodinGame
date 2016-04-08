// Stock Exchange Losses
import java.util.*;

class Solution {
    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        in.nextLine();
        String vs = in.nextLine();
        String[] valuesStr = vs.split(" ");             // values as string
        Integer[] values = new Integer[n];              // values as int
        int min = 0, maxBeforeMin = 0, minIndex = -1;
        int loss = 0;                                   // Determine a loss sequence (sum of consecutive losses)        
        List<Integer> losses = new ArrayList<>();       // Store loss sequences

        // Store values as int
        System.err.println("values:");
        for (int i = 0; i < values.length; ++i) {
            values[i] = Integer.parseInt(valuesStr[i]);
            System.err.println(values[i]);
        }

        // Get losses out of individual loss sequences
        for (int i = 0; i < values.length - 1; ++i) {
            if (values[i] > values[i + 1]) {
                loss += values[i + 1] - values[i];
            } else if (loss != 0) {
                losses.add(loss);
                loss = 0;
            }
        }
        if (loss != 0) { losses.add(loss); }

        // Sort and display losses
        Collections.sort(losses);
        System.err.println("sorted losses:");        
        for (Integer value: losses) {
            System.err.println(value);
        }

        // Determine minimum value and index of it
        Integer[] values2 = new Integer[n];        
        values2 = Arrays.copyOf(values, values.length);
        Arrays.sort(values2);
        min = values2[0];
        minIndex = Arrays.asList(values).indexOf(Integer.valueOf(min));
        System.err.println("min " + min + ", min index pos:" + minIndex);

        // Determine a maximum value located before the minimum value
        Integer[] values3 = new Integer[minIndex + 1];
        values3 = Arrays.copyOf(values, values3.length);
        Arrays.sort(values3);
        maxBeforeMin = values3[values3.length - 1];
        System.err.println("max before min: " + maxBeforeMin);

        // Determine output
        int output = 0;
        if (!losses.isEmpty()) {                            // If list empty, no losses happened:output 0; otherwise...
            output = Math.min(min - maxBeforeMin, losses.get(0));   // Larger of max-min loss and largest loss from losses 
        }
        System.err.println("output:");
        System.out.println(output);
    }
}