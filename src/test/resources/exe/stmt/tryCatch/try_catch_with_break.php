<?php
for ($i = 1; $i <= 3; $i++) {
    echo "Iteration $i\n";
    try {
        if ($i == 2) {
            throw new Exception("Break error");
        }
    } catch (Exception $e) {
        echo "Exception caught: " . $e->getMessage() . "\n";
        break;
    }
}
echo "Loop ended\n";
?>