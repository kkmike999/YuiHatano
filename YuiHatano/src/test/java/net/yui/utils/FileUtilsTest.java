package net.yui.utils;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Created by kkmike999 on 2017/12/06.
 * <p>
 * 文件工具 单元测试
 */
public class FileUtilsTest {

    @Test
    public void deleteDir() throws Exception {
        File dir = new File("build/files");
        dir.mkdirs();

        File file = new File(dir, "myfile");
        file.createNewFile();

        Assert.assertTrue(file.exists());

        // 不能删除
        Assert.assertFalse(dir.delete());
        // 文件还存在
        Assert.assertTrue(dir.exists());

        // 递归删除文件
        FileUtils.deleteDir(dir);

        // 文件已删除
        Assert.assertFalse(dir.exists());
    }
}