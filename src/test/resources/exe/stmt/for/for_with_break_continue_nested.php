<?php
for ($i = 0; $i < 3; $i++) {
    echo "Outer: $i\n";
    for ($j = 0; $j < 3; $j++) {
        if ($j == 1) {
            break;
        }
        echo "  Inner: $j\n";
    }
}
?>