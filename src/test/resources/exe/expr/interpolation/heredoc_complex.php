<?php
class User {
    public $name = 'Alice';
    public $age = 30;
    
    public function getInfo() {
        return "{$this->name} ({$this->age})";
    }
}

$user = new User();
$items = ['apple', 'banana', 'cherry'];

echo <<<REPORT
USER REPORT
===========
Name: {$user->name}
Age: {$user->age}
Info: {$user->getInfo()}
Favorite items: {$items[0]}, {$items[1]}, {$items[2]}
Total items: 3
REPORT;
?>