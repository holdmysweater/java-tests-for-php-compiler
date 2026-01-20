<?php
class Config {
    const VERSION = '1.0.0',
          MAX_SIZE = 1024,
          MIN_SIZE = 1;
    
    public static function getInfo(): string {
        return "Version: " . self::VERSION . ", Max: " . self::MAX_SIZE;
    }
}

echo Config::VERSION . "\n";
echo Config::getInfo() . "\n";
?>