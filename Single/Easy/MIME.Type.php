<?php
// MIME Type
    fscanf(STDIN, "%d", $N);                        // Number of elements which make up the association table
    fscanf(STDIN, "%d", $Q);                        // Number Q of file names to be analyzed
    $mimeTypes = array();                           // Store mime types as File ext / MIME type pairs
    for ($i = 0; $i < $N; $i++) {
        fscanf(STDIN, "%s %s", $EXT, $MT);          // File extension and MIME type
        $mimeTypes[$i][0] = strtolower($EXT);
        $mimeTypes[$i][1] = $MT;
        error_log("EXT:$EXT  \tMT:$MT");       
    }
    // Store file names - one filename per line
    $fileNames = array();
    for ($i = 0; $i < $Q; $i++) {
        $FNAME = stream_get_line(STDIN, 500, "\n"); // One filename per line
        $fileNames[$i] = strtolower($FNAME);
        error_log("FNAME:\t\t$FNAME");
    }
    // Fill output string
    $output = array();
    $ext = "";
    $extIndex = 0;                                  // Index position of extension-separator in string
    for ($i = 0; $i < $Q; $i++) {
        $pos = strrpos($fileNames[$i], ".");        // Return index of last '.' or false if not found
        if (is_bool($pos)) {                        // If no extension
            $output[$i] = "UNKNOWN";
        } else {                                                                            // If there is
            $extIndex = $pos; 
            $ext = substr($fileNames[$i], $extIndex + 1);
            error_log("ext:$ext");
            for ($j = 0; $j < $N; $j++) {
                if ($ext == $mimeTypes[$j][0]) {    // Search mimeTypes for extension
                    $output[$i] = $mimeTypes[$j][1];
                    break;
                } else {
                    $output[$i] = "UNKNOWN";
                }
            } 
        }
    }
    // Display result
    foreach ($output as $line) {                    // For each of the Q filenames, display corresponding MIME type
        print("$line\n");                           // If there is no corresponding type, display UNKNOWN
    }                                                    
?>