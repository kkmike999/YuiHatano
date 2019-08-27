package android.database;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Created by kkmike999 on 2019/08/27.
 */
public class ShadowCursorTest {

    ShadowCursor cursor;

    @Test
    public void getObject() {
        List<Object> data0 = Arrays.asList((Object) 1, "kk");

        cursor = new ShadowCursor(Arrays.asList("id", "name"), Arrays.asList(data0));
        cursor.moveToFirst();
        Assert.assertEquals(1, cursor.getObject(0));
        Assert.assertEquals("kk", cursor.getObject(1));
    }

    @Test
    public void getNullObject() {
        List<Object> data1 = Arrays.asList((Object) 2, null);

        cursor = new ShadowCursor(Arrays.asList("id", "name"), Arrays.asList(data1));
        cursor.moveToFirst();
        Assert.assertEquals(null, cursor.getObject(1));
        Assert.assertEquals("default", cursor.getObject(1, "default"));
    }

    @Test
    public void getString() {
        List<Object> data0 = Arrays.asList((Object) 1, "kk");// first
        List<Object> data1 = Arrays.asList((Object) 2, null);// next

        cursor = new ShadowCursor(Arrays.asList("id", "name"), Arrays.asList(data0, data1));
        cursor.moveToFirst();

        Assert.assertEquals("1", cursor.getString(0));
        Assert.assertEquals("kk", cursor.getString(1));

        // 测试值为null
        cursor.moveToNext();
        Assert.assertEquals("2", cursor.getString(0));
        Assert.assertEquals(null, cursor.getString(1));
    }

    /**
     * 测试 int、short、double、float
     */
    @Test
    public void getNumber() {
        List<Object> data0 = Arrays.asList((Object) 1, "kk");// first
        List<Object> data1 = Arrays.asList(null, null);// next

        cursor = new ShadowCursor(Arrays.asList("id", "name"), Arrays.asList(data0, data1));
        cursor.moveToFirst();

        Assert.assertEquals(1, cursor.getInt(0));
        Assert.assertEquals(1, cursor.getShort(0));
        Assert.assertEquals(1d, cursor.getDouble(0), 0);
        Assert.assertEquals(1f, cursor.getFloat(0), 0);

        // 测试值为null
        cursor.moveToNext();
        Assert.assertEquals(0, cursor.getInt(0));
        Assert.assertEquals(0, cursor.getShort(0));
        Assert.assertEquals(0d, cursor.getDouble(0), 0);
        Assert.assertEquals(0f, cursor.getFloat(0), 0);
    }
}
