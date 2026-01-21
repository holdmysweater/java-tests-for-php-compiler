package exe.stmt;

import exe.utils.ExternalProcessDDT;
import exe.utils.ddt.ErrorCase;
import exe.utils.ddt.SuccessCase;

import java.util.stream.Stream;

class EchoTest extends ExternalProcessDDT {

    @Override
    protected Stream<SuccessCase> echoCases() {
        return Stream.of(
                // Базовые типы данных
                new SuccessCase("123", "123"),
                new SuccessCase("'hi'", "hi"),
                new SuccessCase("\"hello\"", "hello"),

                // Числа и арифметика
                new SuccessCase("10 + 15", "25"),
                new SuccessCase("10 - 5", "5"),
                new SuccessCase("10 * 2", "20"),
                new SuccessCase("10 / 2", "5"),
                new SuccessCase("10 % 3", "1"),
                new SuccessCase("-10", "-10"),
                new SuccessCase("+10", "10"),

                // Строки и конкатенация
                new SuccessCase("10 . 15", "1015"),
                new SuccessCase("'Hello' . ' World'", "Hello World"),
                new SuccessCase("'Number: ' . 42", "Number: 42"),

                // Булевы значения
                new SuccessCase("true", "1"),
                new SuccessCase("false", ""),
                new SuccessCase("!true", ""),
                new SuccessCase("!false", "1"),

                // Сравнения
                new SuccessCase("5 == 5", "1"),
                new SuccessCase("5 != 4", "1"),
                new SuccessCase("5 > 3", "1"),
                new SuccessCase("3 < 5", "1"),

                // Приоритет операторов
                new SuccessCase("2 + 3 * 4", "14"),
                new SuccessCase("(2 + 3) * 4", "20"),
                new SuccessCase("1 + 2 . 3", "33"), // (1+2).3 = 3.3 = "33"

                // Специальные значения
                new SuccessCase("null", ""),

                // Несколько выражений через запятую
                new SuccessCase("1, 2, 3", "123"),
                new SuccessCase("'a', 'b', 'c'", "abc"),
                new SuccessCase("1+2, 3+4", "37"),

                // Пробелы и форматирование
                new SuccessCase("  'trimmed'  ", "trimmed"),
                new SuccessCase("\t'tabbed'\t", "tabbed"),
                new SuccessCase("\n'newline'\n", "newline"),

                // Escape-последовательности в строках
                new SuccessCase("'It\\'s working'", "It's working"),
                new SuccessCase("\"Quote: \\\"test\\\"\"", "Quote: \"test\""),

                // Сложные выражения
                new SuccessCase("(5 + 3) * 2 . ' items'", "16 items"),
                new SuccessCase("true ? 'yes' : 'no'", "yes"),
                new SuccessCase("false ? 'yes' : 'no'", "no"),

                // Граничные значения
                new SuccessCase("0", "0"),
                new SuccessCase("''", ""),
                new SuccessCase("'0'", "0"),

                // Смешанные типы
                new SuccessCase("'Total: ' . (10 + 20)", "Total: 30"),

                // Работа с вложенными выражениями
                new SuccessCase("(1 + (2 + (3 + 4)))", "10"),
                new SuccessCase("'a' . ('b' . ('c' . 'd'))", "abcd"),

                // Логические операции
                new SuccessCase("true && true", "1"),
                new SuccessCase("true && false", ""),
                new SuccessCase("false || true", "1"),
                new SuccessCase("false || false", ""),

                // Тернарный оператор
                new SuccessCase("5 > 3 ? 'greater' : 'less'", "greater"),
                new SuccessCase("0 ? 'truthy' : 'falsy'", "falsy"),

                // Приведение типов
                new SuccessCase("'5' + 3", "8"), // string '5' приводится к int 5
                new SuccessCase("5 + '3'", "8"),
                new SuccessCase("'5' . 3", "53")
        );
    }

    @Override
    protected Stream<ErrorCase> echoErrorCases() {
        return Stream.of(
                // Ошибки типов данных
                new ErrorCase("324 + 'hi'"),
                new ErrorCase("'hello' - 'world'"),
                new ErrorCase("'string' * 5"),
                new ErrorCase("'text' / 2"),
                new ErrorCase("'abc' % 2"),

                // Математические ошибки
                new ErrorCase("10 / 0"),
                new ErrorCase("5 % 0"),

                // Неопределенные переменные
                new ErrorCase("echo $unknown"),

                // Синтаксические ошибки
                new ErrorCase("10 +"),
                new ErrorCase("'unclosed string"),
                new ErrorCase("\"unclosed double quote"),
                new ErrorCase("10 ."),
                new ErrorCase("+"),
                new ErrorCase("()"),
                new ErrorCase(")"),

                // Некорректные выражения с запятыми
                new ErrorCase(","),
                new ErrorCase("1,"),
                new ErrorCase(",2"),

                // Сложные ошибки
                new ErrorCase("10 + '5 apples'"),
                new ErrorCase("'Price: $' + 100"),

                // Смешанные ошибки
                new ErrorCase("$var + 'text'"),
                new ErrorCase("undefined_function()"),

                // Ошибки в тернарном операторе
                new ErrorCase("true ?"),
                new ErrorCase("? 'yes' : 'no'"),
                new ErrorCase("true ? 'yes'"),

                // Некорректные вложенные выражения
                new ErrorCase("((((("),
                new ErrorCase("))))"),
                new ErrorCase("()()()")
        );
    }

}
