<?php
// Тестирование edge cases интерполяции
class A {
    public $b;
    
    public function __construct() {
        $this->b = new B();
    }
}

class B {
    public $c = 'value';
    
    public function getC() {
        return $this->c;
    }
    
    public static function staticGet() {
        return 'static';
    }
}

$a = new A();
$index = 0;
$array = [new B(), new B()];
$array[0]->c = 'first';
$array[1]->c = 'second';

echo "Test 1 (chained): {$a->b->c}\n";
echo "Test 2 (method call): {$a->b->getC()}\n";
echo "Test 3 (static): {B::staticGet()}\n";
echo "Test 4 (array with variable index): {$array[$index]->c}\n";
echo "Test 5 (nested array): {['a' => ['b' => 'c']]['a']['b']}\n";
echo "Test 6 (ternary): {$index == 0 ? 'zero' : 'not zero'}\n";
echo "Test 7 (increment): " . ++$index . "\n";
echo "Test 8 (function call): {strtoupper('hello')}\n";

// Специальный случай: $$var переменные
$varName = 'dynamic';
$$varName = 'dynamic value';
echo "Test 9 (variable variable): {$$varName}\n";

// HEREDOC с escape-последовательностями
echo <<<HTML
<div class="container">
    <h1>{$a->b->c}</h1>
    <p>Price: \${$array[0]->c}</p>
    <p>Escaped: \\\$not_a_variable</p>
</div>
HTML;
?>