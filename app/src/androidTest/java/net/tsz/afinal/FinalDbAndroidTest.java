package net.tsz.afinal;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import net.tsz.afinal.bean.BooleanBean;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by kkmike999 on 2019/08/27.
 */
@RunWith(AndroidJUnit4.class)
public class FinalDbAndroidTest {
    FinalDb db;

    @Before
    public void setUp() throws Exception {
        db = FinalDb.create(InstrumentationRegistry.getTargetContext());
        db.dropDb();
        Assert.assertEquals(0, db.findAll(BooleanBean.class).size());
    }

    @Test
    public void test() {
        BooleanBean b = new BooleanBean(true);
        db.save(b);

        BooleanBean result = db.findAll(BooleanBean.class).get(0);

        System.out.println(result);
    }
}
