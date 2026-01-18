package exe.expr;

import exe.utils.ExternalProcessDDT;
import exe.utils.ddt.ErrorCase;
import exe.utils.ddt.SuccessCase;

import java.util.stream.Stream;

class SimpleExprTest extends ExternalProcessDDT {

    @Override
    protected Stream<SuccessCase> echoCases() {
        return Stream.of(
                // Different values
                new SuccessCase("123", "123"),
                new SuccessCase(" 'hi' ", "hi"),

                // Number cases
                new SuccessCase("10 + 15", "25"),
                new SuccessCase("10 . 15", "1015")
        );
    }

    @Override
    protected Stream<ErrorCase> echoErrorCases() {
        return Stream.of(
                // Different types
                new ErrorCase("324 + 'hi' ")
        );
    }

}
