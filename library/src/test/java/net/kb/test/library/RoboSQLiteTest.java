package net.kb.test.library;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
    public void testSelectCondition() {
//        testInsert();
        testCreateTable();

        // 插入数据
        {
            ContentValues cv = new ContentValues();
            cv.put("id", 1);
            cv.put("name", "leo");

            db.insert("person", null, cv);

            cv.put("name", "leo1");
            db.insert("person", null, cv);
        }

//        Cursor cursor = db.rawQuery("SELECT * FROM person WHERE name BETWEEN 'leo' AND 'leo1'", null);
//        Cursor cursor = db.rawQuery("SELECT * FROM person WHERE name BETWEEN ? AND ?", new String[]{"leo", "leo1"});
        Cursor cursor = db.rawQuery("SELECT * FROM person WHERE name in (?,?)", new String[]{"leo", "leo1"});

        int count = cursor.getCount();

        List<Person> persons = getPersons(cursor);

        System.out.println(persons.toString());
    }

    @Test
    public void testQuery() {
        testCreateTable();

        // 插入数据
        {
            ContentValues cv = new ContentValues();
            cv.put("id", 1);
            cv.put("name", "leo");

            db.insert("person", null, cv);

            cv.put("name", "leo1");
            db.insert("person", null, cv);
        }

        Cursor cursor = db.query(true, "person", new String[]{"id", "name"}, "(id BETWEEN ? and ?) and name LIKE ?", new String[]{"1", "2", "leo%"}, "", "", "id", "10");

        List<Person> persons = getPersons(cursor);

        System.out.println(persons.toString());
    }

    @Test
    public void testDropTable() {
        testInsert();

        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type ='table'", null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                //添加异常捕获.忽略删除所有表时出现的异常:
                //table sqlite_sequence may not be dropped
                String table = cursor.getString(0);
                db.execSQL("DROP TABLE " + table);
            }
            cursor.close();
        }
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
