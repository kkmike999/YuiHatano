package net.yui.app.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by kkmike999 on 2017/6/11.
 */
@Table(name = "Parent")
public class Parent {

    @Column(name = "ID", isId = true, autoGen = true)
    int id;

    @Column(name = "name")
    String name;

    public Parent() {
    }

    public Parent(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
