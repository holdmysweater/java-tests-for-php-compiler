<?php
$fruits = ["apple", "banana", "cherry"];
foreach ($fruits as $fruit) {
    if ($fruit == "banana") {
        continue;
    }
    echo $fruit . "\n";
}
?>