package android.database.sqlite;

import android.content.Context;

import net.kb.test.library.KBSharedPrefCase;

import org.junit.Test;

/**
 * Created by kkmike999 on 2017/06/07.
 */
public class SQLiteOpenHelperTest extends KBSharedPrefCase {

    @Test
    public void testNewSQLiteOpenHelper() {
        Context          context = getContext();
        SQLiteOpenHelper helper  = new MySQLOpenHelper(context, "name");

        SQLiteDatabase db = helper.getWritableDatabase();

        System.out.println();
    }

}