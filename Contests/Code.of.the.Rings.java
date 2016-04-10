// Code of the Rings - Level2 (15012 letters)
import java.util.*;

class Player {
    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        String magicPhrase = in.nextLine();

        String alphabet = " ABCDEFGHIJKLMNOPQRSTUVWXYZ";    // 27 chars
        StringBuffer answer = new StringBuffer();
        char[] zone = new char[30];
        Arrays.fill(zone, ' ');
        int zoneIdx = 0;
        char startCh, findCh;
        int stepFwd, stepBack;
        char[] roll;
        for (int i = 0; i < magicPhrase.length(); ++i) {
            startCh = zone[zoneIdx];
            findCh = magicPhrase.charAt(i);
            stepFwd = findFwd(startCh, findCh, alphabet);
            stepBack = findBack(startCh, findCh, alphabet);
            //System.err.println("startCh:'" + startCh + "' findCh:'" + findCh + "' stepFwd:" + stepFwd + " stepBack:" + stepBack);
            if (stepFwd <= stepBack) {                      // Roll forward
                roll = new char[stepFwd];
                Arrays.fill(roll, '+');
            } else {                                        // Roll backward
                roll = new char[stepBack];
                Arrays.fill(roll, '-');
            }
            answer.append(roll).append(".");
            if (i != magicPhrase.length() - 1) {
                answer.append(">");
            }
            zone[zoneIdx] = findCh;            
            zoneIdx = (zoneIdx == 29)? 0 : ++zoneIdx;
        }
        System.out.println(answer.toString());
    }
    
    static int findFwd(char startCh, char findCh, String alphabet) {
        int startIdx = alphabet.indexOf(startCh);
        int findIdx = alphabet.indexOf(findCh);
        if (findIdx >= startIdx) {
            return findIdx - startIdx;
        } else {
            return (27 - startIdx) + findIdx;
        }
    } // findFwd()
    
    static int findBack(char startCh, char findCh, String alphabet) {
        int startIdx = alphabet.indexOf(startCh);
        int findIdx = alphabet.indexOf(findCh);
        if (findIdx <= startIdx) {
            return startIdx - findIdx;
        } else {
            return startIdx + (27 - findIdx);
        }
    } // findBack()
}