package io.github.pluslake.pipe.exception;

import java.util.function.*;

/**
 * A helper Class for handling checked execption.
 * This class is not supposed to use from outside of the StreamPipe library.
 */
public class Try {
    public static <T> Consumer<T> consumer(CheckedConsumer<T> consumer) {
        return t -> {
            try {
                consumer.accept(t);
            } catch (Exception e) {
                throw new PipeException(e);
            }
        };
    }

    public static <T> Supplier<T> supplier(CheckedSupplier<T> supplier) {
        return () -> {
            try {
                return supplier.get();
            } catch (Exception e) {
                throw new PipeException(e);
            }
        };
    }

    public static <T> Runnable runnable(CheckedSupplier<T> supplier) {
        return () -> {
            try {
                supplier.get();
            } catch (Exception e) {
                throw new PipeException(e);
            }
        };
    }

    public static Runnable runnable(CheckedRunnable runnable) {
        return () -> {
            try {
                runnable.run();
            } catch (Exception e) {
                throw new PipeException(e);
            }
        };
    }

    public static <T, R> Function<T, R> function(CheckedFunction<T, R> function) {
        return t -> {
            try {
                return function.apply(t);
            } catch (Exception e) {
                throw new PipeException(e);
            }
        };
    }

    public static interface CheckedConsumer<T> {
        public void accept(T t) throws Exception;
    }

    public static interface CheckedRunnable {
        public void run() throws Exception;
    }

    public static interface CheckedSupplier<T> {
        public T get() throws Exception;
    }

    public static interface CheckedFunction<T, R> {
        public R apply(T t) throws Exception;
    }
}
