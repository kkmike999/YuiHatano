package net.kb.test.library.utils;

import android.database.sqlite.ShadowSQLiteDatabase;
import android.database.sqlite.ShadowSQLiteStatement;

/**
 * Created by kkmike999 on 2017/06/13.
 */
public class DebugHook {

    public static void setDebug(boolean isDebug) {
        ShadowSQLiteDatabase.DEBUG = isDebug;
        ShadowSQLiteStatement.DEBUG = isDebug;
    }
}
