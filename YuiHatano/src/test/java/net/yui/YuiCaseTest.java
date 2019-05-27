package net.yui;

import android.app.Application;
import android.content.Context;

import net.kb.test.library.KBCase;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by kkmike999 on 2017/06/06.
 */
public class YuiCaseTest extends KBCase {
    private Context mContext;

    @Before
    public void setUp() throws Exception {
        mContext = getContext();
    }

    @Test
    public void testMethod() {
        Assert.assertEquals("Yui Hatano", mContext.getString(R.string.test_string));
        System.out.println();
    }

    @Test
    public void testApplication() {
        Application app = (Application) mContext.getApplicationContext();

        Assert.assertTrue(app instanceof Application);
    }
}