<?php

class VisibilityTest
{
    public $public = "public";
    protected $protected = "protected";
    private $private = "private";

    public function getPublic()
    {
        return $this->public;
    }

    protected function getProtected()
    {
        return $this->protected;
    }

    private function getPrivate()
    {
        return $this->private;
    }

    public function showAll()
    {
        return "Inside class:\n" .
            "Public: " . $this->public . "\n" .
            "Protected: " . $this->protected . "\n" .
            "Private: " . $this->private . "\n" .
            "Public method: " . $this->getPublic() . "\n" .
            "Protected method: " . $this->getProtected() . "\n" .
            "Private method: " . $this->getPrivate();
    }
}

class ChildVisibility extends VisibilityTest
{
    public function testAccess()
    {
        $result = "In child class:\n";

        // Access to public
        $result .= "Public field: " . $this->public . "\n";

        // Access to protected
        $result .= "Protected field: " . $this->protected . "\n";

        // NO access to private
        // $result .= "Private field: " . $this->private . "\n"; // Error!
        $result .= "Private field: inaccessible\n";

        // Calling parent methods
        $result .= "Calling parent protected method: " . $this->getProtected() . "\n";

        return $result;
    }
}

// Reading input (1 - parent, 2 - child)
$choice = trim(fgets(STDIN));

if ($choice == '1') {
    $obj = new VisibilityTest();
    echo "=== Test in parent class ===\n";
    echo $obj->showAll() . "\n";

    echo "\n=== Direct access from outside ===\n";
    echo "Public field: " . $obj->public . "\n";
    echo "Public method: " . $obj->getPublic() . "\n";

    // Attempt to access protected and private (should cause error)
    try {
        echo "Protected field: " . @$obj->protected . "\n";
    } catch (Exception $e) {
        echo "Protected field: access error\n";
    }

    try {
        echo "Protected method: " . @$obj->getProtected() . "\n";
    } catch (Exception $e) {
        echo "Protected method: access error\n";
    }

} else {
    $child = new ChildVisibility();
    echo "=== Test in child class ===\n";
    echo $child->testAccess();
}
?>