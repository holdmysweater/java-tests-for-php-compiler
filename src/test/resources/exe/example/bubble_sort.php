<?php
// Reading array size
$n = fgets(STDIN);

// Reading array elements
$array = [];
for ($i = 0; $i < $n; $i++) {
    $array[] = fgets(STDIN);
}

echo "Original array: " . implode(' ', $array) . "\n";

// Bubble sort
for ($i = 0; $i < $n - 1; $i++) {
    for ($j = 0; $j < $n - $i - 1; $j++) {
        if ($array[$j] > $array[$j + 1]) {
            // Swap elements
            $temp = $array[$j];
            $array[$j] = $array[$j + 1];
            $array[$j + 1] = $temp;
        }
    }
}

echo "Sorted array: " . implode(' ', $array);
?>