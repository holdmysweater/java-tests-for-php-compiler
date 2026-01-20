<?php
// Reading test type
$testType = trim(fgets(STDIN));

switch ($testType) {
    case '1':
        // Accessing undefined variable
        echo $undefinedVariable;
        break;

    case '2':
        // Calling undefined function
        undefinedFunction();
        break;

    case '3':
        // Division by zero
        $result = 10 / 0;
        echo $result;
        break;

    case '4':
        // Accessing private property
        class PrivateTest
        {
            private $secret = "secret";
        }

        $obj = new PrivateTest();
        echo $obj->secret;
        break;

    case '5':
        // Calling private method
        class PrivateMethodTest
        {
            private function secretMethod()
            {
                return "secret method";
            }
        }

        $obj = new PrivateMethodTest();
        echo $obj->secretMethod();
        break;

    case '6':
        // Accessing non-existent array index
        $array = [1, 2, 3];
        echo $array[10];
        break;

    case '7':
        // Using undefined constant
        echo UNDEFINED_CONSTANT;
        break;

    case '8':
        // Incorrect type operation
        $result = "string" + 10;
        echo $result;
        break;

    case '9':
        // Recursion without exit condition
        function infiniteRecursion()
        {
            infiniteRecursion();
        }

        infiniteRecursion();
        break;

    case '10':
        // Accessing property of non-object
        $notObject = "string";
        echo $notObject->property;
        break;

    default:
        echo "Unknown test";
}
?>