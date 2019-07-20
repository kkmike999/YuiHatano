package net.yui.dbflow;

import android.content.Context;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

import net.yui.utils.ReflectPackageUtils;
import net.yui.utils.ReflectUtils;

import java.util.List;

/**
 * Created by kkmike999 on 2017/06/13.
 */
public class DbFlowUtils {

    public static boolean DEBUG = false;

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
            FlowConfig.Builder builder = new FlowConfig.Builder(context).openDatabasesOnInit(false);

            List<Class> classes = ReflectPackageUtils.getClasses(
                    "build/generated/source/apt/debug/",
                    "com.raizlabs.android.dbflow.config"
            );
            List<Class> kaptClasses = ReflectPackageUtils.getClasses(
                    "build/generated/source/kapt/debug/",
                    "com.raizlabs.android.dbflow.config"
            );
            classes.addAll(kaptClasses);

            // 每个模块的DatabaseHolder，都会生成在com.raizlabs.android.dbflow.config包下，自动添加进builder
            List<Class> holderClasses = ReflectPackageUtils.filter(classes, (Class) Class.forName("com.raizlabs.android.dbflow.config.DatabaseHolder"));

            for (Class holderClass : holderClasses) {
                if (holderClass.isInterface()) {
                    return;
                }
                builder.addDatabaseHolder(holderClass);
            }

            FlowManager.init(builder.build());

        } catch (Exception e) {
            if (DEBUG) {
                e.printStackTrace();
            }
        }
    }

    public static void destroy() {
        try {
            FlowManager.destroy();
        } catch (Exception e) {
            if (DEBUG) {
                e.printStackTrace();
            }
        }
    }
}
