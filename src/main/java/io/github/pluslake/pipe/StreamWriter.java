package io.github.pluslake.pipe;

import java.io.*;

public class StreamWriter {
    private static final int BUFFER_SIZE = 1024;

    public static void write(InputStream in, OutputStream out) throws IOException {
        write(in, out, BUFFER_SIZE);
    }

    public static void write(InputStream in, OutputStream out, int bufferSize) throws IOException {
        for (byte[] buffer; (buffer = in.readNBytes(bufferSize)).length > 0; out.write(buffer));
        out.close();
    }
}
