package android.content.res;

import android.shadow.Shadow;

import java.io.File;
import java.io.IOException;

/**
 * Created by kkmike999 on 2017/06/12.
 */
public class ShadowAssetManager implements Shadow {

    AssetManager proxy;

    @Override
    public void setProxyObject(Object proxyObject) {
        proxy = (AssetManager) proxyObject;
    }

    public String[] list(String path) throws IOException {
        File     assetsPath = new File("build/intermediates/bundles/debug/assets/" + path);
        String   abPath     = assetsPath.getAbsolutePath();
        String[] list       = assetsPath.list();
        return list == null ? new String[0] : list;
    }
}
