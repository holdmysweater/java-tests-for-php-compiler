<?php
for ($i = 1; $i <= 7; $i++) {
    switch ($i) {
        case 1:
        case 2:
        case 4:
        case 5:
            echo "Working day\n";
            break;
        case 3:
            echo "Wednesday\n";
            break;
        case 6:
        case 7:
            echo "Weekend\n";
            break;
        default:
            echo "Invalid day\n";
    }
}
?>