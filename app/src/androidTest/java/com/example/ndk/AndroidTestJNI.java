package com.example.ndk;

import net.yui.app.JNI;

import org.junit.Assert;
import org.junit.Test;


/**
 * Created by kkmike999 on 2019/06/12.
 */
public class AndroidTestJNI {

    @Test
    public void add() {
        System.loadLibrary("jni");

        JNI jni = new JNI();
        Assert.assertEquals(2, jni.add(1, 1));

        System.out.println("Hello World");
    }
}