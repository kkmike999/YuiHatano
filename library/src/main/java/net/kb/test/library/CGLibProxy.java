package net.kb.test.library;

import net.kb.test.library.utils.ReflectUtils;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * Created by kkmike999 on 2017/06/06.
 */
public class CGLibProxy implements MethodInterceptor {

    private Object realObject;

    /**
     * 创建代理实例
     *
     * @param realObject
     * @return
     */
    public <T> T getInstance(Class<T> clazz, Object realObject) {
        this.realObject = realObject;

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        // 设置回调方法
        enhancer.setCallback(this);

        // 创建代理对象
        return (T) enhancer.create();
    }

    /**
     * 创建代理实例
     *
     * @param realObject
     * @return
     */
    public <T> T getInstance(Class<T> clazz, Object realObject, Class[] argTypes, Object[] args) {
        this.realObject = realObject;

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        // 设置回调方法
        enhancer.setCallback(this);

        // 创建代理对象
        return (T) enhancer.create(argTypes, args);
    }

    /**
     * 实现MethodInterceptor接口要重写的方法。
     * 回调方法
     */
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        Class[] paramTypes = method.getParameterTypes();

        // 找到realObject对象中，一模一样的方法
        Method method2 = ReflectUtils.findMethod(realObject.getClass(), method.getName(), paramTypes);

        return method2.invoke(realObject, args);
    }
}
