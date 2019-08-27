package net.tsz.afinal.bean;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;

/**
 * Created by kkmike999 on 2019/08/27.
 */
@Table(name = "BooleanBean")
public class BooleanBean {
    @Id
    int id;
    boolean isOk;

    public BooleanBean() {}

    public BooleanBean(boolean isOk) {
        this.isOk = isOk;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isOk() {
        return isOk;
    }

    public void setOk(boolean ok) {
        isOk = ok;
    }
}
