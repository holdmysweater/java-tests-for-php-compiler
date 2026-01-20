<?php
class Test {
    public static $staticProp = "Static";
    
    public function method() {
        echo $this->staticProp; // Нельзя использовать $this для статического свойства
    }
}

$obj = new Test();
$obj->method();
?>