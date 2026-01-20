<?php
echo "Start\n";
try {
    $result = 10 / 0;
    echo "This won't print\n";
} catch (Exception $e) {
    echo "Exception caught: " . $e->getMessage() . "\n";
}
echo "End\n";
?>