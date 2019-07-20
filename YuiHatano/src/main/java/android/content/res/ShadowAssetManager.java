package android.content.res;

import android.shadow.Shadow;
import android.support.annotation.NonNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by kkmike999 on 2017/06/12.
 */
public class ShadowAssetManager implements Shadow {

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

    AssetManager proxy;

    @Override
    public void setProxyObject(Object proxyObject) {
        proxy = (AssetManager) proxyObject;
    }

    @NonNull
    public InputStream open(@NonNull String fileName) throws IOException {
        return open(fileName, ACCESS_STREAMING);
    }

    @NonNull
    public InputStream open(@NonNull String fileName, int accessMode) throws IOException {
        return null;
    }


    public String[] list(String path) throws IOException {
        File     assetsPath = new File("build/intermediates/bundles/debug/assets/" + path);
        String   abPath     = assetsPath.getAbsolutePath();
        String[] list       = assetsPath.list();
        return list == null ? new String[0] : list;
    }
}
