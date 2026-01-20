<?php
class TestClass {
    public function __construct() {
        throw new Exception("Constructor error");
    }
}

echo "Start\n";
try {
    $obj = new TestClass();
} catch (Exception $e) {
    echo "Exception caught: " . $e->getMessage() . "\n";
}
echo "End\n";
?>