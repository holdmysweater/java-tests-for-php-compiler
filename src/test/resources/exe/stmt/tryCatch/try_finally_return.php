<?php
function test() {
    echo "Start\n";
    try {
        throw new Exception("Error in try");
    } finally {
        echo "Finally block\n";
        return "Finally executed";
    }
}

echo test() . "\n";
?>