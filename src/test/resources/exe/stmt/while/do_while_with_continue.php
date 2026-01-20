<?php
$i = 0;
do {
    $i++;
    if ($i == 3) {
        continue;
    }
    echo $i . "\n";
} while ($i < 5);
?>