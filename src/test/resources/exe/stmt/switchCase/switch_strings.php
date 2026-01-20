<?php
$colors = ["red", "green", "blue", "yellow"];
foreach ($colors as $color) {
    switch ($color) {
        case "red":
            echo "Color is red\n";
            break;
        case "green":
            echo "Color is green\n";
            break;
        case "blue":
            echo "Color is blue\n";
            break;
        default:
            echo "Unknown color\n";
    }
}
?>