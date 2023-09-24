package io.github.pluslake.pipe;

import java.io.*;

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
     * 
     * @return An exit code of the execution result.
     */
    public int process(InputStream in, OutputStream out) throws Exception;
}
