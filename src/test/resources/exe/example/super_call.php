<?php

class ParentClass
{
    protected $value;

    public function __construct($value)
    {
        $this->value = $value;
    }

    public function calculate()
    {
        return $this->value * 2;
    }

    public function getInfo()
    {
        return "Parent: " . $this->value;
    }
}

class ChildClass extends ParentClass
{
    private $extra;

    public function __construct($value, $extra)
    {
        parent::__construct($value);
        $this->extra = $extra;
    }

    // Переопределение метода
    public function calculate()
    {
        // Вызов родительского метода
        $parentResult = parent::calculate();
        return $parentResult + $this->extra;
    }

    // Дополнительный вызов родительского метода
    public function getFullInfo()
    {
        $parentInfo = parent::getInfo();
        return $parentInfo . "Child: " . $this->extra;
    }

    // Метод без super вызова
    public function calculateDirectly()
    {
        return $this->value * 3;
    }
}

// Чтение ввода
$value = fgets(STDIN);
$extra = fgets(STDIN);

$child = new ChildClass($value, $extra);

echo "1. calculate() with super: " . $child->calculate() . "\n";
echo "2. getFullInfo() with super: " . $child->getFullInfo() . "\n";
echo "3. calculateDirectly() without super: " . $child->calculateDirectly() . "\n";
echo "4. Calling parent method through child: " . $child->getInfo();
?>