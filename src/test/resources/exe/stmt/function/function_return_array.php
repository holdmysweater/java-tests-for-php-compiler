<?php
function createArray(): array {
    return [1, 2, 3];
}

$arr = createArray();
echo implode(",", $arr) . "\n";
?>