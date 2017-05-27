package net.kb.test.library.utils;

import java.io.BufferedReader;
import java.io.File;

/**
 * Created by kkmike999 on 2017/05/26.
 */
public class FileReader {

    public static String read(File file) {
        if (!file.exists()) {
            return null;
        }
        try {
            BufferedReader br    = new BufferedReader(new java.io.FileReader(file));//构造一个BufferedReader类来读取文件
            StringBuilder  sb    = new StringBuilder();
            char[]         chars = new char[1024 * 512];
            int            hasRead;

            while ((hasRead = br.read(chars)) >= 0) {
                sb.append(chars, 0, hasRead);
            }
            IOUtil.closeIO(br);

            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String readString(String path) {
        return read(new File(path));
    }
}
