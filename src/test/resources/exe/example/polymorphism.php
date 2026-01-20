<?php
class Animal {
    protected $name;
    
    public function __construct($name) {
        $this->name = $name;
    }
    
    public function makeSound() {
        return "Some sound";
    }
    
    public function getName() {
        return $this->name;
    }
}

class Dog extends Animal {
    public function makeSound() {
        return "Woof!";
    }
    
    public function wagTail() {
        return "{$this->name} is wagging tail";
    }
}

class Cat extends Animal {
    public function makeSound() {
        return "Meow!";
    }
    
    public function climbTree() {
        return "{$this->name} is climbing a tree";
    }
}

// Чтение ввода
$input = trim(fgets(STDIN));
$animalType = $input;

// Создание объекта в зависимости от ввода
if ($animalType === 'dog') {
    $animal = new Dog("Rex");
} elseif ($animalType === 'cat') {
    $animal = new Cat("Whiskers");
} else {
    $animal = new Animal("Unknown");
}

// Вызов методов
echo "Имя: " . $animal->getName() . "\n";
echo "Звук: " . $animal->makeSound() . "\n";

// Проверка типа
if ($animal instanceof Dog) {
    echo "Дополнительно: " . $animal->wagTail() . "\n";
} elseif ($animal instanceof Cat) {
    echo "Дополнительно: " . $animal->climbTree() . "\n";
}

// Присваивание дочернего объекта родительской ссылке
$parentRef = $animal;
echo "Через родительскую ссылку: " . $parentRef->makeSound();
?>