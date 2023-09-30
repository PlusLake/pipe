package io.github.pluslake.pipe.stream.compression;

import java.util.zip.*;

import io.github.pluslake.pipe.StreamProcessor;

/**
 * A simple reference implementation of compressing/extracting stream using Zip. You may want to
 * implements your own StreamProcessors for your own purposes.
 */
public class ZipStreamProcessors {
    /**
     * Returns a StreamProcessor that compressing data with Zip.
     * 
     * @param filename the filename inside the zip file
     * @return a new StreamProcessor that compressing data with Zip
     */
    public static StreamProcessor compress(String filename) {
        return (in, out) -> {
            ZipOutputStream stream = new ZipOutputStream(out);
            stream.putNextEntry(new ZipEntry(filename));
            in.transferTo(stream);
            stream.close();
            return 0;
        };
    }

    /**
     * Returns a StreamProcessor that extracting data with Zip.
     * 
     * @return a new StreamProcessor that compressing data with Zip
     */
    public static StreamProcessor extract() {
        return (in, out) -> {
            ZipInputStream stream = new ZipInputStream(in);
            stream.getNextEntry();
            stream.transferTo(out);
            out.close();
            return 0;
        };
    }
}
