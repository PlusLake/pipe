package io.github.pluslake.pipe.stream.compression;

import java.util.zip.*;

import io.github.pluslake.pipe.*;

/**
 * A simple reference implementation of compressing/extracting stream using Zip. You may want to
 * implements your own StreamProcessors for your own purposes.
 */
public class ZipStreamProcessors {
    public static final int BUFFER_SIZE = 1024;

    /** Returns a StreamProcessor that compressing data with GZip. */
    public static StreamProcessor compress(String filename) {
        return (in, out) -> {
            ZipOutputStream stream = new ZipOutputStream(out);
            stream.putNextEntry(new ZipEntry(filename));
            StreamWriter.write(in, out);
            return 0;
        };
    }

    /** Returns a StreamProcessor that extracting data with GZip. */
    public static StreamProcessor extract() {
        return (in, out) -> {
            ZipInputStream stream = new ZipInputStream(in);
            stream.getNextEntry();
            StreamWriter.write(stream, out);
            return 0;
        };
    }
}
