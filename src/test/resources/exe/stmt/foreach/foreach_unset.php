<?php
$fruits = ["a" => "apple", "b" => "banana", "c" => "cherry"];
foreach ($fruits as $key => $fruit) {
    if ($key == "b") {
        unset($fruits[$key]);
    }
    echo $key . ": " . $fruit . "\n";
}
?>