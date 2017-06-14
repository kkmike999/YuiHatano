package net.kb.test.library.utils;

import android.database.sqlite.ShadowSQLiteDatabase;
import android.database.sqlite.ShadowSQLiteOpenHelper;
import android.database.sqlite.ShadowSQLiteStatement;

/**
 * Created by kkmike999 on 2017/06/13.
 */
public class DebugHook {

    /**
     * 设置是否输出SQL语句
     *
     * @param isDebug
     */
    public static void setDebug(boolean isDebug) {
        ShadowSQLiteDatabase.DEBUG = isDebug;
        ShadowSQLiteStatement.DEBUG = isDebug;
    }

    /**
     * 设置是否输出"PRAGMA"语句
     *
     * @param isDebug
     */
    public static void setPragmaDebug(boolean isDebug) {
        ShadowSQLiteStatement.DEBUG_PRAGMA = isDebug;
    }

    /**
     * 设置{@linkplain ShadowSQLiteOpenHelper}是否开启debug模式
     *
     * @param isDebug
     */
    public static void setOpenHelperDebug(boolean isDebug) {
        ShadowSQLiteOpenHelper.DEBUG = isDebug;
    }
}
