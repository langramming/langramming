package com.github.langramming.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StreamUtil {

    public static void copy(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] buffer = new byte[8192];
        int c;
        while ((c = inputStream.read(buffer)) >= 0) {
            outputStream.write(buffer, 0, c);
        }
    }

}
