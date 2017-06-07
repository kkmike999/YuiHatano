package net.kb.test.afinal;

import android.content.Context;
import android.content.ShadowContext;

import net.kb.test.bean.Bean;
import net.kb.test.library.KBSharedPrefCase;
import net.tsz.afinal.FinalDb;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * Created by kkmike999 on 2017/06/07.
 */
public class AfinalTest extends KBSharedPrefCase {

    FinalDb finalDb;

    @Before
    public void setUp() throws Exception {
        Context       context       = getContext();
        ShadowContext shadowContext = getShadowContext();
        shadowContext.putSQLiteDatabase("afinal.db", newSQLiteDatabase("afinal.db"));

        finalDb = FinalDb.create(context);
    }

    @Test
    public void testInsert() {
        Bean bean = new Bean("kkmike999");

        finalDb.save(bean);

        List<Bean> beanRS = finalDb.findAll(Bean.class);
        Bean       b      = beanRS.get(0);

        Assert.assertEquals("kkmike999", b.getName());
    }
}
