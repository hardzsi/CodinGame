// Code of the Rings - Level1
import java.util.*;

class Player {
    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        String magicPhrase = in.nextLine();
        System.err.println("magicPhrase:" + magicPhrase);

        String alphabet = " ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        //String alphaBW = "ZYXWVUTSRQPONMLKJIHGFEDCBA ";
        StringBuffer answer = new StringBuffer();
        char[] zone = new char[30];
        Arrays.fill(zone, ' ');
        int zoneIdx = 0;
        char startCh, findCh;
        int startIdx, findIdx, step;
        for (int i = 0; i < magicPhrase.length(); ++i) {
            startCh = zone[zoneIdx];
            findCh = magicPhrase.charAt(i);
            startIdx = alphabet.indexOf(startCh);
            findIdx = alphabet.indexOf(findCh);
            step = findIdx - startIdx;        
            /*System.err.println("startCh:'" + startCh + "' findCh:'" + findCh + "'" + " zone[" +
                zoneIdx + "]:'" + zone[zoneIdx] + "' startIdx:" + startIdx + " findIdx:" + findIdx + " step:" + step);*/
            char[] roll = new char[Math.abs(step)];
            Arrays.fill(roll, step < 0 ? '-' : '+');
            answer.append(roll).append(".");
            if (i != magicPhrase.length() - 1) {
                answer.append(">");
            }
            zone[zoneIdx] = findCh;            
            zoneIdx = zoneIdx == 29 ? 0 : ++zoneIdx;
        }
        System.out.println(answer.toString());
    }
}