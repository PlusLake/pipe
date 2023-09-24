package io.github.pluslake.pipe;

import static java.io.InputStream.nullInputStream;

import java.io.*;
import java.util.*;
import java.util.stream.*;

import io.github.pluslake.pipe.exception.Try;

public class StreamPipe {
    /**
     * Executes all StreamProcessors in different threads, and each StreamProcessor's InputStream
     * will be piped into the next StreamProcessor's OutputStream. The InputStream of the first
     * StreamProcessor will be set to {@link InputStream#nullInputStream()} and the last
     *  OutputStream of the StreamProcess will be System.out by default.
     * 
     * @param processors 
     */
    public static void run(StreamProcessor... processors) {
        List<InputStream> in = IntStream
                .range(0, processors.length - 1)
                .mapToObj(i -> new PipedInputStream())
                .map(InputStream.class::cast)
                .collect(Collectors.toCollection(ArrayList::new));
        List<OutputStream> out = in
                .stream()
                .map(PipedInputStream.class::cast)
                .map(Try.function(PipedOutputStream::new))
                .collect(Collectors.toCollection(ArrayList::new));
        in.add(0, nullInputStream());
        out.add(System.out);
        List<Thread> threads = IntStream
                .range(0, processors.length)
                .mapToObj(i -> Try.runnable(() -> processors[i].process(in.get(i), out.get(i))))
                .map(Thread::new)
                .toList();
        threads.forEach(Thread::start);
        threads.forEach(Try.consumer(Thread::join));
    }

    public static void run(List<StreamProcessor> processors) {
        run(processors.toArray(StreamProcessor[]::new));
    }

    public static void main(String[] args) throws IOException {
        ProcessBuilder.startPipeline(List.of());
        List.of();
    }
}
