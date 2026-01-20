<?php
// Оператор объединения с null
$value = null;
echo $value ?? "default" . "\n";

$value = "value";
echo $value ?? "default" . "\n";

$arr = ["key" => "existing"];
echo $arr["key"] ?? "default" . "\n";
echo $arr["nonexistent"] ?? "fallback" . "\n";
?>