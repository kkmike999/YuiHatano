package net.yui.utils;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by kkmike999 on 2018/07/12.
 */
public class ReflectUtilsTest {

    @Test
    public void invokeStatic() {
        Assert.assertEquals("value", ReflectUtils.invokeStatic("net.yui.utils.MockHelper", "testStatic"));
        Assert.assertEquals("arg0", ReflectUtils.invokeStatic("net.yui.utils.MockHelper", "testStatic", "arg0"));

        MockHelper mockHelper = new MockHelper();

        Assert.assertEquals("value", ReflectUtils.invoke(mockHelper, "testMethod"));
        Assert.assertEquals("arg0", ReflectUtils.invoke(mockHelper, "testMethod", "arg0"));
    }
}