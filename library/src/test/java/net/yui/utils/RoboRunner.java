package net.yui.utils;

import android.os.Build;

import org.junit.runners.model.InitializationError;
import org.robolectric.RoboSettings;
import org.robolectric.RobolectricTestRunner;

/**
 * Created by kkmike999 on 2016/05/26.
 */
public class RoboRunner extends RobolectricTestRunner {

    private static final int TARGET_SDK_VERSION = Build.VERSION_CODES.LOLLIPOP;
    private static final int MIN_SDK_VERSION    = Build.VERSION_CODES.JELLY_BEAN;

    public RoboRunner(Class<?> klass) throws InitializationError {
        super(klass);

        RoboSettings.setMavenRepositoryUrl("http://maven.aliyun.com/nexus/content/groups/public/");
        RoboSettings.setMavenRepositoryId("alimaven");
    }
}
