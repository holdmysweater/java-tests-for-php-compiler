<?php
$n = (int)trim(fgets(STDIN));

echo "For loop: ";
for ($i = 1; $i <= $n; $i++) {
    echo $i . " ";
}

echo "\nWhile loop: ";
$j = 1;
while ($j <= $n) {
    echo $j . " ";
    $j++;
}

echo "\nDo-while: ";
$k = 1;
do {
    echo $k . " ";
    $k++;
} while ($k <= $n);
?>