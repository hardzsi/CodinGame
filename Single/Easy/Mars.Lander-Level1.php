<?php
// Mars Lander - Level 1
    fscanf(STDIN, "%d", $N);                    // Number of points used to draw the surface of Mars
    for ($i = 0; $i < $N; $i++) {               // Linking all points together in a sequence form the surface of Mars
        fscanf(STDIN, "%d %d", $landX, $landY); // X,Y coordinates of a surface point (0 to 6999)
    }
    while (TRUE)
    {
        fscanf(STDIN, "%d %d %d %d %d %d %d", $X, $Y,
            $HS,                                // Horizontal speed (m/s), can be negative
            $VS,                                // Vertical speed (m/s), can be negative
            $F,                                 // Quantity of remaining fuel in liters
            $R,                                 // Rotation angle in degrees (-90 to 90)
            $P                                  // Thrust power (0 to 4)
        );
        print("0 ".($VS <= -40 ? 4 : 0)."\n");  // R P. R is desired rotation angle, R is desired thrust power
    }
?>