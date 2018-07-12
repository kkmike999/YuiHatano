package android.content;

import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * Created by kkmike999 on 2017/06/01.
 */
public class ContentValuesHelper {

    /**
     * 反射获取{@linkplain ContentValues}#mValues 参数
     *
     * @param from
     * @return
     */
    protected static HashMap<String, Object> getValues(ContentValues from) {
        try {
            Field mValuesField = ContentValues.class.getDeclaredField("mValues");
            mValuesField.setAccessible(true);

            return (HashMap<String, Object>) mValuesField.get(from);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }
}
