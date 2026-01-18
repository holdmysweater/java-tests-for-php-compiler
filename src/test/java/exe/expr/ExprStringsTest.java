package exe.expr;

import exe.ExternalProcess;
import org.junit.jupiter.api.Test;

public class ExprStringsTest extends ExternalProcess {

    @Override
    protected String getName() {
        return "expr";
    }

    @Test
    void echoString() throws Exception {
        assertRun(
                "echo.php",
                "Hello world!"
        );
    }

    @Test
    void concatStrings() throws Exception {
        assertRun(
                "concat.php",
                "foobar"
        );
    }
}
