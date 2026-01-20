<?php
$i = 0;
while ($i < 3) {
    echo "Outer: $i\n";
    $j = 0;
    while (true) {
        $j++;
        if ($j == 2) {
            break;
        }
        echo "  Inner: $j\n";
    }
    $i++;
}
?>