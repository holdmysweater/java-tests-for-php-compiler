<?php
$fruits = ["apple", "banana", "cherry"];
foreach ($fruits as $fruit) {
    if ($fruit == "cherry") {
        break;
    }
    echo $fruit . "\n";
}
?>