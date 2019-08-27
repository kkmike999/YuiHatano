package net.yui.app.afinal;

import net.tsz.afinal.FinalDb;
import net.yui.YuiCase;
import net.yui.app.afinal.bean.BooleanBean;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by kkmike999 on 2019/08/27.
 */
public class FinalDbAndroidTest extends YuiCase {
    FinalDb db;

    @Before
    public void setUp() {
        db = FinalDb.create(getContext());
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
