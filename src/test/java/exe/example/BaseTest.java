package exe.example;

import exe.utils.ExternalProcessDDT;
import org.junit.jupiter.api.Test;

class BaseTest extends ExternalProcessDDT {

    @Override
    protected String getName() {
        return "example";
    }

    @Test
    void input() throws Exception {
        assertFileWithInput("input.php", "hello", "hello");
    }

    @Test
    void ifElseTest() throws Exception {
        assertFileWithInput("if_else.php", "12", "Больше 10");
        assertFileWithInput("if_else.php", "8", "Больше 5, но не больше 10");
        assertFileWithInput("if_else.php", "3", "5 или меньше");
        assertFileWithInput("if_else.php", "10", "5 или меньше");
    }

    @Test
    void loopsTest() throws Exception {
        assertFileWithInput("loops.php", "3", "For loop: 1 2 3 \nWhile loop: 1 2 3 \nDo-while: 1 2 3 ");
        assertFileWithInput("loops.php", "1", "For loop: 1 \nWhile loop: 1 \nDo-while: 1 ");
        assertFileWithInput("loops.php", "0", "For loop: \nWhile loop: \nDo-while: 1 ");
    }

    @Test
    void functionParamsTest() throws Exception {
        assertFileWithInput("function_params.php", "5",
                "По значению - оригинал: 5, результат: 10\n" +
                        "По ссылке - измененный: 10\n" +
                        "Объект - значение: 10\n" +
                        "Массив - первый элемент: 5\n" +
                        "Массив по ссылке - первый элемент: 777");
    }

    @Test
    void bubbleSortTest() throws Exception {
        String input = "5\n3\n1\n4\n2\n5";
        String expected = "Исходный массив: 3 1 4 2 5\n" +
                "Отсортированный массив: 1 2 3 4 5";
        assertFileWithInput("bubble_sort.php", input, expected);
    }

    @Test
    void bubbleSortEmptyTest() throws Exception {
        String input = "0";
        String expected = "Исходный массив: \n" +
                "Отсортированный массив: ";
        assertFileWithInput("bubble_sort.php", input, expected);
    }

    @Test
    void bubbleSortSingleTest() throws Exception {
        String input = "1\n42";
        String expected = "Исходный массив: 42\n" +
                "Отсортированный массив: 42";
        assertFileWithInput("bubble_sort.php", input, expected);
    }

    @Test
    void floodFillTest() throws Exception {
        String input = "5 5\n" +
                "00000\n" +
                "01110\n" +
                "01010\n" +
                "01110\n" +
                "00000\n" +
                "2 2 2";

        String expected = "Исходная матрица:\n" +
                "00000\n" +
                "01110\n" +
                "01010\n" +
                "01110\n" +
                "00000\n" +
                "\n" +
                "Матрица после заливки:\n" +
                "00000\n" +
                "02220\n" +
                "02020\n" +
                "02220\n" +
                "00000\n";

        assertFileWithInput("flood_fill.php", input, expected);
    }

    @Test
    void polymorphismDogTest() throws Exception {
        assertFileWithInput("polymorphism.php", "dog",
                "Имя: Rex\n" +
                        "Звук: Woof!\n" +
                        "Дополнительно: Rex is wagging tail\n" +
                        "Через родительскую ссылку: Woof!");
    }

    @Test
    void polymorphismCatTest() throws Exception {
        assertFileWithInput("polymorphism.php", "cat",
                "Имя: Whiskers\n" +
                        "Звук: Meow!\n" +
                        "Дополнительно: Whiskers is climbing a tree\n" +
                        "Через родительскую ссылку: Meow!");
    }

    @Test
    void polymorphismUnknownTest() throws Exception {
        assertFileWithInput("polymorphism.php", "bird",
                "Имя: Unknown\n" +
                        "Звук: Some sound\n" +
                        "Через родительскую ссылку: Some sound");
    }

    @Test
    void superCallTest() throws Exception {
        assertFileWithInput("super_call.php", "10 5",
                "1. calculate() с super: 25\n" +
                        "2. getFullInfo() с super: Родитель: 10, Дочерний: 5\n" +
                        "3. calculateDirectly() без super: 30\n" +
                        "4. Вызов родительского метода через child: Родитель: 10");
    }


    // Дополнительные тесты для проверки полиморфизма с массивом
    @Test
    void polymorphismArrayExactTest() throws Exception {
        String input = "3\n" +
                "circle 5\n" +
                "rectangle 4 6\n" +
                "triangle 3 4 5";

        String expected = "Фигура 0: Площадь = 78.54, Периметр = 31.42\n" +
                "Фигура 1: Площадь = 24, Периметр = 20\n" +
                "Фигура 2: Площадь = 6, Периметр = 12\n" +
                "\n" +
                "Общая площадь: 108.54\n" +
                "Общий периметр: 63.42";

        assertFileWithInput("polymorphism_array.php", input, expected);
    }

    @Test
    void polymorphismArraySimpleTest() throws Exception {
        String input = "1\ncircle 3";
        String expected = "Фигура 0: Площадь = 28.27, Периметр = 18.85\n" +
                "\n" +
                "Общая площадь: 28.27\n" +
                "Общий периметр: 18.85";
        assertFileWithInput("polymorphism_array.php", input, expected);
    }

    @Test
    void visibilityExactTest() throws Exception {
        // Проверяем точный вывод для родительского класса
        String parentExpected = "=== Тест в родительском классе ===\n" +
                "Внутри класса:\n" +
                "Публичное: публичное\n" +
                "Защищенное: защищенное\n" +
                "Приватное: приватное\n" +
                "Публичный метод: публичное\n" +
                "Защищенный метод: защищенное\n" +
                "Приватный метод: приватное\n" +
                "\n" +
                "=== Прямой доступ извне ===\n" +
                "Публичное поле: публичное\n" +
                "Публичный метод: публичное\n" +
                "Защищенное поле: ошибка доступа\n" +
                "Защищенный метод: ошибка доступа";

        // Проверяем точный вывод для дочернего класса
        String childExpected = "=== Тест в дочернем классе ===\n" +
                "В наследнике:\n" +
                "Публичное поле: публичное\n" +
                "Защищенное поле: защищенное\n" +
                "Приватное поле: недоступно\n" +
                "Вызов родительского защищенного метода: защищенное";

        assertFileWithInput("visibility.php", "1", parentExpected);
        assertFileWithInput("visibility.php", "2", childExpected);
    }

    @Test
    void typeAdditionExactTest() throws Exception {
        // Для типа "5" - числовой ввод
        String input5 = "5";
        // Точный ожидаемый вывод зависит от реализации вашего PHP интерпретатора
        // Здесь предполагается стандартное поведение PHP

        assertFileWithInput("type_addition.php", "5",
                "string: 15\n" +
                        "int: 15\n" +
                        "float: 15\n" +
                        "bool: 11\n" +
                        "string_concat: 510\n" +
                        "int_concat: 510\n" +
                        "float_concat: 510\n" +
                        "bool_concat: 110\n" +
                        "array_concat: Array10");

        // Для строкового ввода "hello"
        assertFileWithInput("type_addition.php", "hello",
                "string: 10\n" +
                        "int: 10\n" +
                        "float: 10\n" +
                        "bool: 11\n" +
                        "string_concat: hello10\n" +
                        "int_concat: 010\n" +
                        "float_concat: 010\n" +
                        "bool_concat: 110\n" +
                        "array_concat: Array10");
    }
}