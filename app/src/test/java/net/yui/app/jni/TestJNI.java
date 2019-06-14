package net.yui.app.jni;

import net.yui.app.JNI;
import net.yui.testCase.JNICase;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by kkmike999 on 2019/06/14.
 */
public class TestJNI extends JNICase {

    static {
        System.loadLibrary("jni");
    }

    @Test
    public void testJNI() {
        JNI jni = new JNI();
        Assert.assertEquals(2, jni.add(1, 1));
    }
}
