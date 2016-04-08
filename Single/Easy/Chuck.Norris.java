// Chuck Norris
import java.util.*;

class Solution {
    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        String MESSAGE = in.nextLine();
 
        // Convert MESSAGE to string as a binary character chain
        char[] chars = MESSAGE.toCharArray();
        StringBuffer binChain = new StringBuffer();       
        for (int i = 0; i < chars.length; ++i) {
            binChain.append(String.format("%7s", Integer.toBinaryString((byte)chars[i] & 0xFF)).replace(' ', '0'));
        }
        String bin = binChain.toString();
        System.err.println("string in binary format: " + bin);

        // Encode binary chain in Chuck Norris way
        StringBuffer answer = new StringBuffer();                               // Stores the encoded chain
        String bit = "";
        int index = 0; 
        while(index < bin.length()) {
            int count = 1;            
            bit = String.valueOf(bin.charAt(index));                            // Get first bit
            System.err.println("string length:" + bin.length());
            while (index < bin.length()) {
                count = 1;
                answer.append(bit.equals("1") ? "0 " : "00 ");                  // Add bit symbol (1:"0",0:"00") + space
                System.err.println("index:" + index + ", bit:" + bit);
                while (++index < bin.length()) {
                    if (String.valueOf(bin.charAt(index)).equals(bit)) {
                        count++;
                    } else {
                        break;
                    }        
                }
                System.err.println("bit count:" + count);
                answer.append(String.format("%0" + count + "d", 0));            // Add count number of "0"-s to string 
                bit = (bit.equals("1")) ? "0" : "1";                            // Switch bit                          
                if (index < bin.length()) { answer.append(" "); }               // Add space if not at EoS   
            }
        }
        System.out.println(answer.toString());
    }
}