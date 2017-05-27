package net.kb.test.library.utils;

import java.lang.reflect.Method;

/**
 * Created by kkmike999 on 2017/05/26.
 */
public class ReflectUtils {

    public static Object invoke(Object receiver, String methodName, Object[] arguments) {
        try {
            Class clazz = receiver.getClass();

            Method method = findMethod(clazz, methodName, toTypes(arguments));

            return method.invoke(receiver, arguments);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Method findMethod(Class clazz, String methodName, Class[] argumentTypes) {
        while (!clazz.equals(Object.class)) {
            Method[] ms = clazz.getDeclaredMethods();

            for (Method m : ms) {
                if (m.getName().equals(methodName) && m.getParameterTypes().length == argumentTypes.length) {

                    Class[] paramTypes = m.getParameterTypes();

                    for (int i = 0; i < paramTypes.length; i++) {
                        Class pt      = paramTypes[i];
                        Class objType = argumentTypes[i];

//                        if (pt.equals(int.class) || objType.equals(int.class)) {
//                            pt = int.class;
//                            objType = int.class;
//                        }

                        // 其实这样还是有bug：
                        // 1.List和ArrayList同时匹配List对象，但可能存在两个方法
                        // 2.基本类型int、float、double、boolean
                        if (pt.isAssignableFrom(objType)) {
                            return m;
                        }
                    }
                }
            }

            clazz = clazz.getSuperclass();
        }
        return null;
    }

    private static Class[] toTypes(Object[] arguments) {
        Class[] types = new Class[arguments.length];

        for (int i = 0; i < arguments.length; i++) {
            types[i] = arguments[i].getClass();
        }
        return types;
    }
}
