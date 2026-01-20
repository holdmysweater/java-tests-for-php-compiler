<?php
$i = 0;
while ($i < 5) {
    $i++;
    if ($i == 3) {
        continue;
    }
    echo $i . "\n";
}
?>