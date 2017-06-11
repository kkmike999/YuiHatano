package android.app;

import android.content.Context;
import android.content.ShadowContext;
import android.content.res.Resources;

/**
 * Created by kkmike999 on 2017/6/12.
 */
public class ShadowApplication extends ShadowContext {

    Application mockApplication;

    public ShadowApplication(Resources resources) {
        super(resources);
    }

    @Override
    public Context getApplicationContext() {
        return mockApplication;
    }

    public void setApplication(Application application) {
        this.mockApplication = application;
    }
}
