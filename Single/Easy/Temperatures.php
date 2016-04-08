<?php
// Temperatures
    fscanf(STDIN, "%d", $N);                    // Number of temperatures to analyse
    $TEMPS = stream_get_line(STDIN, 256, "\n"); // N temperatures expressed as integers ranging: -273 to 5526
    $result = 10000;
    $res = $t = 0;
    if ($N == 0) {
        print("0\n");
    } else {
        $temperatures = explode(" ", trim($TEMPS));
        $temps = array();
        for ($i = 0; $i < count($temperatures); $i++) {
            $temps[$i] = (integer)$temperatures[$i];
            error_log($temps[$i]);
        }
        for ($i = 0; $i < count($temps); $i++) {
            $res = abs($result);
            $t = abs($temps[$i]);
            if ($res == $t) {
                if ($result < $temps[$i]) { $result = $temps[$i]; }    
            } else if ($res > $t) {
                $result = $temps[$i];
            }
        }
        print("$result\n");
    }
?>