package exe.utils.ddt;

public record UnsupportedOperandCase(
        String input,
        String operandTypes
) implements IErrorMessageCase {

    @Override
    public String expectedMessage() {
        return "Unsupported operand types: " + operandTypes;
    }
}
