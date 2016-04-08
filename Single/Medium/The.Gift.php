<?php
// The Gift
    fscanf(STDIN, "%d", $N);        // Number of participants
    fscanf(STDIN, "%d", $C);        // Price of gift
    
    $output = "";
    $sum = 0;
    $budget = array();
    for ($i = 0; $i < $N; $i++) {
        fscanf(STDIN, "%d", $B);    // Budget
        $budget[$i] = $B;
        $sum += $B;
    }
    error_log("$N participants, price of gift:$C, sum:$sum");        
    if ($C > $sum) {
        $output = ("IMPOSSIBLE\n");
    } else {
        sort($budget);
        for ($i = 0; $i < count($budget); $i++) {
            $avg = (integer)($C / (count($budget) - $i));
            if ($budget[$i] < $avg) {
                $C -= $budget[$i];
                $output .= ($budget[$i]);
            } else {
                $C -= $avg;
                $output .= ($avg);
            }
            $output .= ("\n");
        }
    }
    error_log("output:\n");
    print($output);    
?>