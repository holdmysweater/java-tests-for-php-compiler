<?php
$numbers = [1, 2, 3, 4];
foreach ($numbers as $num) {
    switch ($num) {
        case 1:
            echo "Number 1\n";
            break;
        case 2:
            echo "Number 2\n";
            break;
        case 3:
            echo "Number 3\n";
            break;
        default:
            echo "Not 1, 2, or 3\n";
    }
}
?>