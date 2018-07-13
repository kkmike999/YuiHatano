package net.yui.dbflow;

import android.content.Context;

import net.yui.utils.ReflectPackageUtils;
import net.yui.utils.ReflectUtils;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by kkmike999 on 2017/06/13.
 */
public class DbFlowUtils {

    /**
     * 是否依赖了DbFlow
     *
     * @return
     */
    public static boolean isDependOnDbFlow() {
        // 判断是否引用dbflow
        if (ReflectUtils.hasClass("com.raizlabs.android.dbflow.config.FlowManager")) {
            return true;
        }
        return false;
    }

    /**
     * @param context 只能是{@linkplain Context}类型，不能是子类，例如{@linkplain android.app.Application}
     */
    public static void init(Context context) {
        try {
            Object builder = ReflectUtils.newObject("com.raizlabs.android.dbflow.config.FlowConfig$Builder", context);

            if (builder == null) {
                return;
            }
            addDatabaseHolder(builder);

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

    /**
     * 每个模块的DatabaseHolder，都会生成在com.raizlabs.android.dbflow.config包下，自动添加进builder
     *
     * @param builder
     */
    private static void addDatabaseHolder(Object builder) {
//       例子， new FlowConfig.Builder(getApplication()).addDatabaseHolder(GeneratedDatabaseHolder.class)
//                                                .build();
        try {
            // 需要授权/data/app目录权限：[《Android获取data文件夹权限》](https://blog.csdn.net/JavaMoo/article/details/60963328)
            List<Class> classes = ReflectPackageUtils.getClasses(
                    "build/generated/source/apt/debug/",
                    "com.raizlabs.android.dbflow.config"
            );
            List<Class> holderClasses = ReflectPackageUtils.filter(classes, (Class) Class.forName("com.raizlabs.android.dbflow.config.DatabaseHolder"));

            for (Class holderClass : holderClasses) {
                if (holderClass.isInterface()) {
                    return;
                }
                ReflectUtils.invoke(builder, "addDatabaseHolder", holderClass);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
