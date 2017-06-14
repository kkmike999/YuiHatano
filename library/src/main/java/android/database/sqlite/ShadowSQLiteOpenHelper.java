package android.database.sqlite;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.os.Build;
import android.util.Log;

import net.kb.test.library.utils.ReflectUtils;

/**
 * Created by kkmike999 on 2017/6/11.
 */
public class ShadowSQLiteOpenHelper {

    private static final String  TAG                   = SQLiteOpenHelper.class.getSimpleName();
    private static final boolean DEBUG_STRICT_READONLY = false;

    public static boolean DEBUG = false;

    SQLiteOpenHelper mRealOpenHelper;

    private final Context                      mContext;
    private final String                       mName;
    private final SQLiteDatabase.CursorFactory mFactory;
    private final int                          mNewVersion;

    private       SQLiteDatabase       mDatabase;
    private       boolean              mIsInitializing;
    private       boolean              mEnableWriteAheadLogging;
    private final DatabaseErrorHandler mErrorHandler;

    public ShadowSQLiteOpenHelper(SQLiteOpenHelper realOpenHelper, Context context, String name, SQLiteDatabase.CursorFactory factory, int version,
                                  DatabaseErrorHandler errorHandler) {
        if (version < 1) {
            throw new IllegalArgumentException("Version must be >= 1, was " + version);
        }

        mRealOpenHelper = realOpenHelper;
        mContext = context;
        mName = name;
        mFactory = factory;
        mNewVersion = version;
        mErrorHandler = errorHandler;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    protected SQLiteDatabase getDatabaseLocked(boolean writable) {
        if (mDatabase != null) {
            if (!mDatabase.isOpen()) {
                // Darn!  The user closed the database by calling mDatabase.close().
                mDatabase = null;
            } else if (!writable || !mDatabase.isReadOnly()) {
                // The database is already open for business.
                return mDatabase;
            }
        }

        if (mIsInitializing) {
            throw new IllegalStateException("getDatabase called recursively");
        }

        SQLiteDatabase db = mDatabase;
        try {
            mIsInitializing = true;

            if (db != null) {
                if (writable && db.isReadOnly()) {
                    //                    db.reopenReadWrite();

                    ReflectUtils.invoke(db, "reopenReadWrite");
                }
            } else if (mName == null) {
                debug("mName == null ; db = SQLiteDatabase.create(null)");

                db = SQLiteDatabase.create(null);
            } else {
                try {
                    if (DEBUG_STRICT_READONLY && !writable) {
                        String path = mContext.getDatabasePath(mName).getPath();
                        db = SQLiteDatabase.openDatabase(path, mFactory, SQLiteDatabase.OPEN_READONLY, mErrorHandler);
                    } else {
                        debug("openOrCreateDatabase ; mContext=" + mContext + "; mName=" + mName);
                        db = mContext.openOrCreateDatabase(mName, mEnableWriteAheadLogging ? Context.MODE_ENABLE_WRITE_AHEAD_LOGGING : 0, mFactory, mErrorHandler);
                    }
                } catch (SQLiteException ex) {
                    if (writable) {
                        throw ex;
                    }
                    Log.e(TAG, "Couldn't open " + mName + " for writing (will try read-only):", ex);

                    String path = mContext.getDatabasePath(mName).getPath();

                    db = SQLiteDatabase.openDatabase(path, mFactory, SQLiteDatabase.OPEN_READONLY, mErrorHandler);
                }
            }

            mRealOpenHelper.onConfigure(db);

            debug("db=" + db);

            final int version = db.getVersion();
            if (version != mNewVersion) {
                if (db.isReadOnly()) {
                    throw new SQLiteException("Can't upgrade read-only database from version " + db.getVersion() + " to " + mNewVersion + ": " + mName);
                }

                db.beginTransaction();
                try {
                    if (version == 0) {
                        mRealOpenHelper.onCreate(db);
                    } else {
                        if (version > mNewVersion) {
                            mRealOpenHelper.onDowngrade(db, version, mNewVersion);
                        } else {
                            mRealOpenHelper.onUpgrade(db, version, mNewVersion);
                        }
                    }
                    db.setVersion(mNewVersion);
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
            }

            mRealOpenHelper.onOpen(db);

            if (db.isReadOnly()) {
                Log.w(TAG, "Opened " + mName + " in read-only mode");
            }

            mDatabase = db;
            return db;
        } finally {
            mIsInitializing = false;
            if (db != null && db != mDatabase) {
                db.close();
            }
        }
    }

    private void debug(String msg) {
        if (DEBUG) {
            System.out.println(msg);
        }
    }
}
