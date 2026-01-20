<?php
echo "Outer start\n";
try {
    echo "Inner start\n";
    try {
        throw new Exception("Inner error");
    } catch (Exception $e) {
        echo "Inner exception caught: " . $e->getMessage() . "\n";
    } finally {
        echo "Inner finally\n";
    }
} catch (Exception $e) {
    echo "Outer exception caught\n";
}
echo "Outer end\n";
?>