<?php
class Demo {
    public function __construct($required) {
        echo "Required: " . $required;
    }
}

// Вызов без обязательного аргумента
$obj = new Demo();
?>