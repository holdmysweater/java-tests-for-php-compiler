<?php
class PrivateDemo {
    private $secret = "Secret data";
    
    private function secretMethod() {
        return "Secret method";
    }
}

$obj = new PrivateDemo();
echo $obj->secret; // Ошибка доступа к приватному свойству
?>