<?php
function evaluate($value) {
    switch ($value) {
        case "high":
            return "Result: High";
        case "medium":
            return "Result: Medium";
        case "low":
            return "Result: Low";
    }
}

echo evaluate("high") . "\n";
echo evaluate("medium") . "\n";
echo evaluate("low") . "\n";
?>