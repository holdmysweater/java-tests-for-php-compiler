package exe.stmt;

import exe.utils.ExternalProcessDDT;
import exe.utils.ddt.ErrorCase;
import exe.utils.ddt.SuccessCase;

import java.util.stream.Stream;

class WhileTest extends ExternalProcessDDT {

    @Override
    protected String getName() {
        return "stmt/while";
    }


    @Override
    protected Stream<SuccessCase> fileCases() {
        return Stream.of(
                // Обычный while
                new SuccessCase("simple_while.php",
                        "0\n1\n2\n"),

                // Альтернативный синтаксис
                new SuccessCase("while_alternative_syntax.php",
                        "0\n1\n2\n"),

                // Цикл do-while
                new SuccessCase("do_while_simple.php",
                        "0\n1\n2\n"),

                // While с break
                new SuccessCase("while_with_break.php",
                        "0\n1\n2\n"),

                // Do-while с break
                new SuccessCase("do_while_with_break.php",
                        "0\n1\n2\n"),

                // While с continue
                new SuccessCase("while_with_continue.php",
                        "1\n2\n4\n5\n"),

                // Do-while с continue
                new SuccessCase("do_while_with_continue.php",
                        "1\n2\n4\n5\n"),

                // Вложенные while
                new SuccessCase("nested_while.php",
                        "Outer: 1\n  Inner: 1\n  Inner: 2\nOuter: 2\n  Inner: 1\n  Inner: 2\n"),

                // Вложенные do-while
                new SuccessCase("nested_do_while.php",
                        "Outer: 1\n  Inner: 1\n  Inner: 2\nOuter: 2\n  Inner: 1\n  Inner: 2\n"),

                // While с ложным условием
                new SuccessCase("while_false_condition.php",
                        "Loop never executed\n"),

                // Do-while с ложным условием
                new SuccessCase("do_while_false_condition.php",
                        "This runs once\n"),

                // While со сложным условием
                new SuccessCase("while_with_complex_condition.php",
                        "x: 0, y: 0\nx: 1, y: 2\n"),

                // Do-while со сложным условием
                new SuccessCase("do_while_complex_condition.php",
                        "x: 0, y: 0\nx: 1, y: 2\n"),

                // While с пустым телом
                new SuccessCase("while_empty_body.php",
                        "Loop executed but did nothing\n"),

                // Do-while с пустым телом
                new SuccessCase("do_while_empty_body.php",
                        "Loop executed but did nothing\n"),

                // Альтернативный синтаксис с пустым телом
                new SuccessCase("while_alternative_empty.php",
                        "Loop executed but did nothing\n"),

                // Вложенные break/continue
                new SuccessCase("while_nested_break_continue.php",
                        "Outer: 0\n  Inner: 1\nOuter: 1\n  Inner: 1\nOuter: 2\n  Inner: 1\n"),

                // Несколько операторов в теле
                new SuccessCase("while_multiple_statements.php",
                        "First statement\nSecond statement\nThird statement\n" +
                                "First statement\nSecond statement\nThird statement\n")
        );
    }

    @Override
    protected Stream<ErrorCase> fileErrorCases() {
        return Stream.of(
                // Синтаксические ошибки в while
                new ErrorCase("error_while_missing_paren.php"),
                new ErrorCase("error_while_missing_endwhile.php"),
                new ErrorCase("error_do_while_missing_paren.php"),
                new ErrorCase("error_while_invalid_expression.php"),
                new ErrorCase("error_do_without_while.php"),
                new ErrorCase("error_while_without_condition.php"),
                new ErrorCase("error_endwhile_without_while.php"),
                new ErrorCase("error_while_missing_colon.php")
        );
    }
}