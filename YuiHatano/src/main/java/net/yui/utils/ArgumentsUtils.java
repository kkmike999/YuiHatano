package net.yui.utils;

import java.lang.reflect.Constructor;
import java.util.Random;

/**
 * Created by kkmike999 on 2018/12/20.
 */
public class ArgumentsUtils {

    /**
     * 获取构造函数，参数类型
     *
     * @param clazz
     *
     * @return
     */
    public static Class[] getConstructorsArgumensTypes(Class clazz) {
        Constructor[] constructors = clazz.getDeclaredConstructors();

        if (constructors == null || constructors.length == 0) {
            return new Class[0];
        }
        return constructors[0].getParameterTypes();
    }

    /**
     * 根据参数类型，生成对象数组
     *
     * @param argTypes 参数类型数组
     *
     * @return
     */
    public static Object[] getArgumens(Class[] argTypes) {
        if (argTypes == null || argTypes.length == 0) {
            return new Object[0];
        }

        Object[] args = new Object[argTypes.length];

        for (int i = 0; i < argTypes.length; i++) {
            Class  type = argTypes[i];
            Object arg  = null;

            if (type.equals(Integer.class) || type.equals(int.class)) {
                arg = new Random().nextInt(9999);
            } else if (type.equals(Long.class) || type.equals(long.class)) {
                arg = new Random().nextLong();
            } else if (type.equals(Double.class) || type.equals(double.class)) {
                arg = new Random().nextDouble();
            } else if (type.equals(Float.class) || type.equals(float.class)) {
                arg = new Random().nextFloat();
            } else if (type.equals(Boolean.class) || type.equals(boolean.class)) {
                arg = new Random().nextBoolean();
            } else if (CharSequence.class.isAssignableFrom(type)) {
                String name = type.getSimpleName();
                arg = name + "_" + (int) (Math.random() * 1000);
            } else if (type == Object.class) {
                arg = new Object();
            }

            args[i] = arg;
        }

        return args;
    }
}
