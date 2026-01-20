<?php

class Animal
{
    protected $name;

    public function __construct($name)
    {
        $this->name = $name;
    }

    public function makeSound()
    {
        return "Some sound";
    }

    public function getName()
    {
        return $this->name;
    }
}

class Dog extends Animal
{
    public function makeSound()
    {
        return "Woof!";
    }

    public function wagTail()
    {
        return "{$this->name} is wagging tail";
    }
}

class Cat extends Animal
{
    public function makeSound()
    {
        return "Meow!";
    }

    public function climbTree()
    {
        return "{$this->name} is climbing a tree";
    }
}

// Reading input
$input = trim(fgets(STDIN));
$animalType = $input;

// Creating object based on input
if ($animalType === 'dog') {
    $animal = new Dog("Rex");
} elseif ($animalType === 'cat') {
    $animal = new Cat("Whiskers");
} else {
    $animal = new Animal("Unknown");
}

// Calling methods
echo "Name: " . $animal->getName() . "\n";
echo "Sound: " . $animal->makeSound() . "\n";

// Type checking
if ($animal instanceof Dog) {
    echo "Additionally: " . $animal->wagTail() . "\n";
} elseif ($animal instanceof Cat) {
    echo "Additionally: " . $animal->climbTree() . "\n";
}

// Assigning child object to parent reference
$parentRef = $animal;
echo "Through parent reference: " . $parentRef->makeSound();
?>