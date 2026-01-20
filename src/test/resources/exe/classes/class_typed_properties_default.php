<?php
class Product {
    public string $name = "Unknown";
    public float $price = 0.0;
    public int $quantity = 0;
    
    public function getTotal(): float {
        return $this->price * $this->quantity;
    }
}

$product = new Product();
$product->name = "Book";
$product->price = 19.99;
$product->quantity = 3;

echo $product->name . ": " . $product->getTotal() . "\n";
?>