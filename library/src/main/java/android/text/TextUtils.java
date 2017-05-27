package android.text;

/**
 * Created by kkmike999 on 2016/10/14.
 * <p>
 * mock 安卓原生类android.text.TextUtils
 */
public class TextUtils {

    public static boolean isEmpty(CharSequence str) {
        if (str == null || str.equals("")) {
            return true;
        }
        return false;
    }
}
