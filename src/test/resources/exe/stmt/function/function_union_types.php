<?php
function processValue(string|int|float $value): string {
    return gettype($value) . ": " . $value;
}

echo processValue("hello") . "\n";
echo processValue(42) . "\n";
echo processValue(3.14) . "\n";
?>