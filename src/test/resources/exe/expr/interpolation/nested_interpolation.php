<?php

class Department
{
    public $name;
    public $employees = [];

    public function __construct($name)
    {
        $this->name = $name;
    }

    public function addEmployee($emp)
    {
        $this->employees[] = $emp;
    }

    public function getReport()
    {
        $count = count($this->employees);
        return "Department: {$this->name}, Employees: {$count}";
    }
}

class Employee
{
    public $name;
    public $salary;

    public function __construct($name, $salary)
    {
        $this->name = $name;
        $this->salary = $salary;
    }
}

$dept = new Department('IT');
$dept->addEmployee(new Employee('John', 50000));
$dept->addEmployee(new Employee('Jane', 55000));

echo <<<REPORT
DEPARTMENT REPORT
=================

Employee details:
1. Name: {$dept->employees[0]->name}, Salary: \${$dept->employees[0]->salary}
2. Name: {$dept->employees[1]->name}, Salary: \${$dept->employees[1]->salary}
REPORT;
?>