package android.content;

import android.content.res.ShadowResources;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

/**
 * Created by kkmike999 on 2017/05/26.
 */
public class ShadowContext {

    private ShadowResources resources;

    public ShadowContext(ShadowResources resources) {
        this.resources = resources;
    }

    @NonNull
    public final String getString(@StringRes int resId) {
        return getResources().getString(resId);
    }

    public ShadowResources getResources() {
        return resources;
    }

//    public Context getApplicationContext() {
//        return this;
//    }
}
