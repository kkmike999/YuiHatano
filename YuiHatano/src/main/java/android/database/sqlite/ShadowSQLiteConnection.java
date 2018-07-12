package android.database.sqlite;

import android.database.CursorWindow;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;

/**
 * Created by kkmike999 on 2017/06/01.
 */
public class ShadowSQLiteConnection {
    public void execute(String s, Object o, CancellationSignal cancellationSignal) {

    }

    public long executeForLong(String sql, Object[] bindArgs, CancellationSignal cancellationSignal) {
        return 0;
    }

    public String executeForString(String sql, Object[] bindArgs, CancellationSignal cancellationSignal) {
        return null;
    }

    public ParcelFileDescriptor executeForBlobFileDescriptor(String sql, Object[] bindArgs, CancellationSignal cancellationSignal) {
        return null;
    }

    public int executeForChangedRowCount(String sql, Object[] bindArgs, CancellationSignal cancellationSignal) {
        return 0;
    }

    public long executeForLastInsertedRowId(String sql, Object[] bindArgs, CancellationSignal cancellationSignal) {
        return 0;
    }

    public int executeForCursorWindow(String sql, Object[] bindArgs, CursorWindow window, int startPos, int requiredPos, boolean countAllRows, CancellationSignal cancellationSignal) {
        return 0;
    }
}
