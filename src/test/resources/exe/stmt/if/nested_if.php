<?php
$a = true;
$b = false;

if ($a) {
    if ($b) {
        echo "Both true\n";
    } else {
        echo "a true, b false\n";
    }
} else {
    echo "a false\n";
}
?>