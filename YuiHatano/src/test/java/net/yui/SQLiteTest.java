package net.yui;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.ShadowSQLiteDatabase;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kkmike999 on 2017/06/01.
 */
public class SQLiteTest {

    SQLiteDatabase       db;
    ShadowSQLiteDatabase sdb;

    @Before
    public void setUp() throws Exception {
        sdb = new ShadowSQLiteDatabase("build/db/sample.db", 0, null);//ShadowSQLiteDatabaseHelper.newSqliteDatabase();

        db = new CGLibProxy().proxy(SQLiteDatabase.class, sdb);
    }

    @After
    public void tearDown() throws Exception {
        db.close();

        new File("build/db/sample.db").delete();
    }

    @Test
    public void testCheckTableExist() throws SQLException {
        boolean isExist = sdb.checkExist("person");

        if (!isExist) {
            db.execSQL("create table person (id integer, name string)");

            isExist = sdb.checkExist("person");

            Assert.assertTrue(isExist);
        }

        System.out.println("table is " + (isExist ? "" : "not ") + "exist.");
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

        // UPDATE person SET id=1,name='leo_update', WHERE name='leo'
        int row = db.update("person", cv, "name=?", new String[]{"leo"});

        System.out.println("影响行数 row=" + row);

        Cursor cursor = db.rawQuery("SELECT * FROM person", null);

        List<Person> persons = getPersons(cursor);

        System.out.println(persons.toString());
    }

    @Test
    public void testSelect() throws SQLException {
        testInsert();

        Cursor cursor = db.rawQuery("SELECT * FROM person WHERE name=?", new String[]{"leo"});

        int count = cursor.getCount();

        List<Person> persons = getPersons(cursor);

        Assert.assertEquals(count, persons.size());

        System.out.println(persons.toString());
    }

    @Test
    public void testQuery() throws SQLException {
        testCheckTableExist();

        // 插入数据
        {
            ContentValues cv = new ContentValues();
            cv.put("id", 1);
            cv.put("name", "leo");

            db.insert("person", null, cv);

            cv.put("name", "leo1");
            db.insert("person", null, cv);
        }

        Cursor cursor = db.query(false, "person", new String[]{"id", "name"}, "(id BETWEEN ? and ?) and name LIKE ?", new String[]{"1", "2", "leo%"}, "", null, "id", "10");

        int count = cursor.getCount();

        List<Person> persons = getPersons(cursor);

        Assert.assertEquals(count, persons.size());

        System.out.println(persons.toString());
    }

    private List<Person> getPersons(Cursor cursor) {
        List<Person> persons = new ArrayList<>();

        while (cursor.moveToNext()) {
            int    id   = cursor.getInt(cursor.getColumnIndex("id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));

            persons.add(new Person(id, name));
        }

        cursor.close();

        return persons;
    }
}
