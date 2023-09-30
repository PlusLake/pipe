package io.github.pluslake.pipe.exception;

import io.github.pluslake.pipe.StreamPipe;

/** This exception will be thrown when unexpected exceptions thrown in {@link StreamPipe#run()}. */
public class PipeException extends RuntimeException {
    /**
     * Wraps checked exception while execution in StreamPipe.
     * 
     * @param e the exception to wrap
     */
    public PipeException(Exception e) {
        super(e);
    }
}
