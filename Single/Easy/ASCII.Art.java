// ASCII Art
import java.util.*;

class Solution {
    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int L = in.nextInt();
        in.nextLine();
        int H = in.nextInt();
        in.nextLine();
        String T = in.nextLine();

        String[] charset = new String[H];           // 27 ASCII chars including ? mark
        String[] result = new String[H];            // Output ASCII string
        int[] pos = new int[T.length()];
        for (int i = 0; i < H; i++) {               // Create charset
            String ROW = in.nextLine();
            charset[i] = ROW;
            result[i] = "";
        }
        for (int i = 0; i < pos.length; ++i) {      // Store position info starting from 0
            int ch = (int)T.toUpperCase().charAt(i);
            pos[i] = (ch >= 65 && ch <= 90) ? ch - 65 : 26;
            //System.err.println("pos" + i + " = " + pos[i]);
        }
        for (int j = 0; j < pos.length; ++j) {
            int pos1 = pos[j]*L;
            int pos2 = pos1 + L;            
            for (int i = 0; i < H; ++i) {
                String row = charset[i].substring(pos1, pos2);
                result[i] = result[i] + row;
                //System.err.println("row:" + row + "   " + result[i]);
            }
        }
        // Display ASCII string
        for (String row : result) {
            System.out.println(row);    
        }
    }
}