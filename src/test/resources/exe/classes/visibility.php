<?php
class VisibilityDemo {
    public $public = "Public";
    protected $protected = "Protected";
    private $private = "Private";
    
    public function showAll() {
        return $this->public . "|" . $this->protected . "|" . $this->private;
    }
    
    protected function protectedMethod() {
        return "Protected method";
    }
    
    private function privateMethod() {
        return "Private method";
    }
}

class ExtendedVisibility extends VisibilityDemo {
    public function getProtected() {
        return $this->protected; // Доступно из наследника
    }
    
    public function tryGetPrivate() {
        // return $this->private; // Будет ошибка - недоступно
        return "Cannot access private";
    }
}

$obj = new VisibilityDemo();
echo $obj->public . "\n";
echo $obj->showAll() . "\n";

$ext = new ExtendedVisibility();
echo $ext->getProtected() . "\n";
echo $ext->tryGetPrivate() . "\n";
?>