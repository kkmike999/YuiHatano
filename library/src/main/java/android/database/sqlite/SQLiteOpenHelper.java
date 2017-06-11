package android.database.sqlite;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.os.Build;

/**
 * Created by kkmike999 on 2017/06/07.
 */
public abstract class SQLiteOpenHelper {

    ShadowSQLiteOpenHelper shadowSQLiteOpenHelper;

    public SQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        this(context, name, factory, version, null);
    }

    public SQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        if (version < 1) {
            throw new IllegalArgumentException("Version must be >= 1, was " + version);
        }

        shadowSQLiteOpenHelper = new ShadowSQLiteOpenHelper(this, context, name, factory, version, errorHandler);
    }

    public SQLiteDatabase getWritableDatabase() {
        synchronized (this) {
            return getDatabaseLocked(true);
        }
    }

    public SQLiteDatabase getReadableDatabase() {
        synchronized (this) {
            return getDatabaseLocked(false);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private SQLiteDatabase getDatabaseLocked(boolean writable) {
        return shadowSQLiteOpenHelper.getDatabaseLocked(writable);
    }

    public void onConfigure(SQLiteDatabase db) {}

    public abstract void onCreate(SQLiteDatabase db);

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        throw new SQLiteException("Can't downgrade database from version " + oldVersion + " to " + newVersion);
    }

    public abstract void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);

    public void onOpen(SQLiteDatabase db) {}
}
