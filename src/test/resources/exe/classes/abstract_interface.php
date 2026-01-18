<?php
interface AnimalInterface {
    public function makeSound();
    public function eat($food);
}

abstract class Animal implements AnimalInterface {
    protected $name;
    
    public function __construct($name) {
        $this->name = $name;
    }
    
    public function getName() {
        return $this->name;
    }
    
    abstract public function move();
}

class Dog extends Animal {
    public function makeSound() {
        return "Woof! Woof!";
    }
    
    public function eat($food) {
        return $this->name . " is eating " . $food;
    }
    
    public function move() {
        return $this->name . " is running";
    }
}

class Cat extends Animal {
    public function makeSound() {
        return "Meow!";
    }
    
    public function eat($food) {
        return $this->name . " is eating " . $food . " quietly";
    }
    
    public function move() {
        return $this->name . " is walking gracefully";
    }
}

$dog = new Dog("Rex");
$cat = new Cat("Whiskers");

echo $dog->getName() . ": " . $dog->makeSound() . "\n";
echo $cat->getName() . ": " . $cat->makeSound() . "\n";
echo $dog->eat("bone") . "\n";
echo $dog->move() . "\n";
?>