package android.database.sqlite;

import android.database.Cursor;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by kkmike999 on 2019/08/27.
 */
@RunWith(AndroidJUnit4.class)
public class SQLiteDatabaseTest {
    SQLiteDatabase db;

    //    SELECT COUNT(*) AS c FROM sqlite_master WHERE type ='table' AND name ='BEAN'
    //    CREATE TABLE IF NOT EXISTS BEAN ( id INTEGER PRIMARY KEY AUTOINCREMENT,classId INTEGER,settleTime INTEGER,isOk NUMERIC )
    //    INSERT INTO BEAN (classId, settleTime, isOk) VALUES (1, 1000, 'true')
    //    SELECT COUNT(*) AS c FROM sqlite_master WHERE type ='table' AND name ='BEAN'
    //    SELECT * FROM BEAN
    @Before
    public void setUp() {
        db = InstrumentationRegistry.getTargetContext().openOrCreateDatabase("test", 0, null);
        db.execSQL("DROP TABLE IF EXISTS BEAN");
        db.execSQL("CREATE TABLE IF NOT EXISTS BEAN (isOk NUMERIC)");
    }

    /**
     * 测试 SQLiteDatabase 如何处理Boolean类型
     */
    @Test
    public void testBoolean() {
        db.execSQL("INSERT INTO BEAN (isOk) VALUES (?)", new Object[]{true});
        Cursor cursor = db.rawQuery("SELECT * FROM BEAN", null);
        while (cursor.moveToNext()) {
            int columnCount = cursor.getColumnCount();
            if (columnCount > 0) {
                for (int i = 0; i < columnCount; i++) {

                    String column = cursor.getColumnName(i);
                    /**{@link Cursor#FIELD_TYPE_NULL}等*/
                    int    type  = cursor.getType(i);
                    String value = cursor.getString(i);

                    System.out.println("column=" + value);

                    // NUMERIC类型下，存Boolean，会转变为integer
                    // https://www.runoob.com/sqlite/sqlite-data-types.html
                    Assert.assertEquals(type, Cursor.FIELD_TYPE_INTEGER);
                }
            }
        }
    }
}
