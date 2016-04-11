// Mayan Calculation
import java.util.*;

class Solution {
    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int L = in.nextInt();                               // Width of numeral
        int H = in.nextInt();                               // Height of numeral
        long op1 = 0, op2 = 0, result = 0;                  // Value of both operands and the result
        String[] line = new String[H];                      // ASCII representation of 20 numerals - by lines
        List<Numeral> numerals = new ArrayList<>();         // List of 20 available numerals
        List<Numeral> op1numerals = new ArrayList<>();      // Numerals of 1st operand
        List<Numeral> op2numerals = new ArrayList<>();      // Numerals of 2nd operand
        debug("Width:" + L + ", Height:" + H);
        for (int i = 0; i < H; i++) {
            line[i] = in.next();
            debug(line[i]);
        }
        // Storing 20 available numerals by processing each line
        for (int value = 0; value < 20; ++value) {
            int pos = value * L;
            String[] symbol = new String[H];                // ASCII representation of a numeral
            for (int i = 0; i < symbol.length; ++i) {
                symbol[i] = line[i].substring(pos, pos + L);
            }
            numerals.add(new Numeral(symbol, value));
        }
        // Display list of numerals
        //for (Numeral numeral : numerals) { debug(numeral.toString()); }      
        
        // <<< First operand >>>
        int S1 = in.nextInt();
        debug("S1:" + S1);
        String[][] op1symbols = new String[S1 / H][H];      // Array to store all symbols of 1st operand
        for (int i = 0; i < S1; i++) {
            op1symbols[i / H][i - (i / H) * H] = in.next();
        }
        // Determine value of each symbol then store in op1numerals as a numeral
        for (String[] symbol : op1symbols) {
            for (String sLine : symbol) { debug(sLine); }
            debug("value of numeral:" + getValueOfNumeral(symbol, numerals));
            op1numerals.add(new Numeral(symbol, getValueOfNumeral(symbol, numerals)));
        }
        // Determine value of 1st operand
        for (int i = 0; i < op1numerals.size(); ++i) {
            op1 += op1numerals.get(i).getValue() * Math.pow(20, op1numerals.size() - 1 - i);
        }
        debug("op1 = " + op1);

        // <<< Second operand >>>
        int S2 = in.nextInt();
        debug("\nS2:" + S2);
        String[][] op2symbols = new String[S2 / H][H];      // Array to store all symbols of 2nd operand
        for (int i = 0; i < S2; i++) {
            op2symbols[i / H][i - (i / H) * H] = in.next();
        }
        // Determine value of each symbol then store in op2numerals as a numeral
        for (String[] symbol : op2symbols) {
            for (String sLine : symbol) { debug(sLine); }
            debug("value of numeral:" + getValueOfNumeral(symbol, numerals));
            op2numerals.add(new Numeral(symbol, getValueOfNumeral(symbol, numerals)));
        }
        // Determine value of 2nd operand
        for (int i = 0; i < op2numerals.size(); ++i) {
            op2 += op2numerals.get(i).getValue() * Math.pow(20, op2numerals.size() - 1 - i);
        }
        debug("op2 = " + op2);

        // Determine result based on given operation
        String operation = in.next();
        switch (operation) {
            case "*": result = op1 * op2; break;
            case "/": result = op1 / op2; break;
            case "+": result = op1 + op2; break;
            case "-": result = op1 - op2; break;
        }

        debug("\nresult = " + op1 + " " + operation + " " + op2 + " = " + result);
        
        // Determine maximum power
        int i = 0;
        while (result > (long)Math.pow(20, i)) { ++i; }
        --i;
        // Determine and add numerals to output
        StringBuffer output = new StringBuffer();
        long value = result;
        do {
            long numeral = 0;
            long base = (long)(Math.pow(20, i));
            while (value >= base) { value -= base; ++numeral; }
            debug("numeral:" + numeral + ", i:" + i);
            String[] res = numerals.get((int)numeral).getSymbol();
            for (String str : res) {
                output.append(str).append("\n");
            }
            --i;
        } while (i > -1);

        debug("\noutput:");
        System.out.println(output.toString());
    }// main()
    
    // Return value of symbol found among numerals or -1 if symbol not found
    static int getValueOfNumeral(String[] symbol, List<Numeral> numerals) {
        for (Numeral numeral : numerals) {
            boolean same = true;
            String[] sym = numeral.getSymbol();
            for (int i = 0; i < symbol.length; ++i) {
                if (!symbol[i].equals(sym[i])) { same = false; }
            }
            if (same) { return numeral.getValue(); }
        }
        return -1;
    }// getValueOfNumeral()

    static void debug(String str) { System.err.println(str); }
}// class Solution

class Numeral {
    public Numeral(String[] symbol, int value) {
        this.symbol = symbol;
        this.value = value;
    }
    public String[] getSymbol() { return symbol; }
    public int getValue() { return value; }
    /*@Override
    public String toString() {
        StringBuffer result = new StringBuffer("value:" + value);
        for (int i = 0; i < symbol.length; ++i) {
            result.append("\n").append(symbol[i]);
        }
        return result.toString();
    }*/

    private String[] symbol;
    private int value = -1;
}// class Numeral