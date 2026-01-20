<?php
// Смешанные выражения
$a = 5;
$b = 3;
$c = 2;

echo $a + $b * $c . "\n";           // 5 + 6 = 11
echo ($a + $b) * $c . "\n";         // 8 * 2 = 16

$result = ($a > $b) && ($b < $c);
echo $result . "\n";                // true && false = false

$str1 = "Hello";
$str2 = "World";
echo $str2 . $str1 . "\n";          // WorldHello

$arr = [1, 2, 3];
echo isset($arr[0]) . "\n";         // 1
echo isset($arr[5]) . "\n";         // (пустая строка)
?>