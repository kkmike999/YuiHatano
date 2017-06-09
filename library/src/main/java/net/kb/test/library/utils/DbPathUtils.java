package net.kb.test.library.utils;

/**
 * Created by kkmike999 on 2017/06/08.
 */
public class DbPathUtils {

    public static String getDbPath(String dbName) {
        return "build/" + dbName + ".dp";
    }
}
