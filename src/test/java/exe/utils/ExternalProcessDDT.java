package exe.utils;

import exe.utils.ddt.ErrorCase;
import exe.utils.ddt.IErrorMessageCase;
import exe.utils.ddt.SuccessCase;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class ExternalProcessDDT extends ExternalProcess {

    /* ============================================================
       CASE DEFINITIONS (override what you need)
       ============================================================ */

    // -------- FILE --------

    protected Stream<SuccessCase> fileCases() {
        return Stream.empty();
    }

    protected Stream<ErrorCase> fileErrorCases() {
        return Stream.empty();
    }

    protected Stream<IErrorMessageCase> fileErrorMessageCases() {
        return Stream.empty();
    }

    // -------- PHP --------

    protected Stream<SuccessCase> phpCases() {
        return Stream.empty();
    }

    protected Stream<ErrorCase> phpErrorCases() {
        return Stream.empty();
    }

    protected Stream<IErrorMessageCase> phpErrorMessageCases() {
        return Stream.empty();
    }

    // -------- ECHO --------

    protected Stream<SuccessCase> echoCases() {
        return Stream.empty();
    }

    protected Stream<ErrorCase> echoErrorCases() {
        return Stream.empty();
    }

    protected Stream<IErrorMessageCase> echoErrorMessageCases() {
        return Stream.empty();
    }

    /* ============================================================
       TESTS (JUnit entry points)
       ============================================================ */

    // -------- FILE --------

    @ParameterizedTest(name = "file: {0}")
    @MethodSource("fileCasesProvider")
    void file_ddt(SuccessCase c) throws Exception {
        assertFile(c.input(), c.expected());
    }

    @ParameterizedTest(name = "file error: {0}")
    @MethodSource("fileErrorCasesProvider")
    void file_error_ddt(ErrorCase c) throws Exception {
        assertFileError(c.input());
    }

    @ParameterizedTest(name = "file error msg: {0}")
    @MethodSource("fileErrorMessageCasesProvider")
    void file_error_msg_ddt(IErrorMessageCase c) throws Exception {
        assertFileError(c.input(), c.expectedMessage());
    }

    // -------- PHP --------

    @ParameterizedTest(name = "php: {0}")
    @MethodSource("phpCasesProvider")
    void php_ddt(SuccessCase c) throws Exception {
        assertPhp(c.input(), c.expected());
    }

    @ParameterizedTest(name = "php error: {0}")
    @MethodSource("phpErrorCasesProvider")
    void php_error_ddt(ErrorCase c) throws Exception {
        assertPhpError(c.input());
    }

    @ParameterizedTest(name = "php error msg: {0}")
    @MethodSource("phpErrorMessageCasesProvider")
    void php_error_msg_ddt(IErrorMessageCase c) throws Exception {
        assertPhpError(c.input(), c.expectedMessage());
    }

    // -------- ECHO --------

    @ParameterizedTest(name = "echo: {0}")
    @MethodSource("echoCasesProvider")
    void echo_ddt(SuccessCase c) throws Exception {
        assertPhpEcho(c.input(), c.expected());
    }

    @ParameterizedTest(name = "echo error: {0}")
    @MethodSource("echoErrorCasesProvider")
    void echo_error_ddt(ErrorCase c) throws Exception {
        assertPhpEchoError(c.input());
    }

    @ParameterizedTest(name = "echo error msg: {0}")
    @MethodSource("echoErrorMessageCasesProvider")
    void echo_error_msg_ddt(IErrorMessageCase c) throws Exception {
        assertPhpEchoError(c.input(), c.expectedMessage());
    }

    /* ============================================================
       PROVIDERS (centralized, readable)
       ============================================================ */

    // -------- FILE --------

    Stream<SuccessCase> fileCasesProvider() {
        return optional(fileCases(), "No file success cases defined");
    }

    Stream<ErrorCase> fileErrorCasesProvider() {
        return optional(fileErrorCases(), "No file error cases defined");
    }

    Stream<IErrorMessageCase> fileErrorMessageCasesProvider() {
        return optional(fileErrorMessageCases(), "No file error-message cases defined");
    }

    // -------- PHP --------

    Stream<SuccessCase> phpCasesProvider() {
        return optional(phpCases(), "No php success cases defined");
    }

    Stream<ErrorCase> phpErrorCasesProvider() {
        return optional(phpErrorCases(), "No php error cases defined");
    }

    Stream<IErrorMessageCase> phpErrorMessageCasesProvider() {
        return optional(phpErrorMessageCases(), "No php error-message cases defined");
    }

    // -------- ECHO --------

    Stream<SuccessCase> echoCasesProvider() {
        return optional(echoCases(), "No echo success cases defined");
    }

    Stream<ErrorCase> echoErrorCasesProvider() {
        return optional(echoErrorCases(), "No echo error cases defined");
    }

    Stream<IErrorMessageCase> echoErrorMessageCasesProvider() {
        return optional(echoErrorMessageCases(), "No echo error-message cases defined");
    }

    /* ============================================================
       SHARED HELPER
       ============================================================ */

    protected static <T> Stream<T> optional(Stream<T> source, String reason) {
        var list = source.toList();
        Assumptions.assumeTrue(!list.isEmpty(), reason);
        return list.stream();
    }
}
