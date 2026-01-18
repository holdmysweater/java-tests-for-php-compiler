<?php
class ParentClass {
    protected $parentProperty = "Parent value";
    
    public function parentMethod() {
        return "Parent method called";
    }
    
    public function commonMethod() {
        return "From Parent";
    }
}

class ChildClass extends ParentClass {
    private $childProperty = "Child value";
    
    public function getParentProperty() {
        return $this->parentProperty;
    }
    
    public function getChildProperty() {
        return $this->childProperty;
    }
    
    public function commonMethod() {
        return "From Child (overridden)";
    }
    
    public function callParentMethod() {
        return parent::commonMethod();
    }
}

$child = new ChildClass();
echo $child->parentMethod() . "\n";
echo $child->getParentProperty() . "\n";
echo $child->commonMethod() . "\n";
echo $child->callParentMethod() . "\n";
?>