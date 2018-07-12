package android.content;

import android.content.res.Resources;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;

/**
 * Created by kkmike999 on 2017/12/06.
 */
public class ShadowContextTest {

    ShadowContext mShadowContext;

    @Before
    public void setUp() throws Exception {
        mShadowContext = new ShadowContext(mock(Resources.class));
    }

    @Test
    public void getFilesDir() throws Exception {
        List<File> dirs = Arrays.asList(mShadowContext.getFilesDir(), mShadowContext.getCacheDir(), mShadowContext.getDataDir());

        for (File dir : dirs) {
            Assert.assertTrue(dir.isDirectory());
            Assert.assertTrue(dir.exists());
        }
    }
}