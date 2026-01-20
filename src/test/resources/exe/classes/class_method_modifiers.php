<?php
class MethodTest {
    public function publicMethod() {
        return "Public Method";
    }
    
    protected function protectedMethod() {
        return "Protected Method";
    }
    
    private function privateMethod() {
        return "Private Method";
    }
    
    public static function staticPublicMethod() {
        return "Static Public Method";
    }
    
    protected static function staticProtectedMethod() {
        return "Static Protected Method";
    }
    
    private static function staticPrivateMethod() {
        return "Static Private Method";
    }
    
    public function callAll() {
        return $this->publicMethod() . "|" .
               $this->protectedMethod() . "|" .
               $this->privateMethod() . "|" .
               self::staticPublicMethod() . "|" .
               self::staticProtectedMethod() . "|" .
               self::staticPrivateMethod();
    }
}

$obj = new MethodTest();
echo $obj->publicMethod() . "\n";
echo MethodTest::staticPublicMethod() . "\n";
echo $obj->callAll() . "\n";
?>