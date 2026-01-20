<?php
class Test {
    private $prop = "Private";
}

$obj = new Test();
echo $obj->prop; // Нельзя получить доступ к private свойству извне
?>