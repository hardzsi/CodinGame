<?php
// Onboarding
while (TRUE)
{
    fscanf(STDIN, "%s", $enemy1);                           // name of enemy 1
    fscanf(STDIN, "%d", $dist1 );                           // distance to enemy 1
    fscanf(STDIN, "%s", $enemy2);                           // name of enemy 2
    fscanf(STDIN, "%d", $dist2 );                           // distance to enemy 2

    error_log("enemy1:'$enemy1' distance:$dist1");
    // Same but longer: error_log("enemy1:".var_export($enemy1, true)." distance:".var_export($dist1, true));
    error_log("enemy2:'$enemy2' distance:$dist2");
    echo (($dist1 < $dist2) ? "$enemy1\n" : "$enemy2\n");   // output a correct ship name to shoot (don't forget trailing \n)
}
?>