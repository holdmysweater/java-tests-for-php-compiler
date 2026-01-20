<?php
function modifyByValue($value) {
    $value = $value * 2;
    return $value;
}

function modifyByReference(&$value) {
    $value = $value * 2;
}

class TestObject {
    public $value;
    
    public function __construct($val) {
        $this->value = $val;
    }
}

function modifyObject(TestObject $obj) {
    $obj->value = $obj->value * 2;
}

// Чтение ввода
$input = (int)trim(fgets(STDIN));

// Тест 1: передача по значению (примитивный тип)
$original = $input;
$result = modifyByValue($original);
echo "По значению - оригинал: $original, результат: $result\n";

// Тест 2: передача по ссылке
$original = $input;
modifyByReference($original);
echo "По ссылке - измененный: $original\n";

// Тест 3: передача объекта
$obj = new TestObject($input);
modifyObject($obj);
echo "Объект - значение: {$obj->value}\n";

// Тест 4: передача массива
$array = [$input, $input * 2];
function modifyArray($arr) {
    $arr[0] = 999;
}
modifyArray($array);
echo "Массив - первый элемент: {$array[0]}\n";

// Тест 5: передача массива по ссылке
function modifyArrayByReference(&$arr) {
    $arr[0] = 777;
}
modifyArrayByReference($array);
echo "Массив по ссылке - первый элемент: {$array[0]}";
?>