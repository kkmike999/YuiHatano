package net.yui;

import android.app.Application;
import android.app.ShadowApplication;
import android.content.Context;
import android.content.ShadowContext;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.ShadowResources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.DisplayMetrics;

import net.yui.utils.DbPathUtils;
import net.yui.utils.FileUtils;

import org.junit.Rule;
import org.junit.rules.ExternalResource;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by kkmike999 on 2017/9/10.
 * <p>
 * YuiHatano单元测试Case
 */
public class YuiCase {

    protected boolean DEBUG = false;

    private Context       mContext;
    private Application   mApplication;
    private ShadowContext mShadowContext;

    @Rule
    public ExternalResource contextRule = new ExternalResource() {

        @Override
        protected void before() throws Throwable {
            if (DEBUG) {
                System.out.println("KBCase before");
            }
            System.out.println();
            // android sdk Resource只有这个构造函数
            // public Resources(AssetManager assets, DisplayMetrics metrics, Configuration config)

            // 删除、重新创建 临时数据库目录
            deleteDbDir();
            createDbDir();

            ShadowResources shadowResources = new ShadowResources();
            Resources       resources       = new CGLibProxy().proxy(Resources.class, shadowResources, new Class[]{AssetManager.class, DisplayMetrics.class, Configuration.class}, new Object[]{null, null, null});
            mShadowContext = new ShadowContext(resources);

            mContext = new CGLibProxy().proxy(Context.class, mShadowContext);

            // application
            ShadowApplication shadowApplication = new ShadowApplication(resources);
            mApplication = new CGLibProxy().proxy(Application.class, shadowApplication);
        }

        @Override
        protected void after() {
            if (DEBUG) {
                System.out.println("KBCase after");
            }
            Map<String, SQLiteDatabase> dbMap = mShadowContext.getDbMap();

            for (SQLiteDatabase db : dbMap.values()) {
                // 关闭数据库
                db.close();

                String dbPath = db.getPath();

                // 删除临时数据库文件
                new File(dbPath).delete();
            }

            dbMap.clear();

            deleteDbDir();
            deleteDir();
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
            File dbDir = new File(DbPathUtils.getDbDir());
            FileUtils.deleteDir(dbDir);
        }

        /**
         * 临时生成的目录
         */
        private void deleteDir() {
            List<File> dirs = Arrays.asList(mShadowContext.getFilesDir(), mShadowContext.getCacheDir(), mShadowContext.getDataDir());

            for (File dir : dirs) {
                FileUtils.deleteDir(dir);
            }
        }
    };

    protected Context getContext() {
        return mContext;
    }

    protected ShadowContext getShadowContext() {
        return mShadowContext;
    }

    protected Application getApplication() {
        return mApplication;
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
