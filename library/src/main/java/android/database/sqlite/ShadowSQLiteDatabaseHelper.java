package android.database.sqlite;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by kkmike999 on 2017/06/01.
 */
public class ShadowSQLiteDatabaseHelper {

    public static ShadowSQLiteDatabase newSqliteDatabase() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor constructor = ShadowSQLiteDatabase.class.getDeclaredConstructor();

        return (ShadowSQLiteDatabase) constructor.newInstance();
    }

    public static ShadowSQLiteSession getThreadSession(ShadowSQLiteDatabase db) {
        try {
            Method method = ShadowSQLiteDatabase.class.getDeclaredMethod("getThreadSession");
            method.setAccessible(true);

            return (ShadowSQLiteSession) method.invoke(db);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected static ShadowSQLiteStatement newSQLiteStatement(ShadowSQLiteDatabase db, String sql, Object[] bindArgs) {
        try {
//            ShadowSQLiteStatement statement = new ShadowSQLiteStatement(this, sql.toString(), bindArgs);
            Constructor<ShadowSQLiteStatement> constructor = ShadowSQLiteStatement.class.getDeclaredConstructor(ShadowSQLiteDatabase.class, String.class, Object[].class);
            ShadowSQLiteStatement              statement   = constructor.newInstance(db, sql.toString(), bindArgs);

            return statement;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
