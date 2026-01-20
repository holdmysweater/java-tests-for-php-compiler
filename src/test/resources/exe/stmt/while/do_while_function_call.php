<?php
function getCounter() {
    static $counter = 0;
    $counter++;
    return $counter;
}

do {
    echo "Iteration\n";
} while (getCounter() < 3);
?>