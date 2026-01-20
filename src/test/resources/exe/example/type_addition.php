<?php
// Чтение строки ввода
$input = trim(fgets(STDIN));

// Разные типы значений
$types = [
    'string' => $input,
    'int' => (int)$input,
    'float' => (float)$input,
    'bool' => (bool)$input,
    'array' => [$input]
];

// Тестирование сложения
$results = [];

// Сложение с числом 10
foreach ($types as $type => $value) {
    try {
        $result = $value + 10;
        $results[$type] = $result;
    } catch (Exception $e) {
        $results[$type] = "Ошибка: " . $e->getMessage();
    }
}

// Сложение с строкой "10"
foreach ($types as $type => $value) {
    try {
        $result = $value . "10";
        $results[$type . "_concat"] = $result;
    } catch (Exception $e) {
        $results[$type . "_concat"] = "Ошибка: " . $e->getMessage();
    }
}

// Вывод результатов
foreach ($results as $type => $result) {
    echo "$type: $result\n";
}
?>