package net.yui.app.greenDAO;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.yui.YuiCase;

import org.junit.Before;
import org.junit.Test;

/**
 * Created by kkmike999 on 2017/06/09.
 */
public class GreenDAOSqlTest extends YuiCase {

    SQLiteDatabase database;

    @Before
    public void setUp() throws Exception {
        database = newSQLiteDatabase("test.db");
    }

    @Test
    public void testInsert() {
//        String create  = "Creating tables for schema version 1000";
        String create2 = "CREATE TABLE \"USER\" (\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ,\"UID\" INTEGER NOT NULL UNIQUE ,\"NAME\" TEXT);";
        String insert  = "INSERT INTO \"USER\" (\"_id\", \"UID\", \"NAME\")VALUES(null, 1, 'kk1')";
        String query   = "SELECT T.\"_id\", T. \"UID\", T. \"NAME\" FROM \"USER\" T";

//        database.execSQL(create);
        database.execSQL(create2);
        database.execSQL(insert);
        Cursor cursor = database.rawQuery(query, null);

        System.out.println();
    }
}
