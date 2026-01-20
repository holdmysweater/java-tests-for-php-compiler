<?php
function getCounter() {
    static $counter = 0;
    $counter++;
    return $counter;
}

while (getCounter() <= 3) {
    echo "Iteration\n";
}
?>