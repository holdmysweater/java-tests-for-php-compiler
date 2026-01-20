<?php
$values = [1, 2, 3, 5];
foreach ($values as $val) {
    switch (true) {
        case ($val >= 1 && $val <= 3):
            echo "Between 1 and 3\n";
            break;
        case ($val > 3):
            echo "Greater than 3\n";
            break;
    }
}
?>