<?php
// Чтение размера матрицы
$input = trim(fgets(STDIN));
list($n, $m) = explode(' ', $input);
$n = (int)$n;
$m = (int)$m;

// Чтение матрицы
$matrix = [];
for ($i = 0; $i < $n; $i++) {
    $row = trim(fgets(STDIN));
    $matrix[$i] = array_map('intval', str_split($row));
}

// Чтение точки заливки и нового цвета
$input = trim(fgets(STDIN));
list($x, $y, $newColor) = explode(' ', $input);
$x = (int)$x;
$y = (int)$y;

echo "Исходная матрица:\n";
for ($i = 0; $i < $n; $i++) {
    echo implode('', $matrix[$i]) . "\n";
}

// Функция рекурсивной заливки
function floodFill(&$matrix, $x, $y, $oldColor, $newColor, $n, $m) {
    // Проверка границ
    if ($x < 0 || $x >= $n || $y < 0 || $y >= $m) {
        return;
    }
    
    // Проверка цвета
    if ($matrix[$x][$y] != $oldColor) {
        return;
    }
    
    // Изменение цвета
    $matrix[$x][$y] = $newColor;
    
    // Рекурсивный вызов для соседей
    floodFill($matrix, $x + 1, $y, $oldColor, $newColor, $n, $m);
    floodFill($matrix, $x - 1, $y, $oldColor, $newColor, $n, $m);
    floodFill($matrix, $x, $y + 1, $oldColor, $newColor, $n, $m);
    floodFill($matrix, $x, $y - 1, $oldColor, $newColor, $n, $m);
}

$oldColor = $matrix[$x][$y];
if ($oldColor != $newColor) {
    floodFill($matrix, $x, $y, $oldColor, $newColor, $n, $m);
}

echo "\nМатрица после заливки:\n";
for ($i = 0; $i < $n; $i++) {
    echo implode('', $matrix[$i]) . "\n";
}
?>