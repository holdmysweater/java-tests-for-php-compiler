<?php
// Чтение типа теста
$testType = trim(fgets(STDIN));

switch ($testType) {
    case '1':
        // Обращение к несуществующей переменной
        echo $undefinedVariable;
        break;
        
    case '2':
        // Вызов несуществующей функции
        undefinedFunction();
        break;
        
    case '3':
        // Деление на ноль
        $result = 10 / 0;
        echo $result;
        break;
        
    case '4':
        // Доступ к приватному свойству
        class PrivateTest {
            private $secret = "секрет";
        }
        $obj = new PrivateTest();
        echo $obj->secret;
        break;
        
    case '5':
        // Вызов приватного метода
        class PrivateMethodTest {
            private function secretMethod() {
                return "секретный метод";
            }
        }
        $obj = new PrivateMethodTest();
        echo $obj->secretMethod();
        break;
        
    case '6':
        // Обращение к несуществующему индексу массива
        $array = [1, 2, 3];
        echo $array[10];
        break;
        
    case '7':
        // Использование необъявленной константы
        echo UNDEFINED_CONSTANT;
        break;
        
    case '8':
        // Некорректная операция с типами
        $result = "string" + 10;
        echo $result;
        break;
        
    case '9':
        // Рекурсия без условия выхода
        function infiniteRecursion() {
            infiniteRecursion();
        }
        infiniteRecursion();
        break;
        
    case '10':
        // Обращение к свойству не-объекта
        $notObject = "string";
        echo $notObject->property;
        break;
        
    default:
        echo "Неизвестный тест";
}
?>