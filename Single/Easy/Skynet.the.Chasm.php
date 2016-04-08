<?php
// Skynet: the Chasm
// Output: one of 4 keywords: SPEED,SLOW,JUMP,WAIT
    fscanf(STDIN, "%d", $R);        // Length of road before gap
    fscanf(STDIN, "%d", $G);        // Length of gap
    fscanf(STDIN, "%d", $L);        // Length of landing platform
    $beforeJump = true;
    $step = 0;
    while (TRUE) {
        fscanf(STDIN, "%d", $S);    // Motorbike's speed
        fscanf(STDIN, "%d", $X);    // Position on road
        error_log("gap:" + $G + ", length:" + $R + ", pos: " + $X + ", speed:" + $S);
        if ($beforeJump) {
            if (($X + $S) <= $R) {
                if ($S < ($G + 1)) {
                    echo("SPEED\n");
                } else if ($S > ($G + 1)) {
                    echo("SLOW\n");
                } else {
                    echo("WAIT\n");
                }
            } else {
                echo("JUMP\n");
                $beforeJump = false;
            }
        } else {
            echo($S > 0 ? "SLOW\n" : "WAIT\n");
        }
        $step++;
    }
?>