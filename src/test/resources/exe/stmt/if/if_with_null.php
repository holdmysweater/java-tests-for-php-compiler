<?php
$var = null;

if ($var) {
    echo "This won't print\n";
} else {
    echo "Null is falsy\n";
}

if (isset($var)) {
    echo "This won't print\n";
} else {
    echo "isset returns false for null\n";
}
?>