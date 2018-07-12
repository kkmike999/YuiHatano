package net.yui.dbflow;

import android.content.Context;

import net.yui.utils.ReflectUtils;

import java.lang.reflect.Method;

/**
 * Created by kkmike999 on 2017/06/13.
 */
public class DbFlowUtils {

    /**
     * @param context 只能是{@linkplain Context}类型，不能是子类，例如{@linkplain android.app.Application}
     */
    public static void init(Context context) {
        try {
            Object builder    = ReflectUtils.newObject("com.raizlabs.android.dbflow.config.FlowConfig$Builder", context);
            Object flowConfig = ReflectUtils.invoke(builder, "build");
            ReflectUtils.invokeStatic("com.raizlabs.android.dbflow.config.FlowManager", "init", flowConfig);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
