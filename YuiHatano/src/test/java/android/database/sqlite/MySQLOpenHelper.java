package android.database.sqlite;

import android.content.Context;
import android.database.DatabaseErrorHandler;

public class MySQLOpenHelper extends SQLiteOpenHelper {
    public MySQLOpenHelper(Context context, String name) {
        super(context, name, null, 1);
    }

    public MySQLOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}