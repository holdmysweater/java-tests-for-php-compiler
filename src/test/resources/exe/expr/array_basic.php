<?php
// Базовые операции с массивами
$arr1 = [1, 2, 3];
echo gettype($arr1) . "\n";

$arr2 = array("apple", "banana");
echo gettype($arr2) . "\n";

echo $arr2[0] . "\n";
echo $arr2[1] . "\n";

$empty = [];
echo gettype($empty) . "\n";
?>