<?php
class EmptyClass {
    // No properties, only methods
    public function sayHello(): void {
        echo "Hello from empty class!\n";
    }
}

$empty = new EmptyClass();
$empty->sayHello();
?>