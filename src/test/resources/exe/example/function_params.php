<?php
function modifyByValue($value)
{
    $value = $value * 2;
    return $value;
}

function modifyByReference(&$value)
{
    $value = $value * 2;
}

class TestObject
{
    public $value;

    public function __construct($val)
    {
        $this->value = $val;
    }
}

function modifyObject(TestObject $obj)
{
    $obj->value = $obj->value * 2;
}

// Reading input
$input = trim(fgets(STDIN));

// Test 1: passing by value (primitive type)
$original = $input;
$result = modifyByValue($original);
echo "By value - original: $original, result: $result\n";

// Test 2: passing by reference
$original = $input;
modifyByReference($original);
echo "By reference - modified: $original\n";

// Test 3: passing object
$obj = new TestObject($input);
modifyObject($obj);
echo "Object - value: {$obj->value}\n";

// Test 4: passing array
$array = [$input, $input * 2];
function modifyArray($arr)
{
    $arr[0] = 999;
}

modifyArray($array);
echo "Array - first element: {$array[0]}\n";

// Test 5: passing array by reference
function modifyArrayByReference(&$arr)
{
    $arr[0] = 777;
}

modifyArrayByReference($array);
echo "Array by reference - first element: {$array[0]}";
?>