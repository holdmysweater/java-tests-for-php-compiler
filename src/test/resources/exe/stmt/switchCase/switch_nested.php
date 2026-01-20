<?php
$outer = 1;
$inner = "B";

switch ($outer) {
    case 1:
        echo "Outer: 1\n";
        switch ($inner) {
            case "A":
                echo "  Inner: A\n";
                break;
            case "B":
                echo "  Inner: B\n";
                break;
        }
        break;
    case 2:
        echo "Outer: 2\n";
        switch ($inner) {
            case "C":
                echo "  Inner: C\n";
                break;
        }
        break;
}
?>