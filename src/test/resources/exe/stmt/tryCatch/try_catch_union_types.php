<?php
echo "Start\n";
try {
    throw new Exception("Some error");
} catch (InvalidArgumentException|RuntimeException|Exception $e) {
    echo "Exception caught: " . $e->getMessage() . "\n";
}
echo "End\n";
?>