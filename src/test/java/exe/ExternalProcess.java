package exe;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public abstract class ExternalProcess {

    // ---- configuration ----

    protected static final Path PROJECT_ROOT = Paths.get(System.getProperty("user.dir"));

    protected static final Path EXE_PATH = PROJECT_ROOT
            .resolve("src")
            .resolve("test")
            .resolve("resources")
            .resolve("exe")
            .resolve("php_compiler.exe");

    protected static final Path OUTPUT_DIR = PROJECT_ROOT.resolve("target").resolve("dump");

    protected static final Path TEMP_SOURCE_DIR = OUTPUT_DIR;

    /**
     * Example: "expr", "stmt"
     */
    protected abstract String getName();

    // ---- lifecycle ----

    @BeforeEach
    void cleanDumpDirectory() throws IOException {
        if (Files.exists(OUTPUT_DIR)) {
            Files.walk(OUTPUT_DIR)
                    .sorted(Comparator.reverseOrder())
                    .forEach(p -> {
                        try {
                            Files.delete(p);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
        }
        Files.createDirectories(TEMP_SOURCE_DIR);
    }

    // ============================================================
    // Inline PHP assertions
    // ============================================================

    protected final void assertPhp(String phpSource, String expectedOutput)
            throws Exception {

        Path file = writeTempPhpFile(phpSource);
        assertFile(file, expectedOutput);
    }

    protected final void assertPhpEcho(String expr, String expectedOutput)
            throws Exception {

        assertPhp("<?= " + expr + " ?>", expectedOutput);
    }

    protected final void assertPhpError(String phpSource)
            throws Exception {

        Path file = writeTempPhpFile(phpSource);
        assertFileError(file);
    }

    protected final void assertPhpError(String phpSource, String expectedSubstring)
            throws Exception {

        Path file = writeTempPhpFile(phpSource);
        assertFileError(file, expectedSubstring);
    }

    protected final void assertPhpEchoError(String expr)
            throws Exception {

        assertPhpError("<?= " + expr + " ?>");
    }

    protected final void assertPhpEchoError(String expr, String expectedSubstring)
            throws Exception {

        assertPhpError("<?= " + expr + " ?>", expectedSubstring);
    }

    // ============================================================
    // File-based assertions (STRING API)
    // ============================================================

    protected final void assertFile(String inputFile, String expectedOutput) throws Exception {

        assertFile(resolveInputFile(inputFile), expectedOutput);
    }

    protected final void assertFileWithInput(
            String inputFile,
            String stdin,
            String expectedOutput
    ) throws Exception {

        assertFileWithInput(resolveInputFile(inputFile), stdin, expectedOutput);
    }

    protected final void assertFileError(String inputFile) throws Exception {

        assertFileError(resolveInputFile(inputFile));
    }

    protected final void assertFileError(
            String inputFile,
            String expectedSubstring
    ) throws Exception {

        assertFileError(resolveInputFile(inputFile), expectedSubstring);
    }

    // ============================================================
    // Internal file-based assertions (Path-based)
    // ============================================================

    private void assertFile(Path phpFile, String expectedOutput) throws Exception {

        Result result = compileAndRun(phpFile, null);

        Assertions.assertEquals(
                0,
                result.exitCode,
                "\nExpected exit code 0\nOutput:\n" + result.output
        );

        Assertions.assertEquals(
                normalize(expectedOutput),
                normalize(result.output),
                "Output mismatch"
        );
    }

    private void assertFileWithInput(
            Path phpFile,
            String stdin,
            String expectedOutput
    ) throws Exception {

        Result result = compileAndRun(phpFile, stdin);

        Assertions.assertEquals(
                0,
                result.exitCode,
                "\nExpected exit code 0\nOutput:\n" + result.output
        );

        Assertions.assertEquals(
                normalize(expectedOutput),
                normalize(result.output),
                "\nOutput mismatch"
        );
    }

    private void assertFileError(Path phpFile)
            throws Exception {

        Result result = compileAndRun(phpFile, null);

        Assertions.assertNotEquals(
                0,
                result.exitCode,
                "\nExpected non-zero exit code\nOutput:\n" + result.output
        );
    }

    private void assertFileError(
            Path phpFile,
            String expectedSubstring
    ) throws Exception {

        Result result = compileAndRun(phpFile, null);

        Assertions.assertNotEquals(
                0,
                result.exitCode,
                "\nExpected non-zero exit code\nOutput:\n" + result.output
        );

        Assertions.assertTrue(
                result.output.contains(expectedSubstring),
                "\nExpected output to contain:\n" + expectedSubstring + "\nOutput:\n" + result.output
        );
    }

    // ============================================================
    // Core execution pipeline
    // ============================================================

    private Result compileAndRun(Path phpFile, String stdin)
            throws Exception {

        // Compile
        Result compile = runCompiler(phpFile);
        if (compile.exitCode != 0) {
            return compile;
        }

        // Execute generated classes
        String mainClass = classNameFromPhp(phpFile);
        return runGeneratedProgram(mainClass, stdin);

    }

    private Result runCompiler(Path phpFile)
            throws Exception {

        List<String> command = List.of(
                EXE_PATH.toAbsolutePath().toString(),
                phpFile.toAbsolutePath().toString(),
                "-o",
                OUTPUT_DIR.toAbsolutePath().toString()
        );

        return runProcess(command, null);
    }

    private Result runGeneratedProgram(String mainClass, String stdin)
            throws Exception {

        List<String> command = List.of(
                "java",
                "-cp",
                OUTPUT_DIR.toAbsolutePath().toString(),
                mainClass
        );

        return runProcess(command, stdin);
    }

    private Result runProcess(List<String> command, String stdin)
            throws Exception {

        ProcessBuilder pb = new ProcessBuilder(command);
        pb.redirectErrorStream(true);

        Process process = pb.start();

        if (stdin != null) {
            try (var os = process.getOutputStream()) {
                os.write(stdin.getBytes(StandardCharsets.UTF_8));
            }
        }

        String output = new String(
                process.getInputStream().readAllBytes(),
                StandardCharsets.UTF_8
        );

        int exitCode = process.waitFor();
        return new Result(exitCode, output);
    }

    // ============================================================
    // Path + file helpers
    // ============================================================

    protected final Path resolveInputFile(String inputFile) {

        Path inputPath = PROJECT_ROOT
                .resolve("src")
                .resolve("test")
                .resolve("resources")
                .resolve("exe")
                .resolve(getName())
                .resolve(inputFile)
                .normalize();

        if (!Files.exists(inputPath)) {
            throw new IllegalArgumentException(
                    "Input file does not exist: " + inputPath
            );
        }

        return inputPath;
    }

    private static String classNameFromPhp(Path phpFile) {
        String name = phpFile.getFileName().toString();
        int dot = name.lastIndexOf('.');
        return dot == -1 ? name : name.substring(0, dot);
    }

    private Path writeTempPhpFile(String content)
            throws IOException {

        Path file = TEMP_SOURCE_DIR.resolve(
                UUID.randomUUID() + ".php"
        );

        Files.writeString(file, content, StandardCharsets.UTF_8);
        return file;
    }

    protected static String normalize(String s) {
        return s.replace("\r\n", "\n").trim();
    }

    protected record Result(int exitCode, String output) {
    }
}
