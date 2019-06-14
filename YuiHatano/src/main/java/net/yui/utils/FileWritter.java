package net.yui.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by kkmike999 on 2019/06/14.
 */
public class FileWritter {

    public void writeString(String filePath, String content) {
        BufferedWriter output = null;
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                file.createNewFile();// 不存在则创建
            }
            output = new BufferedWriter(new FileWriter(file));
            output.write(content);
            output.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
