package exe.expr;

import exe.utils.ExternalProcessDDT;
import exe.utils.ddt.ErrorCase;
import exe.utils.ddt.SuccessCase;

import java.util.stream.Stream;

class ExprTest extends ExternalProcessDDT {

    @Override
    protected String getName() {
        return "expr";
    }

    @Override
    protected Stream<SuccessCase> fileCases() {
        return Stream.of(
                // Арифметические операции
                new SuccessCase("arithmetic_basic.php",
                        "15\n" +
                                "5\n" +
                                "50\n" +
                                "2\n" +
                                "1\n"),

                new SuccessCase("arithmetic_complex.php",
                        "14\n" +
                                "20\n" +
                                "8\n" +
                                "4\n" +
                                "4\n"),

                // Логические операции
                new SuccessCase("logical_operators.php",
                        "1\n" +
                                "1\n" +
                                "1\n" +
                                "\n" +
                                "1\n"),

                new SuccessCase("bitwise_operators.php",
                        "312-2\n" +
                                "4\n"),

                // Операторы сравнения
                new SuccessCase("comparison_operators.php",
                        "1\n" +
                                "1\n" +
                                "1\n" +
                                "1\n" +
                                "1\n" +
                                "1\n" +
                                "1\n" +
                                "1\n"),

                new SuccessCase("identical_operators.php",
                        "1\n" +
                                "\n" +
                                "1\n" +
                                "\n" +
                                "1\n" +
                                "1\n"),

                // Тернарный оператор
                new SuccessCase("ternary_operator.php",
                        "greater\n" +
                                "less\n" +
                                "test\n" +
                                "default\n" +
                                "default\n"),

                // Операторы присваивания
                new SuccessCase("assignment_operators.php",
                        "10\n" +
                                "20\n" +
                                "10\n" +
                                "40\n" +
                                "2\n" +
                                "0\n" +
                                "100\n" +
                                "Hello World\n" +
                                "8\n" +
                                "2\n"),

                // Инкремент/декремент
                new SuccessCase("increment_decrement.php",
                        "1\n" +
                                "2\n" +
                                "2\n" +
                                "2\n" +
                                "5\n" +
                                "3\n"),

                // Унарные операторы
                new SuccessCase("unary_operators.php",
                        "\n" +
                                "1\n" +
                                "10\n" +
                                "-10\n" +
                                "-5\n"),

                // Массивы
                new SuccessCase("array_basic.php",
                        "apple\n" +
                                "banana\n"),

                new SuccessCase("array_operations.php",
                        "cherry\n" +
                                "3\n" +
                                "banana\n" +
                                "20\n"),

                // Оператор объединения с null
                new SuccessCase("null_coalescing.php",
                        "default\n" +
                                "value\n" +
                                "existing\n"),

                // Оператор spaceship
                new SuccessCase("spaceship_operator.php",
                        "-1\n" +
                                "0\n" +
                                "1\n" +
                                "-1\n" +
                                "0\n" +
                                "1\n"),

                // Оператор instanceof
                new SuccessCase("instanceof_operator.php",
                        "1\n" +
                                "1\n" +
                                "\n"),

                // Вызов функций
                new SuccessCase("function_calls.php",
                        "15\n" +
                                "6\n"),

                // Создание объектов
                new SuccessCase("object_creation.php",
                        "MyClass object\n" +
                                "10\n" +
                                "20\n"),

                // Приоритет операторов
                new SuccessCase("operator_precedence.php",
                        "17\n" +
                                "30\n" +
                                "0\n" +
                                "1\n" +
                                "Hello30\n"),

                // Смешанные выражения
                new SuccessCase("mixed_expressions.php",
                        "11\n" +
                                "16\n" +
                                "\n" +
                                "WorldHello\n")
        );
    }

    @Override
    protected Stream<ErrorCase> fileErrorCases() {
        return Stream.of(
                // Ошибки в выражениях
                new ErrorCase("error_division_by_zero.php"),
                new ErrorCase("error_undefined_function.php"),
                new ErrorCase("error_invalid_object_access.php"),
                new ErrorCase("error_missing_operand.php"),
                new ErrorCase("error_invalid_concat.php")
        );
    }
}