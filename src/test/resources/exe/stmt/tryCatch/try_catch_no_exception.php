<?php
echo "Start\n";
try {
    echo "No exception thrown\n";
} catch (Exception $e) {
    echo "Exception caught\n";
}
echo "End\n";
?>