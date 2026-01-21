<?php
// Операции с массивами
$fruits = ["apple", "banana"];
$fruits[] = "cherry";  // Добавление элемента
echo $fruits[2] . "\n";

echo count($fruits) . "\n";


$colors = ["red" => "apple", "yellow" => "banana"];
echo $colors["yellow"] . "\n";

$numbers = [1, 2, 3];
$numbers[0] = 10;
$numbers[] = 20;
echo $numbers[3] . "\n";
?>