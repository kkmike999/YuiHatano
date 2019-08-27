package android.content.res;

import android.support.annotation.Nullable;
import android.util.DisplayMetrics;

/**
 * Created by kkmike999 on 2019/08/26.
 */
public class Resources extends ShadowResources {

    public Resources() {
        this(null);
    }

    @Deprecated
    public Resources(AssetManager assets, DisplayMetrics metrics, Configuration config) {
        this(null);
    }

    public Resources(@Nullable ClassLoader classLoader) {}

    ////////////////////////////////////////////////////////

    /**
     * This exception is thrown by the resource APIs when a requested resource
     * can not be found.
     */
    public static class NotFoundException extends RuntimeException {
        public NotFoundException() {
        }

        public NotFoundException(String name) {
            super(name);
        }

        public NotFoundException(String name, Exception cause) {
            super(name, cause);
        }
    }

    public final class Theme {

        public Theme() {
        }
    }

    public static class ThemeKey implements Cloneable {}
}
