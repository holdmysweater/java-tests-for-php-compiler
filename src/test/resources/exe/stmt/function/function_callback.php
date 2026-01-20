<?php
function process($a, $b, $callback) {
    return $callback($a, $b);
}

$result = process(10, 20, function($x, $y) {
    return $x + $y;
});

echo "Result: $result\n";
?>