package net.kb.test.library.afinal;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Created by kkmike999 on 2017/06/08.
 */
public class FinalDbUtils {

    /**
     * 清理FinalDb缓存
     */
    public static void clear() {
        try {
            Class fdbClazz = Class.forName("net.tsz.afinal.FinalDb");

            Field daoMapField = fdbClazz.getDeclaredField("daoMap");
            daoMapField.setAccessible(true);

            Map<String, Object> daoMap = (Map<String, Object>) daoMapField.get(null);
            daoMap.clear();

            Class tableInfoClazz = Class.forName("net.tsz.afinal.db.table.TableInfo");

            Field tableInfoMapField = tableInfoClazz.getDeclaredField("tableInfoMap");
            tableInfoMapField.setAccessible(true);

            Map<String, Object> infoMap = (Map<String, Object>) tableInfoMapField.get(null);
            infoMap.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
