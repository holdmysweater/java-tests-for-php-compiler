<?php
function doubleValue(&$value) {
    $value *= 2;
}

$num = 10;
echo "Original: $num\n";
doubleValue($num);
echo "Modified: $num\n";
?>