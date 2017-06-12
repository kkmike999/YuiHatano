package net.kb.test.dbflow;

import com.raizlabs.android.dbflow.annotation.Database;

@Database(name = DBFlowDatabase.NAME, version = DBFlowDatabase.VERSION)
public class DBFlowDatabase {
    //数据库名称
    public static final String NAME    = "DBFlowDatabase";
    //数据库版本号
    public static final int    VERSION = 1;
}