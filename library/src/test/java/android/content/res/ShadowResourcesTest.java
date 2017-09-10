package android.content.res;

import net.yui.R;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by kkmike999 on 2017/05/26.
 */
public class ShadowResourcesTest {

    ShadowResources resources;

    @Before
    public void setUp() throws Exception {
        resources = new ShadowResources();
    }

    @Test
    public void getString() throws Exception {
        Assert.assertEquals("KBUnitTest", resources.getString(R.string.test_string));
    }

    @Test
    public void getStringArray() throws Exception {
        String[] array = resources.getStringArray(R.array.arrayName);

        Assert.assertEquals("item0", array[0]);
        Assert.assertEquals("item1", array[1]);
    }

    @Test
    public void getIntArray() {
        int[] intArray = resources.getIntArray(R.array.intArray);

        Assert.assertEquals(0, intArray[0]);
        Assert.assertEquals(1, intArray[1]);

        int[] intArrayNoItem = resources.getIntArray(R.array.intArrayNoItem);

        Assert.assertEquals(0, intArrayNoItem.length);
    }

    @Test
    public void getPackageName() throws Exception {
        Assert.assertEquals("net.yui", resources.getPackageName());
    }

}