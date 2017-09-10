package net.yui.xutils;

import android.app.Application;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

/**
 * Created by kkmike999 on 2017/6/12.
 */
public class XUtilsDbUtils {

    public static boolean DEBUG = false;

    public static void setUp() {
        // 清除缓存
        try {
            Field appField = Class.forName("org.xutils.x$Ext").getDeclaredField("app");
            appField.setAccessible(true);

            appField.set(null, null);
        } catch (Exception e) {
            if (XUtilsDbUtils.DEBUG) {
                e.printStackTrace();
            }
        }
    }

    public static void init(Application app) {
        try {
            Method initMethod = Class.forName("org.xutils.x$Ext").getDeclaredMethod("init", Application.class);
            initMethod.setAccessible(true);

            initMethod.invoke(null, app);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 清理FinalDb缓存
     */
    public static void clearAndCloseAndDrop() {
        try {
            Class fdbClazz = Class.forName("org.xutils.db.DbManagerImpl");

            Field daoMapField = fdbClazz.getDeclaredField("DAO_MAP");
            daoMapField.setAccessible(true);

            Map<String, Object> DAO_MAP = (Map<String, Object>) daoMapField.get(null);

            // drop
            Method dropMethod = Class.forName("org.xutils.db.table.DbBase").getDeclaredMethod("dropDb");
            dropMethod.setAccessible(true);

            // 关闭&清理数据库
            Method closeMethod = fdbClazz.getDeclaredMethod("close");
            closeMethod.setAccessible(true);

            Collection dbManagers = DAO_MAP.values();

            for (Object dbManager : dbManagers) {
                // dropDb
                dropMethod.invoke(dbManager);
                // close
                closeMethod.invoke(dbManager);
            }

            // 清理缓存
            DAO_MAP.clear();
        } catch (Exception e) {
            if (XUtilsDbUtils.DEBUG) {
                e.printStackTrace();
            }
        }
    }
}
