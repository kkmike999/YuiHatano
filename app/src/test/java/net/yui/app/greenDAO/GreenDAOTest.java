package net.yui.app.greenDAO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import net.yui.app.bean.DaoMaster;
import net.yui.app.bean.DaoSession;
import net.yui.app.bean.User;
import net.yui.app.bean.UserDao;
import net.yui.testCase.GreenDAOCase;
import net.yui.utils.DebugHook;

import org.greenrobot.greendao.query.DeleteQuery;
import org.greenrobot.greendao.query.Query;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

/**
 * Created by kkmike999 on 2017/06/08.
 */
public class GreenDAOTest extends GreenDAOCase {

    private DaoSession mDaoSession;
    private UserDao    mUserDAO;

    @BeforeClass
    public static void beforeClass() {
        DebugHook.setDebug(true);
    }

    @Before
    public void setUp() throws Exception {
        Context context = getContext();

        // 创建数据库 build/test.db，数据库名就是路径
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "test.db", null);
        // 获取可写数据库
        SQLiteDatabase db = helper.getWritableDatabase();

        // 获取数据库对象
        DaoMaster daoMaster = new DaoMaster(db);
        // 获取Dao对象管理者
        mDaoSession = daoMaster.newSession();

        mUserDAO = mDaoSession.getUserDao();
    }

    @Test
    public void testInsert() {
        insert(1, "kk1");

        List<User> users = mUserDAO.loadAll();

        Assert.assertEquals(1, users.size());

        Assert.assertEquals(1, users.get(0).getUid());
        Assert.assertEquals("kk1", users.get(0).getName());
    }

    @Test
    public void testCount() {
        insert(1, "kk1");

        Assert.assertEquals(1, mUserDAO.count());
    }

    @Test
    public void testQuery() {
        insert(1, "kk1");

        Query<User> query = mUserDAO.queryBuilder()
                                    .where(UserDao.Properties.Uid.eq(1))
                                    .build();

        User user1 = query.list().get(0);

        Assert.assertEquals(1, user1.getUid());
        Assert.assertEquals("kk1", user1.getName());
    }

    @Test
    public void testDelete() {
        insert(1, "kk1");

        // 删除全部
        mUserDAO.deleteAll();

        Assert.assertEquals(0, mUserDAO.loadAll().size());

        insert(2, "kk2");

        // 条件删除
        DeleteQuery<User> cm = mUserDAO.queryBuilder().where(UserDao.Properties.Name.eq("kk2")).buildDelete();
        cm.executeDeleteWithoutDetachingEntities();

        Assert.assertEquals(0, mUserDAO.loadAll().size());
    }

    @Test
    public void testInsertOrReplace() {

        for (int i = 0; i < 10; i++) {
            User user = new User(i, "kk" + i);

            mUserDAO.insertOrReplace(user);
        }

        Assert.assertEquals(10, mUserDAO.loadAll().size());

        // update uid=9
        User user9Update = new User(9, "kk9 update");

        mUserDAO.insertOrReplace(user9Update);

        // 数量不变
        Assert.assertEquals(10, mUserDAO.loadAll().size());

        // 查询uid=9的记录
        User user9 = mUserDAO.queryBuilder()
                             .where(UserDao.Properties.Uid.eq(9))
                             .build()
                             .unique();

        Assert.assertEquals("kk9 update", user9.getName());
    }

    private void insert(int uid, String name) {
        User user = new User(uid, name);

        mUserDAO.insert(user);
    }
}
