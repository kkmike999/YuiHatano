package android.app;

import android.content.Context;
import android.content.ShadowContext;
import android.content.res.Resources;

import java.util.ArrayList;

/**
 * Created by kkmike999 on 2017/6/12.
 */
public class ShadowApplication extends ShadowContext {

    Application shadowApplication;

    private ArrayList<Application.ActivityLifecycleCallbacks> mActivityLifecycleCallbacks = new ArrayList<>();

    public ShadowApplication(Resources resources) {
        super(resources);
    }

    @Override
    public Context getApplicationContext() {
        return shadowApplication;
    }

    public void registerActivityLifecycleCallbacks(Application.ActivityLifecycleCallbacks callback) {
        synchronized (mActivityLifecycleCallbacks) {
            mActivityLifecycleCallbacks.add(callback);
        }
    }

    @Override
    public void setProxyObject(Object proxyObject) {
        super.setProxyObject(proxyObject);

        shadowApplication = (Application) proxyObject;
    }
}
