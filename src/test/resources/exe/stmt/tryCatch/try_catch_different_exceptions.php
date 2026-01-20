<?php
echo "Start\n";
try {
    throw new RuntimeException("Runtime error");
} catch (LogicException $e) {
    echo "LogicException caught\n";
} catch (RuntimeException $e) {
    echo "RuntimeException caught: " . $e->getMessage() . "\n";
}
echo "End\n";
?>