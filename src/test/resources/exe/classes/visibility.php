<?php

class VisibilityDemo
{
    public $first = "Public";
    protected $second = "Protected";
    private $third = "Private";

    public function showAll()
    {
        return $this->first . "|" . $this->second . "|" . $this->third;
    }

    protected function protectedMethod()
    {
        return "Protected method";
    }

    private function privateMethod()
    {
        return "Private method";
    }
}

class ExtendedVisibility extends VisibilityDemo
{
    public function getProtected()
    {
        return $this->second; // Доступно из наследника
    }

    public function tryGetPrivate()
    {
        // return $this->private; // Будет ошибка - недоступно
        return "Cannot access private";
    }
}

$obj = new VisibilityDemo();
echo $obj->first . "\n";
echo $obj->showAll() . "\n";

$ext = new ExtendedVisibility();
echo $ext->getProtected() . "\n";
echo $ext->tryGetPrivate() . "\n";
?>