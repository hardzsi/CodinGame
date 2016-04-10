// Code of the Rings - Level3 (7034 letters)
import java.util.*;

class Player {
    public static final String ALPHABET =
                " ABCDEFGHIJKLMNOPQRSTUVWXYZ";              // 27 chars includong space
    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        String magicPhrase = in.nextLine();

        StringBuffer answer = new StringBuffer();
        char[] zone = new char[30];                         // 30 zones - store current character of each zone
        Arrays.fill(zone, ' ');                             // Each zone starts with a space
        int currPos = 0;                                    // Bilbo's current position
        char findCh;                                        // Character to find
        int[][] steps = new int[3][2];                      // FW and BW steps needed at previous, current & next positions
        int[] minSteps = new int[3];                        // Minimal steps needed at previous, current & next positions
        char[] roll;                                        // Store needed number of rune rooling '+' or '-' signs
        for (int i = 0; i < magicPhrase.length(); ++i) {    // For each character of magic phrase do...
            findCh = magicPhrase.charAt(i);
            // Load values into steps array
            steps[0] = getStep(zone[currPos == 0 ? 29 : currPos - 1], findCh);
            steps[1] = getStep(zone[currPos], findCh);
            steps[2] = getStep(zone[currPos == 29 ? 0 : currPos + 1], findCh);
            // Determine the needed minimal steps
            // at previous, current & next positions
            for (int j = 0; j < minSteps.length; ++j) {
                minSteps[j] = Math.min(steps[j][0], steps[j][1]);
            }
            /*System.err.println("startCh:'"+zone[currPos]+
                "' findCh:'"+findCh+"' steps:"+
                Arrays.deepToString(steps)+" minSteps:"+
                Arrays.toString(minSteps)+"\nzone:"+
                Arrays.toString(zone));*/
            // Compose answer considering minimal steps
            if (minSteps[1] <= minSteps[0] &&
                minSteps[1] <= minSteps[2]) {               // Stay at current position
                roll = getRoll(steps[1]);
                answer.append(roll).append(".");
            } else if(minSteps[2] <= minSteps[0]) {         // Move forward 
                roll = getRoll(steps[2]);
                answer.append(">");
                answer.append(roll).append(".");
                currPos = (currPos == 29)? 0 : ++currPos;
            } else {                                        // Move backward
                roll = getRoll(steps[0]);
                answer.append("<");
                answer.append(roll).append(".");
                currPos = (currPos == 0)? 29 : --currPos;
            }
            zone[currPos] = findCh;            
        }
        System.out.println(answer.toString());
    }

    // Returns needed number of rune rolling '+' or '-' signs from given step
    static char[] getRoll(int[] step) {
        char[] roll;
        if (step[0] <= step[1]) {                           // Roll backward
            roll = new char[step[0]];
            Arrays.fill(roll, '-');
        } else {                                            // Roll forward
            roll = new char[step[1]];
            Arrays.fill(roll, '+');
        }
        return roll;
    } // getRoll()

    // Return needed steps to reach character to be found backward and forward
    static int[] getStep(char startCh, char findCh) {
        int[] step = new int[] {findBack(startCh, findCh), findFwd(startCh, findCh)};
        return step;
    } // getStep()
    
    static int findFwd(char startCh, char findCh) {
        int startIdx = ALPHABET.indexOf(startCh);
        int findIdx = ALPHABET.indexOf(findCh);
        if (findIdx >= startIdx) {
            return findIdx - startIdx;
        } else {
            return (27 - startIdx) + findIdx;
        }
    } // findFwd()
    
    static int findBack(char startCh, char findCh) {
        int startIdx = ALPHABET.indexOf(startCh);
        int findIdx = ALPHABET.indexOf(findCh);
        if (findIdx <= startIdx) {
            return startIdx - findIdx;
        } else {
            return startIdx + (27 - findIdx);
        }
    } // findBack()
}