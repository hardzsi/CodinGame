<?php
// Heat Detector
    fscanf(STDIN, "%d %d", $W, $H);     // Width,Height of building
    fscanf(STDIN, "%d", $N);            // Number of turns before game over
    fscanf(STDIN, "%d %d", $X0, $Y0);   // Batman's start position

    error_log("WxH = $W"."x"."$H");
    $directions = array("U", "UR", "R", "DR", "D", "DL", "L", "UL");
    $coords = array(array(0, -1), array(1, -1), array(1, 0), array(1, 1),
                    array(0, 1), array(-1, 1), array(-1, 0), array(-1, -1));
    $coord = array($X0, $Y0);
    $move =  array((integer)($W / 2), (integer)($H / 2));
    while (TRUE) {
        fscanf(STDIN, "%s", $BOMB_DIR); // Direction of bombs from Batman's current location (U, UR, R, DR, D, DL, L or UL)
        error_log("bomb_dir:$BOMB_DIR");
        $index = -1;
        for ($i = 0; $i < count($directions); $i++) {
            if ($directions[$i] == $BOMB_DIR) {
                $index = $i;
            }
        }
        $coord[0] += ($coords[$index][0] * $move[0]);
        $coord[1] += ($coords[$index][1] * $move[1]);
        if ($coord[0] < 0)      { $coord[0] = 0; }
        if ($coord[0] > $W - 1) { $coord[0] = $W - 1; }
        if ($coord[1] < 0)      { $coord[1] = 0; }
        if ($coord[1] > $H - 1) { $coord[1] = $H - 1; }
        print("$coord[0] $coord[1]\n"); // Location of next window Batman should jump to
        $move[0] -= (integer)($move[0] / 2);
        $move[1] -= (integer)($move[1] / 2);
    }
?>