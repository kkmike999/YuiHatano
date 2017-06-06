package android.content;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import net.kkmike.sptest.SharedPreferencesHelper;

/**
 * Created by kkmike999 on 2017/05/26.
 */
public class ShadowContext {

    private Resources resources;

    public ShadowContext(Resources resources) {
        this.resources = resources;
    }

    @NonNull
    public final String getString(@StringRes int resId) {
        return getResources().getString(resId);
    }

    public Resources getResources() {
        return resources;
    }

    public SharedPreferences getSharedPreferences(String name, int mode) {
        return SharedPreferencesHelper.getInstance(name);
    }

//    public Context getApplicationContext() {
//        return this;
//    }
}
