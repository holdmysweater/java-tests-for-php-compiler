<?php
class A {
    public $propA = "A";
    
    public function methodA() {
        return "Method A";
    }
}

class B extends A {
    protected $propB = "B";
    
    public function methodB() {
        return "Method B: " . $this->propA;
    }
}

class C extends B {
    private $propC = "C";
    
    public function methodC() {
        return "Method C: " . $this->propB . ", " . parent::methodA();
    }
}

$c = new C();
echo $c->methodA() . "\n";
echo $c->methodB() . "\n";
echo $c->methodC() . "\n";
?>