package android.database.sqlite;

/**
 * Created by kkmike999 on 2017/06/08.
 */
public class SQLiteOpenHelperHook {

    protected static boolean DEBUG = false;

    public static void setDebug(boolean isDebug) {
        DEBUG = isDebug;
//        try {
//            Field debugField = SQLiteOpenHelper.class.getDeclaredField("DEBUG");
//            debugField.setAccessible(true);
//
//            debugField.set(null, isDebug);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
