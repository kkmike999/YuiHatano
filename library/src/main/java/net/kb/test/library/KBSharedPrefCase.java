package net.kb.test.library;

import android.content.Context;
import android.content.ShadowContext;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.ShadowResources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.DisplayMetrics;

import net.kb.test.library.utils.DbPathUtils;

import org.junit.Rule;
import org.junit.rules.ExternalResource;

import java.io.File;
import java.util.Map;

/**
 * Created by kkmike999 on 2017/05/25.
 */
public class KBSharedPrefCase {

    private Context       mContext;
    private ShadowContext mShadowContext;

    @Rule
    public ExternalResource contextRule = new ExternalResource() {

        @Override
        protected void before() throws Throwable {
            // android sdk Resource只有这个构造函数
//            public Resources(AssetManager assets, DisplayMetrics metrics, Configuration config)

            // 删除、重新创建 临时数据库目录
            deleteDbDir();
            createDbDir();

            ShadowResources shadowResources = new ShadowResources();
            Resources       resources       = new CGLibProxy().getInstance(Resources.class, shadowResources, new Class[]{AssetManager.class, DisplayMetrics.class, Configuration.class}, new Object[]{null, null, null});
            mShadowContext = new ShadowContext(resources);

            mContext = new CGLibProxy().getInstance(Context.class, mShadowContext);

            mShadowContext.setMockContext(mContext);
        }

        @Override
        protected void after() {
            Map<String, SQLiteDatabase> dbMap = mShadowContext.getDbMap();

            for (String dbName : dbMap.keySet()) {
                // 关闭数据库
                dbMap.get(dbName).close();

                String dbPath = DbPathUtils.getDbPath(dbName);

                // 删除临时数据库文件
                new File(dbPath).delete();
            }

            deleteDbDir();
        }

        /**
         * 创建数据库临时目录
         */
        private void createDbDir() {
            File dbDir = new File(DbPathUtils.getDbDir());

            if (!dbDir.exists()) {
                dbDir.mkdirs();
            }
        }

        /**
         * 删除数据库
         */
        private void deleteDbDir() {
            File   dbDir = new File(DbPathUtils.getDbDir());
            File[] files = dbDir.listFiles();

            if (files != null) {
                for (File file : files) {
                    file.delete();
                }
                dbDir.delete();
            }
        }
    };

    public Context getContext() {
        return mContext;
    }

    public ShadowContext getShadowContext() {
        return mShadowContext;
    }

    protected SQLiteDatabase newSQLiteDatabase(String dbName) {
        SQLiteOpenHelper openHelper = new SQLiteOpenHelper(getContext(), dbName, null, 1) {
            @Override
            public void onCreate(SQLiteDatabase db) {}

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
        };

        return openHelper.getWritableDatabase();
    }

    protected void putSQLDatabase(String dbName, SQLiteDatabase db) {
        mShadowContext.putSQLiteDatabase(dbName, db);
    }
}
