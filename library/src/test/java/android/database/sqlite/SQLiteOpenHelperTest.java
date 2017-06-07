package android.database.sqlite;

import android.content.Context;

import net.kb.test.library.KBSharedPrefCase;

import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Field;

/**
 * Created by kkmike999 on 2017/06/07.
 */
public class SQLiteOpenHelperTest extends KBSharedPrefCase {
    @BeforeClass
    public static void beforeClass() throws Exception {
//        ClassPool cp = ClassPool.getDefault();
////        cp.insertClassPath("/Users/kkmike999/Documents/Android Studio Project/KBUnitTest/library/tmp/android/database/sqlite");
//
//        CtClass ctClass = cp.getCtClass("android.database.sqlite.SQLiteOpenHelper");
//        ctClass.defrost();
//        Class clazz = ctClass.toClass();
//
//        Field[] fields = clazz.getDeclaredFields();
//
//        System.out.println();
    }

    @Test
    public void testNewSQLiteOpenHelper() {
        Class   clazz  = SQLiteOpenHelper.class;
        Field[] fields = clazz.getDeclaredFields();

//        for (Field field : fields) {
//            System.out.println(field.getName());
//        }
//        SQLiteOpenHelper helper = new MySQLOpenHelper(null, "name");

        Context          context = getContext();
        SQLiteOpenHelper helper  = new MySQLOpenHelper(context, "name");

        SQLiteDatabase db = helper.getWritableDatabase();

        System.out.println();
    }

}