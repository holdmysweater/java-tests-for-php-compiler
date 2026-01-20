<?php
try {
    try {
        throw new Exception("Inner error");
    } finally {
        echo "Finally\n";
    }
} catch (InvalidArgumentException $e) {
    echo "Wrong exception type\n";
}
?>