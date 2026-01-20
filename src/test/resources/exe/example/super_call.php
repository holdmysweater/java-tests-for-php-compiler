<?php
class ParentClass {
    protected $value;
    
    public function __construct($value) {
        $this->value = $value;
    }
    
    public function calculate() {
        return $this->value * 2;
    }
    
    public function getInfo() {
        return "Родитель: " . $this->value;
    }
}

class ChildClass extends ParentClass {
    private $extra;
    
    public function __construct($value, $extra) {
        parent::__construct($value);
        $this->extra = $extra;
    }
    
    // Переопределение метода
    public function calculate() {
        // Вызов родительского метода
        $parentResult = parent::calculate();
        return $parentResult + $this->extra;
    }
    
    // Дополнительный вызов родительского метода
    public function getFullInfo() {
        $parentInfo = parent::getInfo();
        return $parentInfo . ", Дочерний: " . $this->extra;
    }
    
    // Метод без super вызова
    public function calculateDirectly() {
        return $this->value * 3;
    }
}

// Чтение ввода
$input = trim(fgets(STDIN));
list($value, $extra) = explode(' ', $input);

$child = new ChildClass((int)$value, (int)$extra);

echo "1. calculate() с super: " . $child->calculate() . "\n";
echo "2. getFullInfo() с super: " . $child->getFullInfo() . "\n";
echo "3. calculateDirectly() без super: " . $child->calculateDirectly() . "\n";
echo "4. Вызов родительского метода через child: " . $child->getInfo();
?>