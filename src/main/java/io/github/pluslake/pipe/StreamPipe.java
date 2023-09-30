package io.github.pluslake.pipe;

import static java.io.InputStream.nullInputStream;

import java.io.*;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.*;

import io.github.pluslake.pipe.exception.Try;

/**
 * A Pipeline executor that executes multiple chained {@link StreamProcessor} interface.
 * 
 * @see StreamPipe#run()
 * @see StreamProcessor
 */
public class StreamPipe {

    private final StreamProcessor[] processors;

    private StreamPipe(StreamProcessor[] processors) {
        this.processors = processors;
    }

    /**
     * Executes all StreamProcessors in different threads, and each StreamProcessor's InputStream
     * will be piped into the next StreamProcessor's OutputStream. The InputStream of the first
     * StreamProcessor will be set to {@link InputStream#nullInputStream()} and the last
     * OutputStream of the StreamProcess will be set to System.out by default.
     * 
     * @return A list of exit code of each StreamProcessors
     */
    public List<Integer> run() {
        return Try.supplier(this::runInner).get();
    }

    private List<Integer> runInner() {
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
        out.add(systemOut());
        List<InnerRunnable> runnables = IntStream
                .range(0, processors.length)
                .mapToObj(i -> Try.supplier(() -> processors[i].process(in.get(i), out.get(i))))
                .map(InnerRunnable::new)
                .toList();
        List<Thread> threads = runnables.stream().map(Thread::new).toList();
        threads.forEach(Thread::start);
        threads.forEach(Try.consumer(Thread::join));
        return runnables.stream().map(InnerRunnable::exitCode).toList();
    }

    /** Create an unclosable OutputStream of System.out */
    private OutputStream systemOut() {
        return new FilterOutputStream(System.out) {
            public void close() throws IOException {
                this.flush();
            }
        };
    }

    /**
     * Create a StreamPipe containing multiple StreamProcessors.
     * 
     * @param processors an array of StreamProcessors
     * @return a StreamPipe instance that is ready to run
     */
    public static StreamPipe of(StreamProcessor... processors) {
        return new StreamPipe(processors);
    }

    /**
     * Create a StreamPipe containing multiple StreamProcessors.
     * 
     * @param processors an list of StreamProcessors
     * @return a StreamPipe instance that is ready to run
     */
    public static StreamPipe of(List<StreamProcessor> processors) {
        return new StreamPipe(processors.toArray(StreamProcessor[]::new));
    }

    private static class InnerRunnable implements Runnable {
        private int exitCode;
        private final Supplier<Integer> supplier;

        private InnerRunnable(Supplier<Integer> supplier) {
            this.supplier = supplier;
        }

        @Override
        public void run() {
            this.exitCode = supplier.get();
        }

        private int exitCode() {
            return exitCode;
        }
    }
}
