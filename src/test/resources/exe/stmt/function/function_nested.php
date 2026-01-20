<?php
function add($a, $b) {
    return $a + $b;
}

function multiply($a, $b) {
    return $a * $b;
}

function calculate($x, $y, $z) {
    return add(multiply($x, $y), $z);
}

echo "Result: " . calculate(5, 4, 10) . "\n";
?>