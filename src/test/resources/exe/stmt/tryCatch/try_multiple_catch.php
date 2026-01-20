<?php
echo "Start\n";
try {
    throw new InvalidArgumentException("Invalid argument");
} catch (LogicException $e) {
    echo "LogicException caught\n";
} catch (InvalidArgumentException $e) {
    echo "InvalidArgumentException caught: " . $e->getMessage() . "\n";
} finally {
    echo "Finally block executed\n";
}
echo "End\n";
?>