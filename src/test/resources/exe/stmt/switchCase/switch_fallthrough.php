<?php
$size = "M";
switch ($size) {
    case "S":
        echo "Small\n";
        // fallthrough
    case "M":
        echo "Medium\n";
        // fallthrough
    case "L":
        echo "Large\n";
        break;
    default:
        echo "Invalid size\n";
}
?>