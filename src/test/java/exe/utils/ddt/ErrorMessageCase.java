package exe.utils.ddt;


public record ErrorMessageCase(
        String input,
        String expectedMessage
) implements IErrorMessageCase {
}
