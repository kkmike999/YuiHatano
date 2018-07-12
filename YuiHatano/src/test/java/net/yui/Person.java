package net.yui;

/**
 * Created by kkmike999 on 2017/06/01.
 */
public class Person {
    int    id;
    String name;

    public Person(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
