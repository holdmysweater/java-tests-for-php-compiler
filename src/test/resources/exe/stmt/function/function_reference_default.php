<?php
function increment(&$value, $amount = 1) {
    $value += $amount;
}

$num = 10;
echo "Value: $num\n";
increment($num, 90);
echo "Value: $num\n";
?>