package exe.example;

import exe.utils.ExternalProcessDDT;
import exe.utils.ddt.*;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

class ExampleTest extends ExternalProcessDDT {

    @Override
    protected String getName() {
        // Searches for files in `resources/exe/example`
        return "example";
    }

    /* ================= FILE ================= */

    @Override
    protected Stream<SuccessCase> fileCases() {
        return Stream.of(
                new SuccessCase("echo.php", "Hello world!"),
                new SuccessCase("echo2.php", "Serendipity")
        );
    }

    @Override
    protected Stream<ErrorCase> fileErrorCases() {
        return Stream.of(
                new ErrorCase("concat.php")
        );
    }

    @Override
    protected Stream<IErrorMessageCase> fileErrorMessageCases() {
        return Stream.of(
                new UnsupportedOperandCase("concat.php", "string + int")
        );
    }

    /* ================= PHP ================= */

    @Override
    protected Stream<SuccessCase> phpCases() {
        return Stream.of(
                new SuccessCase("<?php echo 'hi';", "hi"),
                new SuccessCase("<?php echo '123';", "123"),
                new SuccessCase("<?php echo 2 + 3;", "5"),
                new SuccessCase("<?php echo \"i<\" . 3 . 'u';", "i<3u")
        );
    }

    @Override
    protected Stream<ErrorCase> phpErrorCases() {
        return Stream.of(
                new ErrorCase("<?php echo 324 + 'hi';")
        );
    }

    @Override
    protected Stream<IErrorMessageCase> phpErrorMessageCases() {
        return Stream.of(
                new UnsupportedOperandCase("<?php echo 324 + 'hi';", "string + int")
        );
    }

    /* ================= ECHO ================= */

    @Override
    protected Stream<SuccessCase> echoCases() {
        return Stream.of(
                new SuccessCase(" 'hi' ", "hi")
        );
    }

    @Override
    protected Stream<ErrorCase> echoErrorCases() {
        return Stream.of(
                new ErrorCase("324 + 'hi' ")
        );
    }

    @Override
    protected Stream<IErrorMessageCase> echoErrorMessageCases() {
        return Stream.of(
                new ErrorMessageCase("324 + 'hi' ", "Unsupported operand types: string + int")
        );
    }

    @Test
    void input() throws Exception {
        assertFileWithInput("input.php", "hello", "hello");
    }
}
