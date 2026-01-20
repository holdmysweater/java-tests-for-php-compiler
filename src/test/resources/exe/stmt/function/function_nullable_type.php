<?php
function getValue(?int $val): ?string {
    if ($val === null) {
        return null;
    }
    return "Value: " . $val;
}

echo getValue(10) . "\n";
echo getValue(null) . "\n";
?>