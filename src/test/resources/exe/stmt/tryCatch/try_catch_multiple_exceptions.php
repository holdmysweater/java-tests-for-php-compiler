<?php
echo "Start\n";
try {
    throw new InvalidArgumentException("Invalid argument");
} catch (InvalidArgumentException|RuntimeException $e) {
    echo "Caught Exception: " . $e->getMessage() . "\n";
}
echo "End\n";
?>