package net.kb.test.library;

import android.content.Context;
import android.content.ShadowContext;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.ShadowResources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.ShadowSQLiteDatabase;
import android.util.DisplayMetrics;

import org.junit.Rule;
import org.junit.rules.ExternalResource;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kkmike999 on 2017/05/25.
 */
public class KBSharedPrefCase {

    private Context       mContext;
    private ShadowContext mShadowContext;

    private List<String>         dbPaths = new ArrayList<>();
    private List<SQLiteDatabase> dbs     = new ArrayList<>();

    @Rule
    public ExternalResource contextRule = new ExternalResource() {
        @Override
        protected void before() throws Throwable {
            // android sdk Resource只有这个构造函数
//            public Resources(AssetManager assets, DisplayMetrics metrics, Configuration config)

            ShadowResources shadowResources = new ShadowResources();
            Resources       resources       = new CGLibProxy().getInstance(Resources.class, shadowResources, new Class[]{AssetManager.class, DisplayMetrics.class, Configuration.class}, new Object[]{null, null, null});
            mShadowContext = new ShadowContext(resources);

            mContext = new CGLibProxy().getInstance(Context.class, mShadowContext);

            mShadowContext.setMockContext(mContext);
        }

        @Override
        protected void after() {
            for (SQLiteDatabase db : dbs) {
                db.close();
            }
            for (String dbPath : dbPaths) {
                new File(dbPath).delete();
            }
        }
    };

    public Context getContext() {
        return mContext;
    }

    public ShadowContext getShadowContext() {
        return mShadowContext;
    }

    protected SQLiteDatabase newSQLiteDatabase(String name) {
        try {
            String path = "build/sample_" + name + ".db";

            dbPaths.add(path);

            ShadowSQLiteDatabase sdb = new ShadowSQLiteDatabase(path, 0, null);
            SQLiteDatabase       db  = new CGLibProxy().getInstance(SQLiteDatabase.class, sdb);

            dbs.add(db);

            return db;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
