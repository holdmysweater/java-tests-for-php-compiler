<?php
class User {
    public string $name;
    private int $age;
    protected array $data = [];
    
    public function __construct(string $name, int $age) {
        $this->name = $name;
        $this->age = $age;
    }
    
    public function getInfo(): string {
        return $this->name . " is " . $this->age . " years old";
    }
}

$user = new User("John", 25);
echo $user->getInfo() . "\n";
?>