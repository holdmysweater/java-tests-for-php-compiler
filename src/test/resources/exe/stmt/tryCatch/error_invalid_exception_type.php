<?php
try {
    throw new Exception("Error");
} catch (string $e) {
    echo "Invalid type\n";
}
?>