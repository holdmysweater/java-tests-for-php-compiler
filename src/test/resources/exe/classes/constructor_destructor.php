<?php
class ConstructorDemo {
    private $name;
    public static $counter = 0;
    
    public function __construct($name) {
        $this->name = $name;
        self::$counter++;
        echo "Object created: " . $this->name . "\n";
    }
    
    public function __destruct() {
        echo "Object destroyed: " . $this->name . "\n";
    }
    
    public function getName() {
        return $this->name;
    }
}

$obj1 = new ConstructorDemo("First");
$obj2 = new ConstructorDemo("Second");
echo "Total objects: " . ConstructorDemo::$counter . "\n";
echo "Name: " . $obj1->getName() . "\n";
?>