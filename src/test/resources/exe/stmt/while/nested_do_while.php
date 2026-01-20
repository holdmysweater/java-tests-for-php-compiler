<?php
$i = 1;
do {
    echo "Outer: $i\n";
    $j = 1;
    do {
        echo "  Inner: $j\n";
        $j++;
    } while ($j <= 2);
    $i++;
} while ($i <= 2);
?>