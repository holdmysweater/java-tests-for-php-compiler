<?php
class SimpleClass {
    public $property = "Default value";
    
    public function getProperty() {
        return $this->property;
    }
    
    public function setProperty($value) {
        $this->property = $value;
    }
    
    public function sayHello() {
        echo "Hello from SimpleClass!";
    }
}

$obj = new SimpleClass();
$obj->sayHello();
?>