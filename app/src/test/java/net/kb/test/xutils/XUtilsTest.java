package net.kb.test.xutils;

import net.kb.test.bean.Parent;
import net.kb.test.library.testCase.XUtilsCase;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xutils.DbManager;
import org.xutils.common.util.KeyValue;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.db.table.TableEntity;
import org.xutils.ex.DbException;
import org.xutils.x;

/**
 * Created by kkmike999 on 2017/6/11.
 */
public class XUtilsTest extends XUtilsCase {

    protected DbManager db;

    @Before
    public void setUp() throws Exception {
        x.Ext.setDebug(true); // 是否输出debug日志, 开启debug会影响性能.

        // 本地数据的初始化
        DbManager.DaoConfig daoConfig = new DbManager.DaoConfig().setDbName("xutils3_db") //设置数据库名
                                                                 // 设置数据库版本,每次启动应用时将会检查该版本号,
                                                                 // 发现数据库版本低于这里设置的值将进行数据库升级并触发DbUpgradeListener
                                                                 .setDbVersion(1) //
                                                                 // .setDbDir(new File("build/db"))//设置数据库.db文件存放的目录,默认为包名下databases目录下
                                                                 .setAllowTransaction(true)//设置是否开启事务,默认为false关闭事务
                                                                 .setTableCreateListener(new DbManager.TableCreateListener() {
                                                                     @Override
                                                                     public void onTableCreated(DbManager db, TableEntity<?> table) {

                                                                     }
                                                                 })//设置数据库创建时的Listener
                                                                 .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                                                                     @Override
                                                                     public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                                                                         //balabala...
                                                                     }
                                                                 });//设置数据库升级时的Listener,这里可以执行相关数据库表的相关修改,比如alter语句增加字段等
        db = x.getDb(daoConfig);
    }

    @Test
    public void testSave() throws DbException {
        Parent parent = new Parent("name0");

        db.save(parent);
    }

    @Test
    public void testDelete() throws DbException {
        save("name0");

        db.delete(Parent.class);

        Assert.assertEquals(0, db.findAll(Parent.class).size());
    }

    @Test
    public void testUpdate() throws DbException {
        save("name0");

        KeyValue kv = new KeyValue("name", "name_update");

        db.update(Parent.class, WhereBuilder.b("name", "LIKE", "name0"), kv);

        Parent parent = db.findFirst(Parent.class);

        Assert.assertEquals("name_update", parent.getName());
    }

    @Test
    public void testSelect() throws DbException {
        save("name0");

        Parent parent = db.selector(Parent.class)//
                          .where("name", "LIKE", "name0")//
                          .findFirst();

        Assert.assertEquals("name0", parent.getName());
    }

    private void save(String name) throws DbException {
        Parent parent = new Parent(name);

        db.save(parent);
    }
}
