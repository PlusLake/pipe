package io.github.pluslake.pipe;

import java.io.*;

/**
 * TODO
 */
public interface StreamProcessor {
    /**
     * TODO
     * 
     * @param in TODO
     * @param out TODO
     * 
     * @return An exit code of the execution result,
     *         which defined by the authors of implementations.
     */
    public int process(InputStream in, OutputStream out) throws Exception;
}
