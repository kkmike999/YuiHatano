package android.content.res;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kkmike999 on 2017/05/27.
 */
public class ResourceCacheTest {

    ResourceCache mResourceCache;

    @Before
    public void setUp() throws Exception {
        mResourceCache = new ResourceCache();
    }

    @After
    public void tearDown() throws Exception {
        mResourceCache.clearCache();
    }

    @Test
    public void testGetIdTable() throws Exception {
        String rClassName = "net.kb.test.library.R$string";

        mResourceCache.cacheIdTable(rClassName, new HashMap<Integer, String>());

        Map<Integer, String> map0 = mResourceCache.getIdTableCache(rClassName);
        Map<Integer, String> map1 = mResourceCache.getIdTableCache(rClassName);

        Assert.assertEquals(map0, map1);

    }

}