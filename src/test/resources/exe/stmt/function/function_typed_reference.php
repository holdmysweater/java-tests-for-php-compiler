<?php
function doubleInt(int &$value) {
    $value *= 2;
}

$num = 5;
echo "Before: $num\n";
doubleInt($num);
echo "After: $num\n";
?>