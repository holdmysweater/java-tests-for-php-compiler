package exe.stmt;

import exe.utils.ExternalProcessDDT;
import exe.utils.ddt.ErrorCase;
import exe.utils.ddt.SuccessCase;

import java.util.stream.Stream;

class ForTest extends ExternalProcessDDT {

    @Override
    protected String getName() {
        return "stmt/for";
    }

    @Override
    protected Stream<SuccessCase> fileCases() {
        return Stream.of(
                // Простой for
                new SuccessCase("simple_for.php",
                        "0\n1\n2\n"),

                // Альтернативный синтаксис
                new SuccessCase("for_alternative_syntax.php",
                        "0\n1\n2\n"),

                // For с пустыми частями
                new SuccessCase("for_with_empty_parts.php",
                        "0\n1\n2\n"),

                // For без инкремента
                new SuccessCase("for_without_increment.php",
                        "0\n1\n2\n"),

                // For без условия
                new SuccessCase("for_without_condition.php",
                        "0\n1\n2\n"),

                // For со всеми пустыми частями
                new SuccessCase("for_empty_all_parts.php",
                        "0\n1\n2\n"),

                // Несколько выражений
                new SuccessCase("for_multiple_expressions.php",
                        "i: 0, j: 10\ni: 1, j: 9\ni: 2, j: 8\n"),

                // For с break
                new SuccessCase("for_with_break.php",
                        "0\n1\n2\n"),

                // For с continue
                new SuccessCase("for_with_continue.php",
                        "0\n1\n3\n4\n"),

                // Вложенные for
                new SuccessCase("nested_for.php",
                        "Outer: 1\n  Inner: 1\n  Inner: 2\nOuter: 2\n  Inner: 1\n  Inner: 2\n"),

                // Сложный инкремент
                new SuccessCase("for_with_complex_increment.php",
                        "0\n2\n4\n6\n8\n"),

                // For с декрементом
                new SuccessCase("for_decrement.php",
                        "5\n4\n3\n2\n1\n"),

                // For с вызовом функции
                new SuccessCase("for_with_function_call.php",
                        "0\n1\n2\n"),

                // For с массивом
                new SuccessCase("for_with_array.php",
                        "10\n20\n30\n"),

                // Обратный цикл
                new SuccessCase("for_reverse.php",
                        "4\n3\n2\n1\n0\n"),

                // Условие в теле
                new SuccessCase("for_with_condition_in_body.php",
                        "0 is even\n1 is odd\n2 is even\n3 is odd\n4 is even\n" +
                                "5 is odd\n6 is even\n7 is odd\n8 is even\n9 is odd\n"),

                // Несколько переменных
                new SuccessCase("for_multiple_variables.php",
                        "x: 0, y: 0\nx: 1, y: 2\nx: 2, y: 4\n"),

                // Пустое тело
                new SuccessCase("for_with_empty_body.php",
                        "Loop executed\n"),

                // Альтернативный синтаксис, пустое тело
                new SuccessCase("for_with_alternative_empty.php",
                        "Loop executed\n"),

                // Вложенные break/continue
                new SuccessCase("for_with_break_continue_nested.php",
                        "Outer: 0\n  Inner: 0\nOuter: 1\n  Inner: 0\nOuter: 2\n  Inner: 0\n"),

                // Continue во внешнем цикле
                new SuccessCase("for_with_continue_outer.php",
                        "Outer: 0\n  Inner: 0\n  Inner: 1\nOuter: 2\n  Inner: 0\n  Inner: 1\n"),

                // Edge cases
                new SuccessCase("for_zero_iterations.php",
                        "Loop never executed\n"),

                new SuccessCase("for_single_iteration.php",
                        "0\n"),

                new SuccessCase("for_with_float.php",
                        "0.0\n1.0\n2.0\n"),

                // For с изменением переменной цикла в теле
                new SuccessCase("for_changing_counter.php",
                        "0\n2\n4\n")
        );
    }

    @Override
    protected Stream<ErrorCase> fileErrorCases() {
        return Stream.of(
                // Синтаксические ошибки в for
                new ErrorCase("error_for_missing_semicolon.php"),
                new ErrorCase("error_for_missing_paren.php"),
                new ErrorCase("error_for_missing_endfor.php"),
                new ErrorCase("error_for_extra_semicolon.php"),
                new ErrorCase("error_for_invalid_expression.php"),
                new ErrorCase("error_for_missing_colon.php"),
                new ErrorCase("error_endfor_without_for.php"),
                new ErrorCase("error_for_unexpected_semicolon.php")
        );
    }
}