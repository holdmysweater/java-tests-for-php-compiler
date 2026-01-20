<?php

interface Shape
{
    public function area();

    public function perimeter();
}

class Circle implements Shape
{
    private $radius;

    public function __construct($radius)
    {
        $this->radius = $radius;
    }

    public function area()
    {
        return 3.14159 * $this->radius * $this->radius;
    }

    public function perimeter()
    {
        return 2 * 3.14159 * $this->radius;
    }
}

class Rectangle implements Shape
{
    private $width;
    private $height;

    public function __construct($width, $height)
    {
        $this->width = $width;
        $this->height = $height;
    }

    public function area()
    {
        return $this->width * $this->height;
    }

    public function perimeter()
    {
        return 2 * ($this->width + $this->height);
    }
}

class Triangle implements Shape
{
    private $a, $b, $c;

    public function __construct($a, $b, $c)
    {
        $this->a = $a;
        $this->b = $b;
        $this->c = $c;
    }

    public function area()
    {
        // Heron's formula
        $p = $this->perimeter() / 2;
        $mult = $p * ($p - $this->a) * ($p - $this->b) * ($p - $this->c);
        $mult = $mult < 0 ? 0 : $mult;
        return sqrt($mult);
    }

    public function perimeter()
    {
        return $this->a + $this->b + $this->c;
    }
}

// Reading number of shapes
$n = trim(fgets(STDIN));
$shapes = [];

// Reading shapes
for ($i = 0; $i < $n; $i++) {
    $line = trim(fgets(STDIN));

    // Parse without explode
    $type = '';
    $numbers = [];
    $current = '';
    $index = 0;

    for ($j = 0; $j < strlen($line); $j++) {
        $char = $line[$j];
        if ($char == ' ') {
            if ($index == 0) {
                $type = $current;
            } else {
                $numbers[$index - 1] = $current + 0;
            }
            $current = '';
            $index++;
        } else {
            $current .= $char;
        }
    }

    if ($current != '') {
        if ($index == 0) {
            $type = $current;
        } else {
            $numbers[$index - 1] = $current + 0;
        }
    }

    if ($type == 'circle') {
        $shapes[] = new Circle($numbers[0] + 0);
    } else if ($type == 'rectangle') {
        $shapes[] = new Rectangle($numbers[0] + 0, $numbers[1] + 0);
    } else if ($type == 'triangle') {
        $shapes[] = new Triangle($numbers[0] + 0, $numbers[1] + 0, $numbers[2] + 0);
    }
}

// Calculating and outputting results
$totalArea = 0;
$totalPerimeter = 0;

foreach ($shapes as $index => $shape) {
    $area = $shape->area();
    $perimeter = $shape->perimeter();

    // Formatting without round()
    $areaStr = number_format($area, 2, '.', '');
    $perimeterStr = number_format($perimeter, 2, '.', '');

    echo "Shape $index: Area = " . $areaStr .
        ", Perimeter = " . $perimeterStr . "\n";

    $totalArea += $area;
    $totalPerimeter += $perimeter;
}

$totalAreaStr = number_format($totalArea, 2, '.', '');
$totalPerimeterStr = number_format($totalPerimeter, 2, '.', '');

echo "\nTotal area: " . $totalAreaStr . "\n";
echo "Total perimeter: " . $totalPerimeterStr;
?>