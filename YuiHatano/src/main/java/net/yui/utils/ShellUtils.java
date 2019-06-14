package net.yui.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by kkmike999 on 2019/06/04.
 */
public class ShellUtils {

    public static ShellLog exec(String commend) throws IOException, InterruptedException {
        Process proc = Runtime.getRuntime().exec(commend);

        InputStream in = proc.getInputStream();

        BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        String         line;
        StringBuilder  sb = new StringBuilder();

        ShellLog log = new ShellLog();

        while ((line = br.readLine()) != null) {
            System.out.println(line);

            sb.append(line);
        }

        log.log = sb.toString();

        // 读取标准错误流
        if (proc.getErrorStream() != null) {
            sb = new StringBuilder();

            BufferedReader brError = new BufferedReader(new InputStreamReader(proc.getErrorStream(), "UTF-8"));
            String         errline;
            while ((errline = brError.readLine()) != null) {
                System.err.println(errline);

                sb.append(errline);
            }
        }

        log.err = sb.toString();

        int c = proc.waitFor();

        return log;
    }

    public static class ShellLog {
        String log = "";
        String err = "";

        public String getLog() {
            return log;
        }

        public String getErr() {
            return err;
        }
    }
}
