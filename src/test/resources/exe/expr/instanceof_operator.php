<?php
// Оператор instanceof
class MyClass {}
$obj = new MyClass();
echo ($obj instanceof MyClass) . "\n";

class ParentClass {}
class ChildClass extends ParentClass {}
$child = new ChildClass();
echo ($child instanceof ParentClass) . "\n";

echo ($obj instanceof stdClass) . "\n";
?>