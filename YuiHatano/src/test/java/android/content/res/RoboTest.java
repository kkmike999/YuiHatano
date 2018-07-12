package android.content.res;

import net.yui.BuildConfig;
import net.yui.R;
import net.yui.utils.RoboRunner;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.annotation.internal.DoNotInstrument;

/**
 * Created by kkmike999 on 2017/05/26.
 * <p>
 * manifest = "build/intermediates/manifests/aapt/debug/AndroidManifest.xml"
 */
@RunWith(RoboRunner.class)
@Config(constants = BuildConfig.class)
@DoNotInstrument
public class RoboTest {

    Resources resources;

    @Before
    public void setUp() throws Exception {
        resources = RuntimeEnvironment.application.getResources();
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
}
