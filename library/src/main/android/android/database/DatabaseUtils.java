package android.database;

/**
 * Created by kkmike999 on 2017/06/12.
 */
public class DatabaseUtils {

    public static String sqlEscapeString(String value) {
        return ShadowDatabaseUtils.sqlEscapeString(value);
    }
}
