package exe.stmt;

import exe.utils.ExternalProcessDDT;
import exe.utils.ddt.ErrorCase;
import exe.utils.ddt.SuccessCase;

import java.util.stream.Stream;

class IfTest extends ExternalProcessDDT {

    @Override
    protected String getName() {
        return "stmt/if";
    }

    @Override
    protected Stream<SuccessCase> fileCases() {
        return Stream.of(
                // Простой if
                new SuccessCase("simple_if.php", "Condition true\n"),

                // If с else
                new SuccessCase("if_else.php", "Else executed\n"),

                // If с elseif
                new SuccessCase("if_elseif.php", "x is 2\n"),

                // If с elseif и else
                new SuccessCase("if_elseif_else.php", "x is neither 1 nor 2\n"),

                // Альтернативный синтаксис
                new SuccessCase("if_alternative_syntax.php",
                        "Alternative syntax\n" +
                                "Works!\n"),

                // Альтернативный синтаксис с else
                new SuccessCase("if_else_alternative_syntax.php",
                        "Else in alternative syntax\n"),

                // Альтернативный синтаксис с elseif
                new SuccessCase("if_elseif_alternative_syntax.php",
                        "x is 3\n"),

                // Альтернативный синтаксис с elseif и else
                new SuccessCase("if_elseif_else_alternative_syntax.php",
                        "x is other\n"),

                // Вложенные if
                new SuccessCase("nested_if.php",
                        "a true, b false\n"),

                // Сложные выражения
                new SuccessCase("complex_expression.php",
                        "Complex condition true\n" +
                                "OR condition true\n" +
                                "NOT condition true\n"),

                // If с вычислениями
                new SuccessCase("if_with_operations.php",
                        "Sum is greater than 10\n" +
                                "x * 2 equals y\n"),

                // Пустой if
                new SuccessCase("empty_if.php",
                        "After empty if\n"),

                // If со строками
                new SuccessCase("if_with_strings.php",
                        "String comparison works\n" +
                                "String inequality works\n"),

                // If с null
                new SuccessCase("if_with_null.php",
                        "Null is falsy\n")
        );
    }

    @Override
    protected Stream<ErrorCase> fileErrorCases() {
        return Stream.of(
                // Синтаксические ошибки
                new ErrorCase("error_missing_paren.php"),
                new ErrorCase("error_missing_endif.php"),
                new ErrorCase("error_invalid_expression.php"),
                new ErrorCase("error_else_without_if.php"),
                new ErrorCase("error_elseif_without_if.php"),
                new ErrorCase("error_unexpected_endif.php"),
                new ErrorCase("error_missing_colon.php")
        );
    }
}