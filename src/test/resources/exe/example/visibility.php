<?php

class VisibilityTest
{
    public $first = "public";
    protected $second = "protected";
    private $third = "private";

    public function getFirst()
    {
        return $this->first;
    }

    protected function getSecond()
    {
        return $this->second;
    }

    private function getPrivate()
    {
        return $this->third;
    }

    public function showAll()
    {
        return "Inside class:\n" .
            "Public: " . $this->first . "\n" .
            "Protected: " . $this->second . "\n" .
            "Private: " . $this->third . "\n" .
            "Public method: " . $this->getFirst() . "\n" .
            "Protected method: " . $this->getSecond() . "\n" .
            "Private method: " . $this->getPrivate();
    }
}

class ChildVisibility extends VisibilityTest
{
    public function testAccess()
    {
        $result = "In child class:\n";

        // Access to public
        $result .= "Public field: " . $this->first . "\n";

        // Access to protected
        $result .= "Protected field: " . $this->second . "\n";

        // NO access to private
        // $result .= "Private field: " . $this->private . "\n"; // Error!
        $result .= "Private field: inaccessible\n";

        // Calling parent methods
        $result .= "Calling parent protected method: " . $this->getSecond() . "\n";

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
    echo "Public field: " . $obj->first . "\n";
    echo "Public method: " . $obj->getFirst() . "\n";

} else {
    $child = new ChildVisibility();
    echo "=== Test in child class ===\n";
    echo $child->testAccess();
}
?>