<?php
class Calculator {
    public function add(int $a, int $b): int {
        return $a + $b;
    }
}

$calc = new Calculator();
echo $calc->add("string", 5); // Ошибка типов
?>