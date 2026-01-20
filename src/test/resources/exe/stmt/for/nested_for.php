<?php
for ($i = 1; $i <= 2; $i++) {
    echo "Outer: $i\n";
    for ($j = 1; $j <= 2; $j++) {
        echo "  Inner: $j\n";
    }
}
?>