<?php
class MathOperations {
    public static $pi = 3.14159;
    private static $counter = 0;
    
    public static function increment() {
        self::$counter++;
        return self::$counter;
    }
    
    public static function circleArea($radius) {
        return self::$pi * $radius * $radius;
    }
    
    public static function getCounter() {
        return self::$counter;
    }
}

echo "PI: " . MathOperations::$pi . "\n";
echo "Area of circle (r=2): " . MathOperations::circleArea(2) . "\n";
echo "Counter: " . MathOperations::increment() . "\n";
echo "Counter: " . MathOperations::increment() . "\n";
echo "Final Counter: " . MathOperations::getCounter() . "\n";
?>