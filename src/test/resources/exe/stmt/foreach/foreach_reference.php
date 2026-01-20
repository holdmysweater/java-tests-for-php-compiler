<?php
$fruits = ["apple", "banana", "cherry"];
foreach ($fruits as &$fruit) {
    $fruit = strtoupper($fruit);
    echo $fruit . "\n";
}
echo gettype($fruits) . "\n";
?>