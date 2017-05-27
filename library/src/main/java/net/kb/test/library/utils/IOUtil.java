package net.kb.test.library.utils;

import android.os.Build;

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
        if (Build.VERSION.SDK_INT >= 19) {
            return;
        }
        try {
            closeable.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
