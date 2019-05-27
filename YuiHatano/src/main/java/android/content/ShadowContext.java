package android.content;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.ShadowAssetManager;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.ShadowSQLiteDatabase;
import android.os.Build;
import android.shadow.Shadow;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import net.kkmike.sptest.SharedPreferencesHelper;
import net.yui.CGLibProxy;
import net.yui.utils.DbPathUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kkmike999 on 2017/05/26.
 */
public class ShadowContext implements Shadow {

    private Resources                   resources;
    private Context                     mockContext;
    private Application                 application;
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

    public Context getApplicationContext() {
        return application == null ? mockContext : application;
    }

    public File getDatabasePath(String name) {
        return new File("build/db");
    }

    public AssetManager getAssets() {
        return new CGLibProxy().proxy(AssetManager.class, new ShadowAssetManager());
    }

    //////////////////////////// file //////////////////////////
    public File getFilesDir() {
        return getAndCreateDir("build/files/");
    }

    public File getCacheDir() {
        return getAndCreateDir("build/cache");
    }

    public File getDataDir() {
        return getAndCreateDir("build/data");
    }

    private File getAndCreateDir(String dirPath) {
        File dir = new File(dirPath);

        if (!dir.exists()) {
            dir.mkdirs();
        }

        return dir;
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
        String path = DbPathUtils.getDbPath(name);

        // 不同api版本，构造函数参数不同
        ShadowSQLiteDatabase sdb = new ShadowSQLiteDatabase(path, 0, null);
        SQLiteDatabase       db  = new CGLibProxy().proxy(SQLiteDatabase.class, sdb);

        sdb.setMockDatabase(db);

        putSQLiteDatabase(name, db);

        return db;
    }

    public boolean deleteDatabase(String name) {
        SQLiteDatabase db = dbMap.get(name);
        //        db.execSQL("DROP DATABASE " + name);
        db.close();

        String path = DbPathUtils.getDbPath(name);

        new File(path).delete();

        return true;
    }

    public Map<String, SQLiteDatabase> getDbMap() {
        return dbMap;
    }

    /////////////////////////////   SQLiteDatabase end  /////////////////////////////

    @Override
    public String toString() {
        return "ShadowContext@" + hashCode() + "{}";
    }

    @Override
    public void setProxyObject(Object proxyObject) {
        mockContext = (Context) proxyObject;
    }

    public void setApplication(Application application) {
        this.application = application;
    }
}
