package net.yui.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by kkmike999 on 2017/05/26.
 */
public class ReflectUtils {

    public static boolean hasClass(String className) {
        try {
            Class.forName(className);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static void setField(Object receiver, String fieldName, Object value) {
        Class clazz = receiver.getClass();

        while (!clazz.equals(Object.class)) {
            Field[] fields = clazz.getDeclaredFields();

            for (Field field : fields) {
                if (field.getName().equals(fieldName)) {
                    field.setAccessible(true);
                    try {
                        field.set(receiver, value);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    return;
                }
            }

            clazz = clazz.getSuperclass();
        }
    }

    /**
     * 执行方法
     *
     * @param receiver
     * @param methodName
     * @param arguments  传入参数
     *
     * @return
     */
    public static Object invoke(Object receiver, String methodName, Object... arguments) {
        try {
            Class   clazz         = receiver.getClass();
            Class[] argumentTypes = arguments == null || arguments.length == 0 ? new Class[0] : toTypes(arguments);
            Method  method        = findMethod(clazz, methodName, argumentTypes);

            return method.invoke(receiver, arguments);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 执行静态方法
     *
     * @param className
     * @param methodName
     * @param arguments  参数
     *
     * @return
     */
    public static Object invokeStatic(String className, String methodName, Object... arguments) {
        try {
            Class clazz = Class.forName(className);
            return invokeStatic(clazz, methodName, arguments);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object invokeStatic(Class clazz, String methodName, Object... arguments) {
        try {
            Class[] argumentTypes = arguments == null || arguments.length == 0 ? new Class[0] : toTypes(arguments);
            Method  method        = findMethod(clazz, methodName, argumentTypes);

            return method.invoke(null, arguments);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object newObject(String className, Object... args) {
        try {
            Class       clazz       = Class.forName(className);
            Class[]     argTypes    = args == null || args.length == 0 ? new Class[0] : toTypes(args);
            Constructor constructor = clazz.getConstructor(argTypes);

            return constructor.newInstance(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

//    public static Object invoke(Object receiver, String methodName) {
//        try {
//            Class clazz = receiver.getClass();
//
//            Method method = findMethod(clazz, methodName, new Class[0]);
//
//            return method.invoke(receiver);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    public static Method findMethod(Class clazz, String methodName, Class... argumentTypes) {
        while (!clazz.equals(Object.class)) {
            Method[] ms = clazz.getDeclaredMethods();

            for (Method m : ms) {
                if (m.getName().equals(methodName) && m.getParameterTypes().length == argumentTypes.length) {

                    // 没有参数
                    if (argumentTypes.length == 0) {
                        return m;
                    }

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

            String name = types[i].getName();

            // 处理cglib代理类
            if (name.contains("$$EnhancerByCGLIB")) {
                try {
                    String readClassName = name.substring(0, name.indexOf("$$EnhancerByCGLIB"));
                    Class  type          = Class.forName(readClassName);

                    types[i] = type;
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return types;
    }
}
