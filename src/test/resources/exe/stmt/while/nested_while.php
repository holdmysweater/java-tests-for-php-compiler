<?php
$i = 1;
while ($i <= 2) {
    echo "Outer: $i\n";
    $j = 1;
    while ($j <= 2) {
        echo "  Inner: $j\n";
        $j++;
    }
    $i++;
}
?>