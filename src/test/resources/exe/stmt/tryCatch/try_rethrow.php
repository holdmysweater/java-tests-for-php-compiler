<?php
echo "Start\n";
try {
    try {
        throw new Exception("Original error");
    } catch (Exception $e) {
        echo "Caught and rethrowing\n";
        throw $e;
    }
} catch (Exception $e) {
    echo "Caught rethrown exception: " . $e->getMessage() . "\n";
}
?>