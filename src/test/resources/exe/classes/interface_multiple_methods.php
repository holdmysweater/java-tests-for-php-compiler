<?php
interface Drawable {
    public function draw(): void;
    public function getArea(): float;
}

interface Scalable {
    public function scale(float $factor): void;
}

class Circle implements Drawable, Scalable {
    private float $radius;
    
    public function __construct(float $radius) {
        $this->radius = $radius;
    }
    
    public function draw(): void {
        echo "Drawing circle with radius: " . $this->radius . "\n";
    }
    
    public function getArea(): float {
        return 3.14159 * $this->radius * $this->radius;
    }
    
    public function scale(float $factor): void {
        $this->radius *= $factor;
        echo "Scaled to radius: " . $this->radius . "\n";
    }
}

$circle = new Circle(5);
$circle->draw();
echo "Area: " . $circle->getArea() . "\n";
$circle->scale(1.5);
?>