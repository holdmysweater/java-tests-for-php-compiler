<?php
class TestModifiers {
    public $publicProp = "Public";
    protected $protectedProp = "Protected";
    private $privateProp = "Private";
    
    public static $staticPublic = "Static Public";
    protected static $staticProtected = "Static Protected";
    private static $staticPrivate = "Static Private";
    
    public function showAll() {
        return $this->publicProp . "|" . 
               $this->protectedProp . "|" . 
               $this->privateProp . "|" . 
               self::$staticPublic . "|" . 
               self::$staticProtected . "|" . 
               self::$staticPrivate;
    }
}

$obj = new TestModifiers();
echo $obj->publicProp . "\n";
echo TestModifiers::$staticPublic . "\n";
echo $obj->showAll() . "\n";
?>