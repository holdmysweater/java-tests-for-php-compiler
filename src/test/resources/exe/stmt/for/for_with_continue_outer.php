<?php
for ($i = 0; $i < 3; $i++) {
    if ($i == 1) {
        continue;
    }
    echo "Outer: $i\n";
    for ($j = 0; $j < 2; $j++) {
        echo "  Inner: $j\n";
    }
}
?>