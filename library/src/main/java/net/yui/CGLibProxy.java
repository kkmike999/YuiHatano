package net.yui;

import android.shadow.Shadow;

import net.yui.utils.ReflectUtils;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by kkmike999 on 2017/06/06.
 */
public class CGLibProxy implements MethodInterceptor {

    private Object shadowObject;

    /**
     * 创建代理实例
     *
     * @param shadowObject shadow类，调用proxy类方法时，实际是执行shadow类方法
     * @return
     */
    public <T> T proxy(Class<T> clazz, Object shadowObject) {
        this.shadowObject = shadowObject;

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        // 设置回调方法
        enhancer.setCallback(this);

        // 创建代理对象
        T proxy = (T) enhancer.create();

        if (shadowObject instanceof Shadow) {
            ((Shadow) shadowObject).setProxyObject(proxy);
        }
        return proxy;
    }

    /**
     * 创建代理实例
     *
     * @param shadowObject shadow类，调用proxy类方法时，实际是执行shadow类方法
     * @return
     */
    public <T> T proxy(Class<T> clazz, Object shadowObject, Class[] argTypes, Object[] args) {
        this.shadowObject = shadowObject;

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        // 设置回调方法
        enhancer.setCallback(this);

        // 创建代理对象
        T proxy = (T) enhancer.create(argTypes, args);

        if (this.shadowObject instanceof Shadow) {
            ((Shadow) this.shadowObject).setProxyObject(proxy);
        }
        return proxy;
    }

    /**
     * 实现MethodInterceptor接口要重写的方法。
     * 回调方法
     */
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        Class[] paramTypes = method.getParameterTypes();

        // 找到realObject对象中，一模一样的方法
        Method method2 = ReflectUtils.findMethod(shadowObject.getClass(), method.getName(), paramTypes);

        if (method2 == null) {
            throw new RuntimeException("method \'" + shadowObject.getClass() + "." + method.getName() + "\' not found.");
        }

        try {
            return method2.invoke(shadowObject, args);
        } catch (InvocationTargetException e) {
            Throwable targetEx = e.getTargetException();

            throw targetEx;
        }
    }
}
