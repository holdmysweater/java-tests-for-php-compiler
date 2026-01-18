package exe;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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

    // ---- to be provided by subclasses ----

    /**
     * Example: "expr", "stmt"
     */
    protected abstract String getName();

    // ---- before each ----

    @BeforeEach
    void cleanDumpDirectory() throws IOException {
        if (Files.exists(OUTPUT_DIR)) {
            Files.walk(OUTPUT_DIR)
                    .sorted(Comparator.reverseOrder())
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                        } catch (IOException e) {
                            throw new RuntimeException("Failed to delete " + path, e);
                        }
                    });
        }
    }

    // ---- public assertion helpers ----

    protected final void assertRun(String inputFile, String expectedOutput)
            throws Exception {

        Result result = runInternal(inputFile);

        Assertions.assertEquals(
                0,
                result.exitCode,
                "Expected exit code 0"
        );

        Assertions.assertEquals(
                normalize(expectedOutput),
                normalize(result.output),
                "Output mismatch"
        );
    }

    protected final void assertRunWithInput(
            String inputFile,
            String simulatedUserInput,
            String expectedOutput
    ) throws Exception {

        Result result = runInternal(inputFile, simulatedUserInput);

        Assertions.assertEquals(
                0,
                result.exitCode,
                "Expected exit code 0"
        );

        Assertions.assertEquals(
                normalize(expectedOutput),
                normalize(result.output),
                "Output mismatch"
        );
    }

    protected final void assertException(String inputFile)
            throws Exception {

        Result result = runInternal(inputFile);

        Assertions.assertNotEquals(
                0,
                result.exitCode,
                "Expected non-zero exit code"
        );
    }

    protected final void assertException(
            String inputFile,
            String expectedSubstring
    ) throws Exception {

        Result result = runInternal(inputFile);

        Assertions.assertNotEquals(
                0,
                result.exitCode,
                "Expected non-zero exit code"
        );

        Assertions.assertTrue(
                result.output.contains(expectedSubstring),
                "Expected output to contain: " + expectedSubstring
        );
    }

    // ---- core execution ----

    private Result runInternal(String inputFile) throws Exception {
        return runInternal(inputFile, null);
    }

    private Result runInternal(
            String inputFile,
            String stdinInput
    ) throws Exception {

        Path inputPath = PROJECT_ROOT
                .resolve("src")
                .resolve("test")
                .resolve("resources")
                .resolve("exe")
                .resolve(getName())
                .resolve(inputFile)
                .normalize();

        if (!inputPath.toFile().exists()) {
            throw new IllegalArgumentException(
                    "Input file does not exist: " + inputPath
            );
        }

        File outDir = OUTPUT_DIR.toFile();
        if (!outDir.exists() && !outDir.mkdirs()) {
            throw new IllegalStateException("Could not create output directory");
        }

        List<String> command = new ArrayList<>();
        command.add(EXE_PATH.toAbsolutePath().toString());
        command.add(inputPath.toAbsolutePath().toString());
        command.add("-o");
        command.add(OUTPUT_DIR.toAbsolutePath().toString());

        ProcessBuilder pb = new ProcessBuilder(command);
        pb.directory(EXE_PATH.getParent().toFile());
        pb.redirectErrorStream(true);

        Process process = pb.start();

        if (stdinInput != null) {
            try (var stdin = process.getOutputStream()) {
                stdin.write(stdinInput.getBytes(StandardCharsets.UTF_8));
                stdin.flush();
            }
        }

        String output = new String(
                process.getInputStream().readAllBytes(),
                StandardCharsets.UTF_8
        );

        int exitCode = process.waitFor();

        return new Result(exitCode, output);
    }

    protected static String normalize(String s) {
        return s
                .replace("\r\n", "\n")
                .trim();
    }

    protected record Result(int exitCode, String output) {
    }
}
