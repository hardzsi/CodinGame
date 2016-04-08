<?php
// Power of Thor
    fscanf(STDIN, "%d %d %d %d",
        $LX, $LY,                   // X,Y position of Light of Power
        $x, $y                      // Thor's starting X,Y position
    );
    while (true) {
        fscanf(STDIN, "%d", $RT);   // Remaining turns Thor can move
        $move = "";
        if ($LY < $y) {
            $move = "N";
            $y--;
        } else if ($LY > $y) {
            if (abs($LY - $y) * 2 > abs($LX - $x)) {
                $move = "S";
                $y++;
            }
        }
        if ($LX > $x) {
            $move .= "E";
            $x++;
        } else if ($LX < $x) {
            $move .= "W";
            $x--;
        }
        print($move."\n");          // A move can be: N NE E SE S SW W or NW
    }
?>