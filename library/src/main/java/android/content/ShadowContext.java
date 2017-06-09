package android.content;

import android.annotation.TargetApi;
import android.content.res.Resources;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.ShadowSQLiteDatabase;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import net.kb.test.library.CGLibProxy;
import net.kb.test.library.utils.DbPathUtils;
import net.kkmike.sptest.SharedPreferencesHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kkmike999 on 2017/05/26.
 */
public class ShadowContext {

    private Resources resources;
    private Context   mockContext;
    private Map<String, SQLiteDatabase> dbMap = new HashMap<>();

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

    public void setMockContext(Context mockContext) {
        this.mockContext = mockContext;
    }

    public Context getApplicationContext() {
        return mockContext;
    }

    /////////////////////////////   SQLiteDatabase    /////////////////////////////
    public void putSQLiteDatabase(String name, SQLiteDatabase db) {
        dbMap.put(name, db);
    }

    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory) {
        return openOrCreateDatabase(name, mode, factory, null);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory, DatabaseErrorHandler errorHandler) {
        if (dbMap.containsKey(name)) {
            return dbMap.get(name);
        }
        // 创建数据库
        try {
            String path = DbPathUtils.getDbPath(name);

            ShadowSQLiteDatabase sdb = new ShadowSQLiteDatabase(path, 0, null);
            SQLiteDatabase       db  = new CGLibProxy().getInstance(SQLiteDatabase.class, sdb);

            sdb.setMockDatabase(db);

            putSQLiteDatabase(name, db);

            return db;
        } catch (java.sql.SQLException e) {
            throw new android.database.SQLException("", e);
        }
    }

    public Map<String, SQLiteDatabase> getDbMap() {
        return dbMap;
    }

    /////////////////////////////   SQLiteDatabase end  /////////////////////////////

    @Override
    public String toString() {
        return "ShadowContext@" + hashCode() + "{}";
    }
}
