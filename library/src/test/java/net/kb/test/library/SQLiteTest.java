package net.kb.test.library;

import android.content.ContentValues;
import android.database.sqlite.ShadowSQLiteDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.sql.SQLException;

/**
 * Created by kkmike999 on 2017/06/01.
 */
public class SQLiteTest {

    ShadowSQLiteDatabase db;

    @Before
    public void setUp() throws Exception {
        db = new ShadowSQLiteDatabase("build/sample.db", 0, null);//ShadowSQLiteDatabaseHelper.newSqliteDatabase();
    }

    @After
    public void tearDown() throws Exception {
        new File("build/sample.db").delete();
    }

    @Test
    public void testCheckTableExist() throws SQLException {
        boolean exist = db.checkExist("person");

        if (!exist) {
            db.execSQL("create table person (id integer, name string)");
        }

        System.out.println(exist);
    }

    @Test
    public void testInsert() throws SQLException {
        testCheckTableExist();

        ContentValues cv = new ContentValues();
        cv.put("id", 1);
        cv.put("name", "leo");

        db.insert("person", null, cv);
        db.insert("person", null, cv);
        db.insert("person", null, cv);
    }

    @Test
    public void testDeleted() throws SQLException {
        testInsert();

        int row = db.delete("person", "name=?", new String[]{"leo"});

        System.out.println("影响行数 row=" + row);
    }

    @Test
    public void testUpdate() throws SQLException {
        testInsert();

        ContentValues cv = new ContentValues();
        cv.put("id", 1);
        cv.put("name", "leo_update");

        // UPDATE person SET name=?,id=? WHERE name='leo'
        int row = db.update("person", cv, "name=?", new String[]{"leo"});

        System.out.println("影响行数 row=" + row);
    }
}
