<?php
// Тернарный оператор
echo (10 > 5) ? "greater" : "less" . "\n";
echo (5 > 10) ? "greater" : "less" . "\n";

$value = "test";
echo $value ?: "default" . "\n";
echo "" ?: "default" . "\n";
echo null ?: "default" . "\n";
?>