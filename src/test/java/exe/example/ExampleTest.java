package exe.example;

import exe.ExternalProcess;
import org.junit.jupiter.api.Test;

class ExampleTest extends ExternalProcess {

    @Override
    protected String getName() {
        return "example";
    }

    @Test
    void file() throws Exception {
        assertFile("echo.php", "Hello world!");
    }

    @Test
    void fileError() throws Exception {
        assertFileError("concat.php");
    }

    @Test
    void fileErrorWithMessage() throws Exception {
        assertFileError("concat.php", "Unsupported operand types: string + int");
    }

    @Test
    void php() throws Exception {
        assertPhp("<?php echo \"hi\";", "hi");
    }

    @Test
    void phpError() throws Exception {
        assertPhpError("<?php echo \"324 + \"hi\";");
    }

    @Test
    void phpErrorWithMessage() throws Exception {
        assertPhpError("<?php echo 324 + \"hi\";", "Unsupported operand types: string + int");
    }

    @Test
    void echo() throws Exception {
        assertPhpEcho("\"hi\"", "hi");
    }

    @Test
    void echoError() throws Exception {
        assertPhpEchoError("324 + \"hi\"");
    }

    @Test
    void echoErrorWithMessage() throws Exception {
        assertPhpEchoError("324 + \"hi\"", "Unsupported operand types: string + int");
    }
}
