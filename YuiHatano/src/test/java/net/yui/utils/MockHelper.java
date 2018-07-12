package net.yui.utils;

/**
 * Created by kkmike999 on 2018/07/12.
 */
public class MockHelper {

    public static String testStatic() {
        return "value";
    }

    public static String testStatic(String arg0) {
        return arg0;
    }

    public String testMethod() {
        return "value";
    }

    public String testMethod(String arg0) {
        return arg0;
    }
}
