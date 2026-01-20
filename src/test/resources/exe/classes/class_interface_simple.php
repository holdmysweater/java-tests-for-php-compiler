<?php
interface Logger {
    public function log(string $message): void;
}

class FileLogger implements Logger {
    public function log(string $message): void {
        echo "Logging to file: $message\n";
    }
}

$logger = new FileLogger();
$logger->log("Test message");
?>