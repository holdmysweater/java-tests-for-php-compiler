<?php
// Чтение размера массива
$n = fgets(STDIN);

// Чтение элементов массива
$array = [];
for ($i = 0; $i < $n; $i++) {
    $array[] = trim(fgets(STDIN));
}

echo "Исходный массив: " . implode(' ', $array) . "\n";

// Сортировка пузырьком
for ($i = 0; $i < $n - 1; $i++) {
    for ($j = 0; $j < $n - $i - 1; $j++) {
        if ($array[$j] > $array[$j + 1]) {
            // Меняем местами
            $temp = $array[$j];
            $array[$j] = $array[$j + 1];
            $array[$j + 1] = $temp;
        }
    }
}

echo "Отсортированный массив: " . implode(' ', $array);
?>