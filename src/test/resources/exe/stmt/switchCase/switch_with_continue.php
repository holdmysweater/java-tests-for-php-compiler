<?php
for ($i = 1; $i <= 3; $i++) {
    switch ($i) {
        case 1:
            echo "Iteration $i: One\n";
            break;
        case 2:
            echo "Iteration $i: Skip\n";
            continue 2; // continue the outer loop
        case 3:
            echo "Iteration $i: Three\n";
            break;
    }
}
?>