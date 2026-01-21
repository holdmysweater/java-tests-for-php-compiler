<?php
function getResult($type): string|int|array
{
    switch ($type) {
        case 'int':
            return 10;
        case 'string':
            return 'hello';
        case 'array':
            return [1, 2, 3];
    }
}

echo "Int: " . getResult('int') . "\n";
echo "String: " . getResult('string') . "\n";
print_r(getResult('array'));
?>

