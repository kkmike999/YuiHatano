package android.app;

import android.content.Context;
import android.content.ShadowContext;
import android.content.res.Resources;

/**
 * Created by kkmike999 on 2017/6/12.
 */
public class ShadowApplication extends ShadowContext {

    Application shadowApplication;

    public ShadowApplication(Resources resources) {
        super(resources);
    }

    @Override
    public Context getApplicationContext() {
        return shadowApplication;
    }

    @Override
    public void setProxyObject(Object proxyObject) {
        super.setProxyObject(proxyObject);

        shadowApplication = (Application) proxyObject;
    }
}
