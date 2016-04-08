<?php
// ASCII Art
    fscanf(STDIN, "%d", $L);                // Letter width
    fscanf(STDIN, "%d", $H);                // Letter height
    $T = stream_get_line(STDIN, 256, "\n"); // Line of text
    
    $charset = array();                     // 27 ASCII chars including ? mark
    $result = array();                      // Output ASCII string
    $pos = array_fill(0, strlen($T), 0);
    for ($i = 0; $i < $H; $i++) {           // Create charset
        $ROW = stream_get_line(STDIN, 1024, "\n");
        $charset[$i] = $ROW;
        $result[$i] = "";
    }
    for ($i = 0; $i < count($pos); $i++) {  // Store position info starting from 0
        $chStr = strtoupper($T);
        $ch = ord($chStr[$i]);
        $pos[$i] = ($ch >= 65 && $ch <= 90) ? $ch - 65 : 26;
        //error_log("pos $i = $pos[$i]");
    }
    for ($j = 0; $j < count($pos); $j++) {
        $pos1 = $pos[$j] * $L;
        $pos2 = $L;
        for ($i = 0; $i < $H; $i++) {
            $row = substr($charset[$i], $pos1, $pos2);
            $result[$i] .= $row;
            //error_log("row:$row   $result[$i]");
        }
    }
    // Display ASCII string
    foreach ($result as $row) {
        echo("$row\n");    
    }
?>