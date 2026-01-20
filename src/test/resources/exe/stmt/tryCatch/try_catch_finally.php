<?php
echo "Start\n";
try {
    throw new Exception("Something went wrong");
} catch (Exception $e) {
    echo "Exception caught: " . $e->getMessage() . "\n";
} finally {
    echo "Finally block executed\n";
}
echo "End\n";
?>