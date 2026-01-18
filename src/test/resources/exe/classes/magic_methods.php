<?php
class MagicDemo {
    private $data = array();
    
    public function __set($name, $value) {
        $this->data[$name] = $value;
    }
    
    public function __get($name) {
        return isset($this->data[$name]) ? $this->data[$name] : null;
    }
    
    public function __isset($name) {
        return isset($this->data[$name]);
    }
    
    public function __toString() {
        return "MagicDemo object with data: " . implode(", ", $this->data);
    }
    
    public function __call($name, $arguments) {
        return "Calling undefined method '$name' with arguments: " . implode(', ', $arguments);
    }
    
    public static function __callStatic($name, $arguments) {
        return "Calling undefined static method '$name' with arguments: " . implode(', ', $arguments);
    }
}

$obj = new MagicDemo();
$obj->name = "Test";
$obj->value = 123;

echo $obj->name . "\n";
echo $obj->value . "\n";
echo $obj . "\n";
echo $obj->undefinedMethod("arg1", "arg2") . "\n";
echo MagicDemo::undefinedStaticMethod("static_arg") . "\n";

if (isset($obj->name)) {
    echo "Property 'name' exists\n";
}
?>