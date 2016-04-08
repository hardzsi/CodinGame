// MIME Type
// For each of the Q filenames, display on a line the corresponding MIME type.
// If there is no corresponding type, then display UNKNOWN.
import java.util.*;

class Solution {
    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int N = in.nextInt();                           // Number of elements which make up the association table.
        in.nextLine();
        int Q = in.nextInt();                           // Number Q of file names to be analyzed.
        in.nextLine();
        String[][] mimeTypes = new String[N][2];        // Store mime types as EXT / MIME TYPE pairs
        for (int i = 0; i < N; i++) {
            String EXT = in.next();                     // file extension
            String MT = in.next();                      // MIME type
            mimeTypes[i][0] = EXT.toLowerCase();
            mimeTypes[i][1] = MT;
            System.err.println("EXT:" + EXT + ", MT:" + MT);
            in.nextLine();
        }

        // Store file names - one filename per line
        String[] fileNames = new String[Q];
        for (int i = 0; i < Q; i++) {
            String FNAME = in.nextLine();
            fileNames[i] = FNAME.toLowerCase();
            System.err.println("FNAME:" + FNAME);
        }
        System.err.println();
        
        // Fill output string
        String[] output = new String[Q];
        String ext = "";
        int extIndex = 0;                               // Index position of extension-separator in string
        for (int i = 0; i < Q; ++i) {
            if ((extIndex = fileNames[i].lastIndexOf(".", fileNames[i].length() - 1)) == -1) {  // If no extension
                output[i] = "UNKNOWN";
            } else {                                                                            // If there is
                ext = fileNames[i].substring(extIndex + 1);
                for (int j = 0; j < N; ++j) {
                    if (ext.equals(mimeTypes[j][0])) {  // Search mimeTypes for extension
                        output[i] = mimeTypes[j][1];
                        break;
                    } else {
                        output[i] = "UNKNOWN";
                    }
                } 
            }
        }
        // Display result
        for (String line : output) {
            System.out.println(line);         
        }
    }
}