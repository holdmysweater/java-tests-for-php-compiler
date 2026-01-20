<?php
function riskyFunction() {
    throw new Exception("Function error");
}

echo "Before function\n";
try {
    riskyFunction();
    echo "This won't print\n";
} catch (Exception $e) {
    echo "Exception caught: " . $e->getMessage() . "\n";
}
echo "After function\n";
?>