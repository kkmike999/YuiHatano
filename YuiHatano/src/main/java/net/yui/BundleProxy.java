package net.yui;

import android.os.Bundle;

import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by kkmike999 on 2017/06/06.
 */
public class BundleProxy extends CGLibProxy {

    Map<String, Object> map = new ConcurrentHashMap<>();

    /**
     * 创建代理实例
     * <p>
     * //     * @param shadowObject shadow类，调用proxy类方法时，实际是执行shadow类方法
     *
     * @return
     */
    public Bundle proxy(Object shadowBundle) {
        return super.proxy(Bundle.class, shadowBundle);
    }

    /**
     * 实现MethodInterceptor接口要重写的方法。
     * 回调方法
     */
    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        String methodName = method.getName();

        if (args == null) {
            args = new Object[0];
        }

        // get、put方法，第一个参数一定是String key

        if (methodName.startsWith("put") && args.length > 1 && args[0] instanceof String) {
            map.put(args[0].toString(), args[1]);
            return null;
        } else if (methodName.startsWith("get") && args.length > 0 && args[0] instanceof String) {
            return map.get(args[0].toString());
        }
        return super.intercept(obj, method, args, proxy);
    }
}
