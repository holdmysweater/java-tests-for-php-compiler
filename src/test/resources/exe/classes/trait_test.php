<?php
trait Logger {
    public function log($message) {
        echo date('Y-m-d H:i:s') . ": " . $message . "\n";
    }
}

trait Formatter {
    public function format($text) {
        return "[" . strtoupper($text) . "]";
    }
}

class Application {
    use Logger, Formatter;
    
    public function run() {
        $this->log("Application started");
        echo $this->format("hello world") . "\n";
        $this->log("Application finished");
    }
}

$app = new Application();
$app->run();
?>