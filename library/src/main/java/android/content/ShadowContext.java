package android.content;

import android.content.res.Resources;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

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

    public void putSQLiteDatabase(String name, SQLiteDatabase db) {
        dbMap.put(name, db);
    }

    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory) {
        return openOrCreateDatabase(name, mode, factory, null);
    }

    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory, DatabaseErrorHandler errorHandler) {
        return dbMap.get(name);
    }

    @Override
    public String toString() {
        return "ShadowContext@" + hashCode() + "{}";
    }
}
