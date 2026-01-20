<?php
$input = trim(fgets(STDIN));
$number = (int)$input;

if ($number > 10) {
    echo "Больше 10";
} elseif ($number > 5) {
    echo "Больше 5, но не больше 10";
} else {
    echo "5 или меньше";
}
?>