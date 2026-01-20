<?php
$values = [true, false, null];
foreach ($values as $val) {
    switch ($val) {
        case true:
            echo "True\n";
            break;
        case false:
            echo "False\n";
            break;
        default:
            echo "Not boolean\n";
    }
}
?>