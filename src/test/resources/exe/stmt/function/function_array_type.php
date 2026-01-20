<?php
function getNumbers(): array {
    return [1, 2, 3];
}

$numbers = getNumbers();
echo "Array: " . implode(",", $numbers) . "\n";
?>