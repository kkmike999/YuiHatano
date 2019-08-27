## YuiHatano介绍

**YuiHatano**是一款轻量级DAO单元测试框架，开发者可以通过此框架，在Android Studio运行SQLiteDatabase、SharedPreference单元测试。

YuiHatano支持原生SQLiteDatabase操作及GreenDAO、Afinal、XUtils、DbFlow第三方库。

## 引用

在module的**build.gradle**添加依赖：
```groovy
repositories {
    maven { url "https://dl.bintray.com/kkmike999/maven" }
}

dependencies {
    testImplementation('net.yui:YuiHatano:1.1.7') {
        exclude group: 'com.android.support'
    }
}
```

## 配置

在Android Studio操作栏，`Run`->`EditConfigurations`，双击`Defaults`，选择`Android JUnit`窗口，找到`Working directory`参数栏，点击最右边的`...`选择`MODULE_DIR`。

![Run->EditConfigurations](https://github.com/kkmike999/YuiHatano/blob/master/readme/images/Run-EditConfigurations.png?raw=true)
![Android Junit](https://github.com/kkmike999/YuiHatano/blob/master/readme/images/Android%20Junit.png?raw=true)
![选择MODULE_DIR](https://github.com/kkmike999/YuiHatano/blob/master/readme/images/select%20MODULE_DIR.png?raw=true)

操作示范：
![](https://github.com/kkmike999/YuiHatano/blob/master/readme/images/operate.gif?raw=true)

## 使用方法

### SQLiteDatabase

```java
public class SQLiteDatabaseTest extends YuiCase {

    SQLiteDatabase db;

    @Before
    public void setUp() throws Exception {
        // 使用YuiHatano提供的Context，获取SQLiteDatabase实例
        db = getContext().openOrCreateDatabase("build/test.db", 0, null);
    }

    @Test
    public void testCreateTable() {
        String sql = "CREATE TABLE person (id INTEGER, name VARCHAR)";

        db.execSQL(sql);
    }
}
```

### GreenDAO

[GreenDAO github](https://github.com/greenrobot/greenDAO)

User：
```java
@Entity
public class User {

    // 不能用int
    @Id(autoincrement = true)
    private Long id;

    @Unique
    private int uid;

    private String name;

    public User(int uid, String name) {
        this.uid = uid;
        this.name = name;
    }
}
```

单元测试：
```java
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
        User user = new User(uid, name);

        mUserDAO.insert(user);

        List<User> users = mUserDAO.loadAll();

        Assert.assertEquals(1, users.size());

        Assert.assertEquals(1, users.get(0).getUid());
        Assert.assertEquals("kk1", users.get(0).getName());
    }
}
```

### AFinal

[AFinal Github](https://github.com/yangfuhai/afinal)

单元测试：
```java
public class AfinalTest extends AFinalCase {

    FinalDb finalDb;

    @Before
    public void setUp() throws Exception {
        Context context = getContext();

        finalDb = FinalDb.create(context, false);
    }

    @Test
    public void testSave() {
        Bean bean = new Bean(uid, name);

        finalDb.save(bean);

        List<Bean> beanRS = finalDb.findAll(Bean.class);
        Bean       b      = beanRS.get(0);

        Assert.assertEquals("kkmike999", b.getName());
    }
}
```

### XUtils

[XUtils3 Github](https://github.com/wyouflf/xUtils3)

```java
@Table(name = "Parent")
public class Parent {

    @Column(name = "ID", isId = true, autoGen = true)
    int id;

    @Column(name = "name")
    String name;
}
```

单元测试：
```java
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

        Parent parent = db.selector(Parent.class)
                          .where("name", "LIKE", "name0")
                          .findFirst();

        Assert.assertEquals("name0", parent.getName());
    }
}
```

### DbFlow

DbFlow gradle配置，自行查阅：[DbFlow中文教程]( https://yumenokanata.gitbooks.io/dbflow-tutorials/content/index.html)。

```java
@Table(database = DBFlowDatabase.class)
public class UserModel extends BaseModel {
    //自增ID
    @Column
    @PrimaryKey(autoincrement = true)
    public Long   id;
    @Column
    public String name;
    @Column
    public int    sex;

    public String getName() {
        return name;
    }

    public int getSex() {
        return sex;
    }
}
```

单元测试：
```java
public class DbFlowTest extends DbFlowCase {

    @Before
    public void setUp() throws Exception {
        Assert.assertEquals(0, new Select(Method.count()).from(UserModel.class).count());
    }

    @Test
    public void onInsert() {
        UserModel people = new UserModel();

        people.name = "张三";
        people.sex = 1;
        people.save();// 添加对象，一条一条保存

        Assert.assertEquals(1, new Select(Method.count()).from(UserModel.class).count());
    }
}
```

### Native方法测试

（目前仅支持MacOS，并且测试的module有cpp源文件，MacOS上还需要安装gcc）

示例目录结构：
```shell
./app/
└── src
    ├── main
    │   ├── cpp
    │   │   ├── CMakeLists.txt
    │   │   └── jni.cpp
    │   ├── java
    │   │   └── net
    │   │       └── yui
    │   │           └── app
    │   │               ├── JNI.java
    └── test
        └── java
            └── net
                └── yui
                    └── app
                        ├── jni
                        │   └── TestJNI.java
```

含有native方法的`JNI`:
```java
public class JNI {
    public native int add(int a, int b);
}
```

测试用例继承`JNICase`，其他代码照常：
```java
public class TestJNI extends JNICase {

    static {
        System.loadLibrary("jni");
    }

    @Test
    public void testJNI() {
        JNI jni = new JNI();
        Assert.assertEquals(2, jni.add(1, 1));
    }
}
```




