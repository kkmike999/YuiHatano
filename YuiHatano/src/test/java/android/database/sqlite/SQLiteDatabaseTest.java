package android.database.sqlite;

import android.database.Cursor;

import net.yui.YuiCase;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by kkmike999 on 2019/08/27.
 */
public class SQLiteDatabaseTest extends YuiCase {
    SQLiteDatabase db;

    //    SELECT COUNT(*) AS c FROM sqlite_master WHERE type ='table' AND name ='BEAN'
    //    CREATE TABLE IF NOT EXISTS BEAN ( id INTEGER PRIMARY KEY AUTOINCREMENT,classId INTEGER,settleTime INTEGER,isOk NUMERIC )
    //    INSERT INTO BEAN (classId, settleTime, isOk) VALUES (1, 1000, 'true')
    //    SELECT COUNT(*) AS c FROM sqlite_master WHERE type ='table' AND name ='BEAN'
    //    SELECT * FROM BEAN
    @Before
    public void setUp() {
        db = getContext().openOrCreateDatabase("build/test.db", 0, null);
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
                    int type  = cursor.getType(i);
                    int value = cursor.getInt(i);

                    System.out.println("column=" + value);

                    // NUMERIC类型下，存Boolean，会转变为integer
                    // https://www.runoob.com/sqlite/sqlite-data-types.html
                    Assert.assertEquals(Cursor.FIELD_TYPE_INTEGER, type);
                    Assert.assertEquals(1, value);
                }
            }
        }
    }
}
