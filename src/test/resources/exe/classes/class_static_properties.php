<?php
class Counter {
    public static int $count = 0;
    private static array $instances = [];
    
    public function __construct() {
        self::$count++;
        self::$instances[] = $this;
    }
    
    public static function getCount(): int {
        return self::$count;
    }
}

new Counter();
new Counter();
new Counter();
echo "Count: " . Counter::$count . "\n";
echo "Static method: " . Counter::getCount() . "\n";
?>