<?php
// Horse-racing Duals
    fscanf(STDIN, "%d", $N);
    $str = array();                 // Strength of horses
    for ($i = 0; $i < $N; $i++) {
        fscanf(STDIN, "%d", $Pi);
        $str[$i] = $Pi;
        error_log("strength: $str[$i]");
    }
    sort($str);
    $diff = abs($str[0] - $str[1]); // Difference at start
    for ($i = 1; $i < count($str) - 1; $i++) {
        if (abs($str[$i] - $str[$i + 1]) < $diff) {
            $diff = abs($str[$i] - $str[$i + 1]);
        }
    }
    print("$diff\n");
?>