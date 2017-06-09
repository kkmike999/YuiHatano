package net.sf.jsqlparser.statement.insert;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * Created by kkmike999 on 2017/06/09.
 * <p>
 * {@linkplain Insert}代理
 */
public class InsertOrReplaceProxy implements MethodInterceptor {

    Insert realInsert;

    /**
     * 创建{@linkplain Insert}代理实例
     *
     * @return
     */
    public Insert getInstance(Class<Insert> clazz, Insert realInsert) {
        this.realInsert = realInsert;

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        // 设置回调方法
        enhancer.setCallback(this);

        // 创建代理对象
        return (Insert) enhancer.create();
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {

        if (method.getName().equals("toString") && args.length == 0) {
            String sql = toString((String) proxy.invoke(realInsert, args));
            return sql;
        }
        return proxy.invoke(realInsert, args);
    }

    public String toString(String sql) {

        String[] sqlParts = new String[2];
        sqlParts[0] = sql.substring(0, "INSERT".length());
        sqlParts[1] = sql.substring("INSERT".length(), sql.length());

        sql = "INSERT OR REPLACE" + sqlParts[1];

        return sql;
    }
}
