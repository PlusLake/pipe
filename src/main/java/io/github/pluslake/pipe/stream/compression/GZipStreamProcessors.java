package io.github.pluslake.pipe.stream.compression;

import java.util.zip.*;

import io.github.pluslake.pipe.StreamProcessor;

/**
 * A simple reference implementation of compressing/extracting stream using GZip. You may want to
 * implements your own StreamProcessors for your own purposes.
 */
public class GZipStreamProcessors {
    private static final int BUFFER_SIZE = 65536;

    /**
     * Returns a new StreamProcessor that compressing data with GZip with a default buffer size.
     * 
     * @return a new StreamProcessor that compressing data with GZip
     * @see GZipStreamProcessors#compress(int)
     */
    public static StreamProcessor compress() {
        return compress(BUFFER_SIZE);
    }

    /**
     * Returns a new StreamProcessor that compressing data with GZip with specified buffer size.
     * The instance returned should be used only once. {@link GZIPOutputStream} is used internally
     * to compress.
     * 
     * @param bufferSize the buffer size of the processor in bytes
     * @return a new StreamProcessor that compressing data with GZip
     */
    public static StreamProcessor compress(int bufferSize) {
        return (in, out) -> {
            in.transferTo(new GZIPOutputStream(out));
            out.close();
            return 0;
        };
    }

    /**
     * Returns a new StreamProcessor that extracting data with GZip with a default buffer size.
     * 
     * @return a new StreamProcessor that compressing data with GZip
     * @see GZipStreamProcessors#extract(int)
     */
    public static StreamProcessor extract() {
        return extract(BUFFER_SIZE);
    }

    /**
     * Returns a new StreamProcessor that extracting data with GZip with specified buffer size.
     * The instance returned should be used only once. {@link GZIPOutputStream} is used internally
     * to compress.
     * 
     * @param bufferSize the buffer size of the processor in bytes
     * @return a new StreamProcessor that compressing data with GZip
     */
    public static StreamProcessor extract(int bufferSize) {
        return (in, out) -> {
            new GZIPInputStream(in).transferTo(out);
            out.close();
            return 0;
        };
    }
}
