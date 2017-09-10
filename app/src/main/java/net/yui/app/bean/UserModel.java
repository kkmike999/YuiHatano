package net.yui.app.bean;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import net.yui.app.dbflow.DBFlowDatabase;

//@ModelContainer //表示可以直接解析JSON
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