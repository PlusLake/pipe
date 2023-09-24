package io.github.pluslake.pipe;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

import org.junit.jupiter.api.Test;

import io.github.pluslake.pipe.stream.ProcessStream;
import io.github.pluslake.pipe.stream.compression.GZipStreamProcessors;

public class PipeTest {

    /** A simple example to call external processes. */
    @Test
    public void commandTest() {
        List<Integer> result = StreamPipe.of(
                ProcessStream.of("echo", "Java\nJavaScript\nC\nRust"),
                ProcessStream.of("grep", "Java")
        ).run();
        assertIterableEquals(List.of(0, 0), result);
    }

    /** Exit code of external processes will be returned. */
    @Test
    public void exitCode() {
        List<Integer> result = StreamPipe.of(
                ProcessStream.of("cat", "AnyNonExisitingFile" + System.currentTimeMillis())
        ).run();
        assertEquals(1, result.size());
        assertEquals(1, result.get(0));
    }

    /** GZip test  */
    @Test
    public void gzip() throws IOException {
        String fileInputPlain = "unit_test/in/fruits.txt";
        String fileInput = "unit_test/in/fruits.txt.gz";
        String fileOutput = "unit_test/out/gzip_test_output.txt";

        List<Integer> result = StreamPipe.of(
                ProcessStream.of("cat", fileInput),
                GZipStreamProcessors.extract(),
                ProcessStream.of("cp", "/dev/stdin", fileOutput)
        ).run();
        assertIterableEquals(List.of(0, 0, 0), result);
        assertEquals(Files.readString(Path.of(fileInputPlain)), Files.readString(Path.of(fileOutput)));
    }
}
