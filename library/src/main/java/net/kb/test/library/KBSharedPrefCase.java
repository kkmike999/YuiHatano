package net.kb.test.library;

import android.content.Context;
import android.content.ShadowContext;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.ShadowResources;
import android.util.DisplayMetrics;

import org.junit.Rule;
import org.junit.rules.ExternalResource;

/**
 * Created by kkmike999 on 2017/05/25.
 */
public class KBSharedPrefCase {

    private Context mContext;

    @Rule
    public ExternalResource contextRule = new ExternalResource() {
        @Override
        protected void before() throws Throwable {
            // android sdk Resource只有这个构造函数
//            public Resources(AssetManager assets, DisplayMetrics metrics, Configuration config)

            ShadowResources shadowResources = new ShadowResources();
            Resources       resources       = new CGLibProxy().getInstance(Resources.class, shadowResources, new Class[]{AssetManager.class, DisplayMetrics.class, Configuration.class}, new Object[]{null, null, null});
            ShadowContext   shadowContext   = new ShadowContext(resources);

            mContext = new CGLibProxy().getInstance(Context.class, shadowContext);
        }
    };

    public Context getContext() {
        return mContext;
    }
}
