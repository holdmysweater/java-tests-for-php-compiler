<?php
final class FinalClass {
    public function method() {
        return "Final method";
    }
}

// Попытка наследовать финальный класс
class ChildClass extends FinalClass {
}
?>