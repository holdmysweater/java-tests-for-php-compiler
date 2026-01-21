package exe.stmt;

import exe.utils.ExternalProcessDDT;
import exe.utils.ddt.ErrorCase;
import exe.utils.ddt.SuccessCase;

import java.util.stream.Stream;

class FunctionTest extends ExternalProcessDDT {

    @Override
    protected String getName() {
        return "stmt/function";
    }

    @Override
    protected Stream<SuccessCase> fileCases() {
        return Stream.of(
                // Простая функция без параметров
                new SuccessCase("function_simple.php",
                        "Hello from function!\n"),

                // Функция с одним параметром
                new SuccessCase("function_with_parameter.php",
                        "Hello, John!\n" +
                                "Hello, Jane!\n"),

                // Функция с несколькими параметрами
                new SuccessCase("function_multiple_params.php",
                        "Result: 15\n" +
                                "Result: 50\n"),

                // Функция с параметрами по умолчанию
                new SuccessCase("function_default_params.php",
                        "Hello, Guest, welcome!\n" +
                                "Hello, Alice, welcome!\n" +
                                "Hello, Bob, welcome!\n"),

                // Функция с возвращаемым типом
                new SuccessCase("function_return_type.php",
                        "30\n" +
                                "7.5\n"),

                // Функция с типом array
                new SuccessCase("function_array_type.php",
                        "Array: 1,2,3\n"),

                // Функция с передачей по ссылке
                new SuccessCase("function_by_reference.php",
                        "Original: 10\n" +
                                "Modified: 20\n"),

                // Функция с типизированными параметрами
                new SuccessCase("function_typed_params.php",
                        "Sum: 8\n" +
                                "Concat: HelloWorld\n"),

                // Функция с рекурсией
                new SuccessCase("function_recursive.php",
                        "Factorial of 5 is: 120\n"),

                // Функция с вложенным вызовом
                new SuccessCase("function_nested.php",
                        "Result: 30\n"),

                // Функция с возвратом массива
                new SuccessCase("function_return_array.php",
                        "1,2,3\n"),

                // Функция с void типом
                new SuccessCase("function_void_type.php",
                        "Printing...\n"),

                // Функция с несколькими типами возврата
                new SuccessCase("function_multiple_return_types.php",
                        "Int: 10\n" +
                                "String: hello\n" +
                                "Array\n" +
                                "(\n" +
                                "    [0] => 1\n" +
                                "    [1] => 2\n" +
                                "    [2] => 3\n" +
                                ")\n"),

                // Функция с параметром по ссылке со значением по умолчанию
                new SuccessCase("function_reference_default.php",
                        "Value: 10\n" +
                                "Value: 100\n"),

                // Функция с типизированным параметром по ссылке
                new SuccessCase("function_typed_reference.php",
                        "Before: 5\n" +
                                "After: 10\n")
        );
    }

    @Override
    protected Stream<ErrorCase> fileErrorCases() {
        return Stream.of(
                // Ошибки синтаксиса
                new ErrorCase("error_function_missing_name.php"),
                new ErrorCase("error_function_missing_paren.php"),
                new ErrorCase("error_function_missing_brace.php"),
                new ErrorCase("error_function_duplicate_param.php"),
                new ErrorCase("error_function_invalid_default.php"),
                new ErrorCase("error_function_redeclaration.php")
        );
    }
}