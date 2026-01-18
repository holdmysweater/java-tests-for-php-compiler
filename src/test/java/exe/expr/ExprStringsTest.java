package exe.expr;

import exe.utils.ExternalProcessDDT;
import exe.utils.ddt.SuccessCase;

import java.util.stream.Stream;

class ExprStringsTest extends ExternalProcessDDT {

    @Override
    protected Stream<SuccessCase> echoCases() {
        return Stream.of(
                // Different values
                new SuccessCase("123", "123"),
                new SuccessCase("\"hi\"", "hi"),

                // Number cases
                new SuccessCase("10 + 15", "25"),
                new SuccessCase("10 . 15", "1015")
        );
    }

}
