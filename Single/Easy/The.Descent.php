<?php
// The Descent
    $height = array();
    $maxHeight = 0;
    $canFire = true;
    while (TRUE) {
        fscanf(STDIN, "%d %d", $SX, $SY);
        $canFire = true;
        for ($i = 0;  $i < 8; $i++)
        {
            fscanf(STDIN, "%d", $MH);   // Represents height of one mountain from 9-0. Mountain heights provided left to right
            $height[$i] = $MH;
        }
        // Get max height
        for ($j = 0, $maxHeight = 0; $j < count($height); $j++) {
            if ($height[$j] > $maxHeight) {
                $maxHeight = $height[$j];
            }
        }
        if ($height[$SX] == $maxHeight && $canFire) {
            echo("FIRE\n");             // Either FIRE or HOLD
            $canFire = false;
        } else {
            echo("HOLD\n");         
        }
    }
?>