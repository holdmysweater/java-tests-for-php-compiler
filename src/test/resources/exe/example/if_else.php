<?php
$input = fgets(STDIN);
$number = $input;

if ($number > 10) {
    echo "Greater than 10";
} elseif ($number > 5) {
    echo "Greater than 5, but not greater than 10";
} else {
    echo "5 or less";
}
?>