<?php
function sum(int $a, int $b): int {
    return $a + $b;
}

function concat(string $a, string $b): string {
    return $a . $b;
}

echo "Sum: " . sum(3, 5) . "\n";
echo "Concat: " . concat("Hello", "World") . "\n";
?>