package io.github.pluslake.pipe.stream;

import java.io.*;
import java.util.List;

import io.github.pluslake.pipe.*;
import io.github.pluslake.pipe.exception.Try;

/**
 * A reference implementation of StreamProcessor for calling external processes. 3 threads will be
 * created to:
 * <li> Reading InputStream from upstream Stream and pass to the process
 * <li> Redirecting stdout of the process, to the OutputStream of downstream StreamProcessor
 * <li> Redirecting stderr of the process, to System.err
 */
public class ProcessStream implements StreamProcessor {
    private int bufferSize = 1024;

    private final ProcessBuilder builder;

    private ProcessStream(ProcessBuilder builder) {
        this.builder = builder;
    }

    private Thread thread(InputStream in, OutputStream out) {
        return new Thread(Try.runnable(() -> StreamWriter.write(in, out, bufferSize)));
    }

    @Override
    public int process(InputStream in, OutputStream out) throws IOException, InterruptedException {
        Process process = builder.start();
        List<Thread> list = List.of(
                thread(in, process.getOutputStream()),
                thread(process.getInputStream(), out),
                thread(process.getErrorStream(), systemError())
        );
        list.forEach(Thread::start);
        list.forEach(Try.consumer(Thread::join));
        return process.waitFor();
    }

    /** Create an unclosable OutputStream of System.err */
    private OutputStream systemError() {
        return new FilterOutputStream(System.err) {
            public void close() throws IOException {
                this.flush();
            }
        };
    }

    /**
     * Set the buffer size.
     * 
     * @throws IllegalArgumentException if the specified buffer size is negative.
     */
    public ProcessStream setBufferSize(int size) {
        if (size < 1) {
            String message = "The new buffer size must be greater than 0. Given: %d";
            throw new IllegalArgumentException(String.format(message, size));
        }
        return this;
    }

    /**
     * Create a ProcessStream from list of arguments. The arguments will be used in
     *  {@link ProcessBuilder#ProcessBuilder(List))} directly. See {@link ProcessStream}
     * for details.
     */
    public static ProcessStream of(List<String> arguments) {
        return new ProcessStream(new ProcessBuilder(arguments));
    }

    /**
     * Create a ProcessStream from list of arguments. The arguments will be used in
     * {@link ProcessBuilder#ProcessBuilder(String...)} directly. See {@link ProcessStream}
     * for details.
     */
    public static ProcessStream of(String... arguments) {
        return new ProcessStream(new ProcessBuilder(arguments));
    }
}
