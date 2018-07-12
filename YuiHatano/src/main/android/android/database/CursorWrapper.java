package android.database;

/**
 * Created by kkmike999 on 2017/06/12.
 */
public class CursorWrapper {

    Cursor mCursor;

    public CursorWrapper(Cursor cursor) {
        mCursor = cursor;
    }

    public boolean moveToFirst() {
        return mCursor.moveToFirst();
    }

    public boolean moveToNext() {
        return mCursor.moveToNext();
    }

    public void close() {
    }
}
