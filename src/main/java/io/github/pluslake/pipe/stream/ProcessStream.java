package io.github.pluslake.pipe.stream;

import java.io.*;
import java.util.List;

import io.github.pluslake.pipe.StreamProcessor;
import io.github.pluslake.pipe.exception.Try;

/**
 * TODO
 */
public class ProcessStream implements StreamProcessor {
    private int bufferSize = 1024;

    private final ProcessBuilder builder;

    private ProcessStream(ProcessBuilder builder) {
        this.builder = builder;
    }

    private Thread thread(InputStream in, OutputStream out) {
        return new Thread(Try.runnable(() -> {
            for (byte[] buffer; (buffer = in.readNBytes(bufferSize)).length > 0; out.write(buffer));
            out.close();
        }));
    }

    @Override
    public int process(InputStream in, OutputStream out) throws IOException, InterruptedException {
        Process process = builder.start();
        List<Thread> list = List.of(
                thread(in, process.getOutputStream()),
                thread(process.getInputStream(), out)
        );
        list.forEach(Thread::start);
        list.forEach(Try.consumer(Thread::join));
        return process.waitFor();
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
     * TODO
     */
    public static ProcessStream of(List<String> arguments) {
        return new ProcessStream(new ProcessBuilder(arguments));
    }

    /**
     * TODO
     */
    public static ProcessStream of(String... arguments) {
        return new ProcessStream(new ProcessBuilder(arguments));
    }
}
