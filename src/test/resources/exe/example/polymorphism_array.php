<?php
interface Shape {
    public function area();
    public function perimeter();
}

class Circle implements Shape {
    private $radius;
    
    public function __construct($radius) {
        $this->radius = $radius;
    }
    
    public function area() {
        return pi() * $this->radius * $this->radius;
    }
    
    public function perimeter() {
        return 2 * pi() * $this->radius;
    }
}

class Rectangle implements Shape {
    private $width;
    private $height;
    
    public function __construct($width, $height) {
        $this->width = $width;
        $this->height = $height;
    }
    
    public function area() {
        return $this->width * $this->height;
    }
    
    public function perimeter() {
        return 2 * ($this->width + $this->height);
    }
}

class Triangle implements Shape {
    private $a, $b, $c;
    
    public function __construct($a, $b, $c) {
        $this->a = $a;
        $this->b = $b;
        $this->c = $c;
    }
    
    public function area() {
        // Формула Герона
        $p = $this->perimeter() / 2;
        return sqrt($p * ($p - $this->a) * ($p - $this->b) * ($p - $this->c));
    }
    
    public function perimeter() {
        return $this->a + $this->b + $this->c;
    }
}

// Чтение количества фигур
$n = (int)trim(fgets(STDIN));
$shapes = [];

// Чтение фигур
for ($i = 0; $i < $n; $i++) {
    $line = trim(fgets(STDIN));
    $parts = explode(' ', $line);
    
    $type = $parts[0];
    switch ($type) {
        case 'circle':
            $shapes[] = new Circle((float)$parts[1]);
            break;
        case 'rectangle':
            $shapes[] = new Rectangle((float)$parts[1], (float)$parts[2]);
            break;
        case 'triangle':
            $shapes[] = new Triangle((float)$parts[1], (float)$parts[2], (float)$parts[3]);
            break;
    }
}

// Вычисление и вывод результатов
$totalArea = 0;
$totalPerimeter = 0;

foreach ($shapes as $index => $shape) {
    $area = $shape->area();
    $perimeter = $shape->perimeter();
    
    echo "Фигура $index: Площадь = " . round($area, 2) . 
         ", Периметр = " . round($perimeter, 2) . "\n";
    
    $totalArea += $area;
    $totalPerimeter += $perimeter;
}

echo "\nОбщая площадь: " . round($totalArea, 2) . "\n";
echo "Общий периметр: " . round($totalPerimeter, 2);
?>