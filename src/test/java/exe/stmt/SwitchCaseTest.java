package exe.stmt;

import exe.utils.ExternalProcessDDT;
import exe.utils.ddt.ErrorCase;
import exe.utils.ddt.SuccessCase;

import java.util.stream.Stream;

class SwitchCaseTest extends ExternalProcessDDT {

    @Override
    protected String getName() {
        return "stmt/switchCase";
    }

    @Override
    protected Stream<SuccessCase> fileCases() {
        return Stream.of(
                // Простой switch с фигурными скобками
                new SuccessCase("switch_simple.php",
                        "Monday\n"),

                // Switch с альтернативным синтаксисом
                new SuccessCase("switch_alternative_syntax.php",
                        "Monday\n"),

                // Switch с несколькими case
                new SuccessCase("switch_multiple_cases.php",
                        "Working day\n" +
                                "Working day\n" +
                                "Wednesday\n" +
                                "Working day\n" +
                                "Working day\n" +
                                "Weekend\n" +
                                "Invalid day\n"),

                // Switch с default
                new SuccessCase("switch_with_default.php",
                        "Unknown number\n"),

                // Switch с break
                new SuccessCase("switch_with_break.php",
                        "One\n" +
                                "Two\n" +
                                "Three\n" +
                                "Four\n" +
                                "Five\n"),

                // Switch без break (проваливание)
                new SuccessCase("switch_fallthrough.php",
                        "Small\n" +
                                "Medium\n" +
                                "Large\n" +
                                "Invalid size\n"),

                // Switch с вложенными case
                new SuccessCase("switch_nested_cases.php",
                        "Even number\n" +
                                "Odd number\n"),

                // Switch с выражениями в case
                new SuccessCase("switch_expressions.php",
                        "Between 1 and 3\n" +
                                "Between 1 and 3\n" +
                                "Between 1 and 3\n" +
                                "Greater than 3\n"),

                // Switch с пустым телом
                new SuccessCase("switch_empty.php",
                        "Switch with empty body\n"),

                // Switch с двоеточием в case
                new SuccessCase("switch_colon_syntax.php",
                        "Case 1\n"),

                // Switch с точкой с запятой в case
                new SuccessCase("switch_semicolon_syntax.php",
                        "Case 2\n"),

                // Switch с default в начале
                new SuccessCase("switch_default_first.php",
                        "Default case\n" +
                                "Case 1\n"),

                // Switch с числами
                new SuccessCase("switch_numbers.php",
                        "Number 1\n" +
                                "Number 2\n" +
                                "Number 3\n" +
                                "Not 1, 2, or 3\n"),

                // Switch с строками
                new SuccessCase("switch_strings.php",
                        "Color is red\n" +
                                "Color is green\n" +
                                "Color is blue\n" +
                                "Unknown color\n"),

                // Switch с boolean
                new SuccessCase("switch_boolean.php",
                        "True\n" +
                                "False\n" +
                                "Not boolean\n"),

                // Switch внутри функции
                new SuccessCase("switch_in_function.php",
                        "Grade: A\n" +
                                "Grade: B\n" +
                                "Grade: C\n" +
                                "Grade: F\n"),

                // Switch с return
                new SuccessCase("switch_with_return.php",
                        "Result: High\n" +
                                "Result: Medium\n" +
                                "Result: Low\n"),

                // Switch с continue в цикле
                new SuccessCase("switch_with_continue.php",
                        "Iteration 1: One\n" +
                                "Iteration 2: Skip\n" +
                                "Iteration 3: Three\n"),

                // Switch с вложенным switch
                new SuccessCase("switch_nested.php",
                        "Outer: 1\n" +
                                "  Inner: A\n" +
                                "  Inner: B\n" +
                                "Outer: 2\n" +
                                "  Inner: C\n")
        );
    }

    @Override
    protected Stream<ErrorCase> fileErrorCases() {
        return Stream.of(
                // Ошибки синтаксиса
                new ErrorCase("error_switch_missing_paren.php"),
                new ErrorCase("error_switch_missing_brace.php"),
                new ErrorCase("error_switch_missing_endswitch.php"),
                new ErrorCase("error_case_missing_expression.php"),
                new ErrorCase("error_case_missing_colon.php"),
                new ErrorCase("error_default_missing_colon.php"),
                new ErrorCase("error_switch_missing_expression.php"),
                new ErrorCase("error_switch_duplicate_default.php")
        );
    }
}