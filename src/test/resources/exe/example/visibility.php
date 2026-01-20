<?php
class VisibilityTest {
    public $public = "публичное";
    protected $protected = "защищенное";
    private $private = "приватное";
    
    public function getPublic() {
        return $this->public;
    }
    
    protected function getProtected() {
        return $this->protected;
    }
    
    private function getPrivate() {
        return $this->private;
    }
    
    public function showAll() {
        return "Внутри класса:\n" .
               "Публичное: " . $this->public . "\n" .
               "Защищенное: " . $this->protected . "\n" .
               "Приватное: " . $this->private . "\n" .
               "Публичный метод: " . $this->getPublic() . "\n" .
               "Защищенный метод: " . $this->getProtected() . "\n" .
               "Приватный метод: " . $this->getPrivate();
    }
}

class ChildVisibility extends VisibilityTest {
    public function testAccess() {
        $result = "В наследнике:\n";
        
        // Доступ к публичному
        $result .= "Публичное поле: " . $this->public . "\n";
        
        // Доступ к защищенному
        $result .= "Защищенное поле: " . $this->protected . "\n";
        
        // НЕТ доступа к приватному
        // $result .= "Приватное поле: " . $this->private . "\n"; // Ошибка!
        $result .= "Приватное поле: недоступно\n";
        
        // Вызов родительских методов
        $result .= "Вызов родительского защищенного метода: " . $this->getProtected() . "\n";
        
        return $result;
    }
}

// Чтение ввода (1 - родитель, 2 - наследник)
$choice = trim(fgets(STDIN));

if ($choice == '1') {
    $obj = new VisibilityTest();
    echo "=== Тест в родительском классе ===\n";
    echo $obj->showAll() . "\n";
    
    echo "\n=== Прямой доступ извне ===\n";
    echo "Публичное поле: " . $obj->public . "\n";
    echo "Публичный метод: " . $obj->getPublic() . "\n";
    
    // Попытка доступа к защищенному и приватному (должна вызвать ошибку)
    try {
        echo "Защищенное поле: " . @$obj->protected . "\n";
    } catch (Exception $e) {
        echo "Защищенное поле: ошибка доступа\n";
    }
    
    try {
        echo "Защищенный метод: " . @$obj->getProtected() . "\n";
    } catch (Exception $e) {
        echo "Защищенный метод: ошибка доступа\n";
    }
    
} else {
    $child = new ChildVisibility();
    echo "=== Тест в дочернем классе ===\n";
    echo $child->testAccess();
}
?>