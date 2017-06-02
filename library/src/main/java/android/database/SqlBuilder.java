package android.database;

import java.util.Collection;
import java.util.List;

/**
 * Created by kkmike999 on 2016/09/07.
 * <p>
 * sql语句工具类
 */
public class SqlBuilder {

    /**
     * 拼接sql语句
     * <p/>
     * 例子, sql("insert into person(name, age) values(?,?)", new Object[]{"name", 4});
     *
     * @param sql
     * @param args Object支持int、long、float、double、String、Collection子类.<br>注意，参数是String，sql语句本身不需要"' '"(单引号)
     * @return
     */
    public static String sql(String sql, String... args) {
        return sql(sql, (Object[]) args);
    }

    /**
     * 拼接sql语句
     * <p/>
     * 例子, sql("insert into person(name, age) values(?,?)", new Object[]{"name", 4});
     *
     * @param sql
     * @param args Object支持int、long、float、double、String、Collection子类.<br>注意，参数是String，sql语句本身不需要"' '"(单引号)
     * @return
     */
    public static String sql(String sql, Object... args) {

        sql = sql.replace("?", "%s");

        String sqlResult = String.format(sql, transformArgs(args));

        return sqlResult;
    }

    /**
     * 拼接sql语句
     * <p/>
     * 例子, sql("insert into person(name, age) values(?,?)", new Object[]{"name", 4});
     *
     * @param sql
     * @param args Object支持int、long、float、double、String、Collection子类.<br>注意，参数是String，sql语句本身不需要"' '"(单引号)
     * @return
     */
    public static String sql(String sql, List<String> args) {

        sql = sql.replace("?", "%s");

        String sqlResult = String.format(sql, join(args));

        return sqlResult;
    }

    /**
     * 转换一些参数类型。例如，List -> String
     *
     * @param args
     * @return
     */
    protected static Object[] transformArgs(Object... args) {

        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];

            if (arg instanceof Collection) {
                String stringArg = arg.toString().replace("[", "").replace("]", "");

                args[i] = stringArg;

            } else if (arg instanceof CharSequence) {
                String stringArg = arg.toString();

                // string类型，前后要有"' '"(单引号)

//                if (!stringArg.startsWith("'")) {
//                    stringArg = "'" + stringArg;
//                }
//                if (!stringArg.endsWith("'")) {
//                    stringArg = stringArg + "'";
//                }

                args[i] = "'" + stringArg + "'";
            }
        }

        return args;
    }

    /**
     * @param list
     * @param <T>
     * @return [a, b, c] -> "a","b","c"
     */
    public static <T extends CharSequence> String join(List<T> list) {

        StringBuilder sb = new StringBuilder();

        for (T t : list) {
            sb.append("\'");
            sb.append(t.toString());
            sb.append("\'");
            sb.append(",");
        }

        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }

        return sb.toString();
    }
}
