<?php
$x = 0;
$y = 0;
do {
    echo "x: $x, y: $y\n";
    $x++;
    $y += 2;
} while ($x < 3 && $y < 3);
?>