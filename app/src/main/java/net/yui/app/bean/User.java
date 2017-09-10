package net.yui.app.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by kkmike999 on 2017/06/08.
 */
@Entity
public class User {

    // 不能用int
    @Id(autoincrement = true)
    private Long id;

    // 约束唯一标识
    @Unique
    private int uid;

    private String name;

    public User(int uid, String name) {
        this.uid = uid;
        this.name = name;
    }

    @Generated(hash = 1586227787)
    public User(Long id, int uid, String name) {
        this.id = id;
        this.uid = uid;
        this.name = name;
    }

    @Generated(hash = 586692638)
    public User() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getUid() {
        return this.uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
