<?php
// Defibrillators
    fscanf(STDIN, "%s", $LON);                      // User's longitude (in degrees)
    fscanf(STDIN, "%s", $LAT);                      // User's latitude (in degrees)
    fscanf(STDIN, "%d", $N);                        // Number of defibrillators
    
    // Store data for each defibrillator location
    $data = array();                                // fields as string:ID,name,address,phone,longitude(deg),latitude(deg)
    for ($i = 0; $i < $N; ++$i) {
        $DEFIB = stream_get_line(STDIN, 256, "\n");
        $field = explode(";", $DEFIB);
        for ($j = 0; $j < count($field); ++$j) {
            $data[$i][$j] = $field[$j];
        }
    }
    // Display data for each defibrillator location
    foreach ($data as $d) {
        $line = "";
        foreach ($d as $f) { $line .= "$f | "; }
        error_log($line);
    }
    // Determine distance from user to each defibrillators
    $dist = array();
    $lonA = toRad($LON);
    $latA = toRad($LAT);
    for ($i = 0; $i < $N; ++$i) {
        $lonB = toRad($data[$i][4]);
        $latB = toRad($data[$i][5]);       
        $x = (float)(($lonB - $lonA) * cos(($latA + $latB) / 2));
        $y = (float)($latB - $latA);
        $dist[$i] = (float)(sqrt(pow($x, 2) + pow($y, 2)) * 6371.0);
        //error_log("$dist[$i]");
    }
    error_log("\n");

    // Sort distance array and identify the index of the nearest defib.
    $sorted = $dist;                                // Cerate a copy of $dist
    sort($sorted);             
    $index = 0;                                     // Stores array index of defib. located closest to user’s position 
    for ($i = 0; $i < count($dist); ++$i) {
        if ($dist[$i] == $sorted[0]) {
            $index = $i;
            break;
        }
    }
    print($data[$index][1]."\n");                   // Display name of nearest defibrillator location

    function toRad($deg) {
        return deg2rad((float)(str_replace(',', '.', $deg)));
    }
?>