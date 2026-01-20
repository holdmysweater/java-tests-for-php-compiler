<?php
$str = "hello";

if ($str == "hello") {
    echo "String comparison works\n";
}

if ($str != "world") {
    echo "String inequality works\n";
}

if (strlen($str) > 0) {
    echo "String has length\n";
}
?>