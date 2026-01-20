<?php
// Создание объектов
class MyClass {
    public $property = "MyClass object";
    
    public function getValue() {
        return 10;
    }
}

$obj = new MyClass();
echo $obj->property . "\n";

$value = $obj->getValue();
echo $value . "\n";
echo $value * 2 . "\n";
?>