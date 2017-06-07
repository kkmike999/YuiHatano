package net.kb.test.bean;

/**
 * Created by kkmike999 on 2017/06/07.
 */
public class Bean {
    int    id;
    String name;

    public Bean() {
    }

    public Bean(String name) {
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
