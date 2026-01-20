<?php
class MyClass {
    public $property1 = "value1";
    public $property2 = "value2";
}

$obj = new MyClass();
foreach ($obj as $key => $value) {
    echo $key . ": " . $value . "\n";
}
?>