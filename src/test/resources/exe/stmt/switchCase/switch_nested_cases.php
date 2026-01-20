<?php
$numbers = [2, 3];
foreach ($numbers as $num) {
    switch ($num % 2) {
        case 0:
            echo "Even number\n";
            break;
        case 1:
            echo "Odd number\n";
            break;
    }
}
?>