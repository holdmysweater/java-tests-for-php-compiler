<?php
try {
    echo "Test\n";
} catch (Exception $e) {
    echo "First catch\n";
} catch (Exception $e) {
    echo "Duplicate catch\n";
}
?>