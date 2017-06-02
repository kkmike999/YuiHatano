package net.kb.test.library;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

/**
 * Created by kkmike999 on 2017/06/01.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class RoboSQLiteTest {

    SQLiteDatabase db;

    @Before
    public void setUp() throws Exception {
        db = new MySQLiteOpenHelper(RuntimeEnvironment.application, "test", null, 1).getWritableDatabase();
    }

    @Test
    public void testCreateTable() {
        String sql = "create table person (id integer, name string)";

        db.execSQL(sql);
    }

    @Test
    public void testInsert() {
        testCreateTable();

        ContentValues cv = new ContentValues();
        cv.put("id", 1);
        cv.put("name", "leo");

        db.insert("person", null, cv);
        db.insert("person", null, cv);
        db.insert("person", null, cv);
    }

    @Test
    public void testDeleted() {
        testInsert();

        int row = db.delete("person", "name=?", new String[]{"leo"});

        System.out.println("影响行数 row=" + row);
    }

    @Test
    public void testUpdate() {
        testInsert();

        ContentValues cv = new ContentValues();
        cv.put("id", 1);
        cv.put("name", "leo_update");

        int row = db.update("person", cv, "name=?", new String[]{"leo"});

        System.out.println("影响行数 row=" + row);
    }
}
