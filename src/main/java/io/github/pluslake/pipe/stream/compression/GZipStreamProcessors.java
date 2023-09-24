package io.github.pluslake.pipe.stream.compression;

import java.util.zip.*;

import io.github.pluslake.pipe.*;

/**
 * A simple reference implementation of compressing/extracting stream using GZip. You may want to
 * implements your own StreamProcessors for your own purposes.
 */
public class GZipStreamProcessors {
    public static final int BUFFER_SIZE = 1024;

    /** Returns a StreamProcessor that compressing data with GZip. */
    public static StreamProcessor compress() {
        return (in, out) -> {
            StreamWriter.write(in, new GZIPOutputStream(out), BUFFER_SIZE);
            return 0;
        };
    }

    /** Returns a StreamProcessor that extracting data with GZip. */
    public static StreamProcessor extract() {
        return (in, out) -> {
            StreamWriter.write(new GZIPInputStream(in), out, BUFFER_SIZE);
            return 0;
        };
    }
}
