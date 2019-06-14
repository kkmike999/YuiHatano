package net.yui.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.List;

public class SignatureUtil {

    private static final char HEX_DIGITS[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public static String toHexString(byte[] b) {
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
            sb.append(HEX_DIGITS[b[i] & 0x0f]);
        }
        return sb.toString();
    }

    /**
     * 对字符串md5加密
     *
     * @param string
     *
     * @return
     */
    public static String md5(String string) {
        if (TextUtils.isEmpty(string))
            return "";
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(string.getBytes());
            byte messageDigest[] = digest.digest();

            return toHexString(messageDigest);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return "";
    }

    /**
     * 获取文件md5
     *
     * @param file
     *
     * @return
     */
    public static String fileMD5(File file) {
        if (file != null && !file.exists()) {
            return null;
        }

        String hex = null;

        byte[]      buffer = new byte[1024];
        InputStream fis    = null;
        try {
            int numRead = 0;
            fis = new FileInputStream(file);

            MessageDigest md5 = MessageDigest.getInstance("MD5");
            while ((numRead = fis.read(buffer)) > 0) {
                md5.update(buffer, 0, numRead);
            }

            hex = toHexString(md5.digest());

            IOUtil.closeIO(fis);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtil.closeIO(fis);
            return hex;
        }

    }

    /**
     * 获取文件md5
     *
     * @param path 文件路径
     *
     * @return
     */
    public static String fileMD5(String path) {
        File file = new File(path);
        return fileMD5(file);
    }

    /**
     * 比较 文件的md5 与 参数md5Compare 是否一致
     *
     * @param file         文件
     * @param md5ToCompare 校验的md5
     *
     * @return if true，文件与校验md5一致；否则false
     */
    public static boolean checkFileMD5(File file, String md5ToCompare) {
        if (file != null && file.exists()) {
            String md5 = fileMD5(file);

            if (md5 != null && md5.toLowerCase().equals(md5ToCompare.toLowerCase())) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 比较 文件的md5 与 参数md5Compare 是否一致
     *
     * @param path         文件路径
     * @param md5ToCompare 校验的md5
     *
     * @return if true，md5一致；否则false
     */
    public static boolean checkFileMD5(String path, String md5ToCompare) {
        return checkFileMD5(new File(path), md5ToCompare);
    }

    /**
     * 对字符串SHA加密
     *
     * @param decript
     *
     * @return
     */
    public static String SHA(String decript) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA");
            digest.update(decript.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 对字符串SHA-1加密
     *
     * @param decript
     *
     * @return
     */
    public static String SHA1(String decript) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(decript.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getAppSignMD5(Context context) {
        Signature signature = getPackageSignature(context, context.getPackageName());
        if (signature != null) {
            return getMessageDigest(signature.toByteArray());
        }
        return null;
    }

    public static String getSignMD5(Context context, String packageName) {
        Signature signature = getPackageSignature(context, packageName);
        if (signature != null) {
            return getMessageDigest(signature.toByteArray());
        }
        return null;
    }

    public static Signature getPackageSignature(Context context, String packageName) {
        try {
            PackageManager        pm   = context.getPackageManager();
            List<PackageInfo>     apps = pm.getInstalledPackages(PackageManager.GET_SIGNATURES);
            Iterator<PackageInfo> it   = apps.iterator();
            while (it.hasNext()) {
                PackageInfo info = it.next();
                if (info.packageName.equals(packageName)) {
                    return info.signatures[0];
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static final String getMessageDigest(byte[] paramArrayOfByte) {
        char[] arrayOfChar1 = {48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97, 98, 99, 100, 101, 102};
        try {
            MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
            localMessageDigest.update(paramArrayOfByte);
            byte[] arrayOfByte  = localMessageDigest.digest();
            int    i            = arrayOfByte.length;
            char[] arrayOfChar2 = new char[i * 2];
            int    j            = 0;
            int    k            = 0;
            while (true) {
                if (j >= i)
                    return new String(arrayOfChar2);
                int m = arrayOfByte[j];
                int n = k + 1;
                arrayOfChar2[k] = arrayOfChar1[(0xF & m >>> 4)];
                k = n + 1;
                arrayOfChar2[n] = arrayOfChar1[(m & 0xF)];
                j++;
            }
        } catch (Exception localException) {
        }
        return null;
    }

}
