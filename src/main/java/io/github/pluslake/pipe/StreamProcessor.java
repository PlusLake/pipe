package io.github.pluslake.pipe;

import java.io.*;

import io.github.pluslake.pipe.exception.PipeException;

/**
 * Represents a "Imaginary Process" which can read data from upstream, and write data to downstream.
 * This interface is supposed to be invoked by {@link StreamPipe}.
 */
public interface StreamProcessor {
    /**
     * Handle the given InputStream from the upstream, and write the result to the downstream.
     * 
     * @param in  The stream that can be read from the upstream
     * @param out The stream that can be write to the downstream
     * @return an exit code of the execution result
     * @throws Exception you may dicide to catch your exceptions inside this method,
     *                   or just let the {@link StreamPipe#run} wraps all the exception into
     *                   {@link PipeException} when execution.
     */
    public int process(InputStream in, OutputStream out) throws Exception;
}
