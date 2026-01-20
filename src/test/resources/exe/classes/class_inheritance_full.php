<?php
class Animal {
    protected string $name;
    protected int $age;
    
    public function __construct(string $name, int $age) {
        $this->name = $name;
        $this->age = $age;
    }
    
    public function makeSound(): string {
        return "Some sound";
    }
    
    public function getInfo(): string {
        return $this->name . " is " . $this->age . " years old";
    }
}

class Dog extends Animal {
    private string $breed;
    
    public function __construct(string $name, int $age, string $breed) {
        parent::__construct($name, $age);
        $this->breed = $breed;
    }
    
    public function makeSound(): string {
        return "Woof! Woof!";
    }
    
    public function getFullInfo(): string {
        return parent::getInfo() . ", Breed: " . $this->breed;
    }
}

$dog = new Dog("Rex", 3, "Golden Retriever");
echo $dog->makeSound() . "\n";
echo $dog->getFullInfo() . "\n";
?>