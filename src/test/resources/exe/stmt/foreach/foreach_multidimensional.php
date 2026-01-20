<?php
$items = [
    ["Fruit" => "apple", "Color" => "red"],
    ["Fruit" => "banana", "Color" => "yellow"]
];

foreach ($items as $item) {
    echo "Fruit: " . $item["Fruit"] . ", Color: " . $item["Color"] . "\n";
}
?>