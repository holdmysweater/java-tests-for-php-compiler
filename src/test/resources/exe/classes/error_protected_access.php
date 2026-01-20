<?php
class Test {
    protected $prop = "Protected";
}

$obj = new Test();
echo $obj->prop; // Нельзя получить доступ к protected свойству извне
?>