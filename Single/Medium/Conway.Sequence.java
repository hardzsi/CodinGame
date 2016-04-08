// Conway Sequence
import java.util.*;

class Solution {
    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int R = in.nextInt();                       // Starting number
        int L = in.nextInt();                       // Line to display: times x value
        System.err.println("R:" + R + ", L:" + L + "\n");
        
        List<Integer>[] lines = new ArrayList[L];   // Store all lines as integer lists
        for (int i = 0; i < L; ++i) {               // Initializing all lists
            lines[i] = new ArrayList();
        }
        lines[0].add(R);                            // Create first line
        System.err.println("lines[0]:" + lines[0]);
        // Determine following lines until L
        for (int level = 1, idx = 0, times = 0; level < L; ++level) {
            do {
                times = getTimes(lines[level - 1], idx);
                lines[level].add(times);
                lines[level].add(lines[level - 1].get(idx));
                idx += times;
            } while (idx < lines[level - 1].size());
            System.err.println("lines[" + level + "]:" + lines[level]);
            idx = 0;
        }
        // Convert list to space-separated string to provide answer
        StringBuffer answer = new StringBuffer();
        for (Integer value : lines[L - 1]) {
            answer.append(value).append(" ");
        }
        answer.deleteCharAt(answer.length() - 1);
        
        System.out.println(answer.toString());
    }// main()

    static int getTimes(List<Integer> line, int index) {
        int value = line.get(index);
        int count = 1;
        while (++index < line.size()) {
            if (line.get(index) == value) {
                ++count;
            } else {
                break;
            }
        }       
        return count;
    } // getTimes()
}