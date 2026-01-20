<?php
function sum(...$numbers) {
    $total = 0;
    foreach ($numbers as $num) {
        $total += $num;
    }
    return $total;
}

echo "Sum: " . sum(1, 2, 3) . "\n";
echo "Sum: " . sum(1, 2, 3, 4) . "\n";
echo "Sum: " . sum(1, 2, 3, 4, 5) . "\n";
?>