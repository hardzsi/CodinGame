<?php
// Chuck Norris
    $MESSAGE = stream_get_line(STDIN, 100, "\n");
    error_log("MESSAGE:$MESSAGE");
    // Convert MESSAGE to string as a binary character chain
    $chars = str_split($MESSAGE);
    $bin = "";                                                              // Store binary chain as string
    for ($i = 0; $i < count($chars); ++$i) {
        $bin .= sprintf("%07d", decbin(ord($chars[$i])));
    }
    error_log("string in binary format: $bin\n\noutput:");
    
    // Encode binary chain in Chuck Norris way
    $answer = "";                                                           // Stores the encoded chain
    $bit = "";
    $index = 0; 
    while($index < strlen($bin)) {
        $count = 1;            
        $bit = $bin[$index];                                                // Get first bit
        while ($index < strlen($bin)) {
            $count = 1;
            $answer .= ($bit == "1" ? "0 " : "00 ");                        // Add bit symbol (1:"0",0:"00") + space
            while (++$index < strlen($bin)) {
                if ($bin[$index] == $bit) {
                    $count++;
                } else {
                    break;
                }        
            }
            $answer .= sprintf("%0".$count."d", 0);                         // Add count number of "0"-s to string 
            $bit = ($bit == "1" ? "0" : "1");                               // Switch bit                          
            if ($index < strlen($bin)) { $answer .= " "; }                  // Add space if not at EoS   
        }
    }
    
    echo("$answer\n");
?>