<?php
class Product {
    public $name;
    public $price;
    public $category;
    
    public function __construct($name, $price, $category) {
        $this->name = $name;
        $this->price = $price;
        $this->category = $category;
    }
    
    public function getFormattedPrice() {
        return number_format($this->price, 2);
    }
}

$products = [
    new Product('Laptop', 1299.99, 'Electronics'),
    new Product('Coffee Mug', 12.50, 'Kitchen'),
    new Product('Book', 24.99, 'Education')
];

$total = array_sum(array_map(fn($p) => $p->price, $products));

echo <<<CATALOG
PRODUCT CATALOG
===============

PRODUCT LIST:
-------------
<?php foreach ($products as $i => $product): ?>
{$i + 1}. {$product->name}
    Category: {$product->category}
    Price: \${$product->getFormattedPrice()}
    
<?php endforeach; ?>

SUMMARY:
--------
Total products: <?php echo count($products); ?>
Total value: \$<?php echo number_format($total, 2); ?>

Generated: <?php echo date('Y-m-d H:i:s'); ?>

CATALOG;
?>