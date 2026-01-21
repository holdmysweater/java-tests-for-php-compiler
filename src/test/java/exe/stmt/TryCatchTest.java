package exe.stmt;

import exe.utils.ExternalProcessDDT;
import exe.utils.ddt.ErrorCase;
import exe.utils.ddt.SuccessCase;

import java.util.stream.Stream;

class TryCatchTest extends ExternalProcessDDT {

    @Override
    protected String getName() {
        return "stmt/tryCatch";
    }

    @Override
    protected Stream<SuccessCase> fileCases() {
        return Stream.of(
                // Простой try-catch
                new SuccessCase("try_catch_simple.php",
                        "Start\n" +
                                "Exception caught: Division by zero\n" +
                                "End\n"),

                // Try-catch с разными типами исключений
                new SuccessCase("try_catch_different_exceptions.php",
                        "Start\n" +
                                "RuntimeException caught: Runtime error\n" +
                                "End\n"),

                // Try-finally
                new SuccessCase("try_finally.php",
                        "Start\n" +
                                "No exception thrown\n" +
                                "Finally block executed\n" +
                                "End\n"),

                // Try-catch-finally
                new SuccessCase("try_catch_finally.php",
                        "Start\n" +
                                "Exception caught: Something went wrong\n" +
                                "Finally block executed\n" +
                                "End\n"),

                // Catch без исключения
                new SuccessCase("try_catch_no_exception.php",
                        "Start\n" +
                                "No exception thrown\n" +
                                "End\n"),

                // Try-catch с повторным выбрасыванием исключения
                new SuccessCase("try_rethrow.php",
                        "Start\n" +
                                "Caught and rethrowing\n" +
                                "Caught rethrown exception: Original error\n"),

                // Try-catch с исключением в функции
                new SuccessCase("try_exception_in_function.php",
                        "Before function\n" +
                                "Exception caught: Function error\n" +
                                "After function\n"),

                // Try-catch с исключением в конструкторе
                new SuccessCase("try_exception_in_constructor.php",
                        "Start\n" +
                                "Exception caught: Constructor error\n" +
                                "End\n"),

                // Try-catch с finally без исключения
                new SuccessCase("try_finally_no_exception.php",
                        "Start\n" +
                                "Try block\n" +
                                "Finally block executed\n" +
                                "End\n"),

                // Try-catch с исключением и return в finally
                new SuccessCase("try_finally_return.php",
                        "Start\n" +
                                "Finally block\n" +
                                "Finally executed\n"),

                // Catch с разными типами исключений в одном блоке
                new SuccessCase("try_catch_union_types.php",
                        "Start\n" +
                                "Exception caught: Some error\n" +
                                "End\n"),

                // Try-catch с break в цикле
                new SuccessCase("try_catch_with_break.php",
                        "Iteration 1\n" +
                                "Iteration 2\n" +
                                "Exception caught: Break error\n" +
                                "Loop ended\n")
        );
    }

    @Override
    protected Stream<ErrorCase> fileErrorCases() {
        return Stream.of(
                // Ошибки синтаксиса
                new ErrorCase("error_try_missing_catch.php"),
                new ErrorCase("error_try_missing_brace.php"),
                new ErrorCase("error_catch_missing_paren.php"),
                new ErrorCase("error_catch_missing_variable.php"),
                new ErrorCase("error_finally_without_try.php"),
                new ErrorCase("error_catch_without_try.php"),
                new ErrorCase("error_try_missing_block.php")
        );
    }
}