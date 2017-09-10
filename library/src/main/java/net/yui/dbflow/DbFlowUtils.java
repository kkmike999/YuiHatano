package net.yui.dbflow;

import java.lang.reflect.Method;

/**
 * Created by kkmike999 on 2017/06/13.
 */
public class DbFlowUtils {

    public static void destroy() {
        try {
            Class  clazz         = Class.forName("com.raizlabs.android.dbflow.config.FlowManager");
            Method destroyMethod = clazz.getDeclaredMethod("destroy");
            destroyMethod.setAccessible(true);

            destroyMethod.invoke(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
