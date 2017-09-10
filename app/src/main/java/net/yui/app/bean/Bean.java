package net.yui.app.bean;

/**
 * Created by kkmike999 on 2017/06/07.
 */
public class Bean {
    int    id;
    int    uid;
    String name;

    public Bean() {
    }

    public Bean(int uid, String name) {
        this.uid = uid;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
