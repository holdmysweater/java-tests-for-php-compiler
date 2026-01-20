<?php
$data = [
    "red" => ["apple", "strawberry"],
    "yellow" => ["banana", "lemon"]
];

foreach ($data as $color => $items) {
    echo "Outer: $color\n";
    foreach ($items as $item) {
        echo "  Inner: $item\n";
    }
}
?>