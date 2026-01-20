<?php
function greet($name = "Guest", $message = "welcome") {
    echo "Hello, $name, $message!\n";
}

greet();
greet("Alice");
greet("Bob", "welcome");
?>