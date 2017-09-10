package net.yui;

import net.yui.CGLibProxy;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by kkmike999 on 2017/06/09.
 */
public class CGLibProxyTest {
    @Test
    public void getInstance() throws Exception {
        A proxyA = new CGLibProxy().proxy(A.class, new B());

        Assert.assertTrue(proxyA instanceof A);
    }

    private static class A {

        public A() {}

        public void display() {

        }
    }

    private static class B {
        public B() {}

        public void display() {

        }
    }

}