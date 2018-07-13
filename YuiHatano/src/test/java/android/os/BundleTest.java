package android.os;

import net.yui.BundleProxy;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

/**
 * Created by kkmike999 on 2018/07/13.
 */
public class BundleTest {

    Bundle mBundle;

    @Before
    public void setUp() throws Exception {
        // 创建代理对象
        mBundle = new BundleProxy().proxy(new ShadowBundle());

        System.out.println();
    }

    @Test
    public void testGet() {
        mBundle.putInt("k0", 1);
        mBundle.putString("k1", "v1");
        mBundle.putByte("k2", Byte.valueOf("1").byteValue());
        mBundle.putFloat("k3", 1.1f);
        mBundle.putChar("k4", '4');
        mBundle.putCharArray("k5", new char[]{'5'});
        mBundle.putIntArray("k6", new int[]{6});
        mBundle.putBoolean("k7", true);

        Assert.assertEquals(1, mBundle.getInt("k0"));
        Assert.assertEquals("v1", mBundle.getString("k1"));
        Assert.assertEquals(Byte.valueOf("1").byteValue(), mBundle.getByte("k2"));
        Assert.assertEquals(1.1f, mBundle.getFloat("k3"), 0);
        Assert.assertEquals('4', mBundle.getChar("k4"));
        Assert.assertTrue(Arrays.equals(new char[]{'5'}, mBundle.getCharArray("k5")));
        Assert.assertTrue(Arrays.equals(new int[]{6}, mBundle.getIntArray("k6")));
        Assert.assertEquals(true, mBundle.getBoolean("k7"));
    }
}