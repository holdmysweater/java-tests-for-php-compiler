<?php
class MathConstants {
    public const PI = 3.14159;
    protected const E = 2.71828;
    private const SECRET = 42;
    
    public function getConstants() {
        return self::PI . ", " . self::E . ", " . self::SECRET;
    }
}

$math = new MathConstants();
echo MathConstants::PI . "\n";
echo $math->getConstants() . "\n";
?>