//package android.database.sqlite;
//
//import android.database.Cursor;
//
//import net.kb.test.library.CGLibProxy;
//
//import java.io.File;
//import java.sql.SQLException;
//
///**
// * Created by kkmike999 on 2017/6/12.
// */
//public class SQLiteDatabase {
//
//    public static SQLiteDatabase openOrCreateDatabase(File file, SQLiteDatabase.CursorFactory factory) throws SQLException {
//        ShadowSQLiteDatabase shadowSQLiteDatabase = new ShadowSQLiteDatabase(file.getPath(), 0, null);
//
//        System.out.println("openOrCreateDatabase");
//
//        return new CGLibProxy().proxy(SQLiteDatabase.class, shadowSQLiteDatabase);
//    }
//
//    /**
//     * Used to allow returning sub-classes of {@link Cursor} when calling query.
//     */
//    public interface CursorFactory {
//        /**
//         * See {@link SQLiteCursor#SQLiteCursor(SQLiteCursorDriver, String, SQLiteQuery)}.
//         */
//        public Cursor newCursor(SQLiteDatabase db, SQLiteCursorDriver masterQuery, String editTable, SQLiteQuery query);
//    }
//}
