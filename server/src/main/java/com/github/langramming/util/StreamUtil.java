package com.github.langramming.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StreamUtil {

    public static void copy(InputStream inputStream, OutputStream outputStream) throws IOException {
        copy(8192, inputStream, outputStream);
    }

    public static void copy(int bufferSize, InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] buffer = new byte[bufferSize];
        int c;
        while ((c = inputStream.read(buffer)) >= 0) {
            outputStream.write(buffer, 0, c);
        }
    }

}
