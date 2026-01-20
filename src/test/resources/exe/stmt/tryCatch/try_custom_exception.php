<?php
class CustomException extends Exception {}

echo "Start\n";
try {
    throw new CustomException("My custom error");
} catch (CustomException $e) {
    echo "CustomException caught: " . $e->getMessage() . "\n";
}
echo "End\n";
?>