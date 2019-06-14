package android.content.res;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by kkmike999 on 2019/06/14.
 */
public class AssetManager {

    /**
     * Mode for {@link #open(String, int)}: no specific information about how
     * data will be accessed.
     */
    public static final int ACCESS_UNKNOWN   = 0;
    /**
     * Mode for {@link #open(String, int)}: Read chunks, and seek forward and
     * backward.
     */
    public static final int ACCESS_RANDOM    = 1;
    /**
     * Mode for {@link #open(String, int)}: Read sequentially, with an
     * occasional forward seek.
     */
    public static final int ACCESS_STREAMING = 2;
    /**
     * Mode for {@link #open(String, int)}: Attempt to load contents into
     * memory, for fast small reads.
     */
    public static final int ACCESS_BUFFER    = 3;

    @NonNull
    public InputStream open(@NonNull String fileName) throws IOException {
        return open(fileName, ACCESS_STREAMING);
    }

    @NonNull
    public InputStream open(@NonNull String fileName, int accessMode) throws IOException {
        return null;
    }
}