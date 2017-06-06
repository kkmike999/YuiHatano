package net.kb.test.library.utils;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;

public class IOUtil {
    public static void closeIO(Closeable closeable) {
        if (closeable == null) {
            return;
        }
        if (closeable instanceof Flushable) {
            try {
                ((Flushable) closeable).flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            closeable.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
