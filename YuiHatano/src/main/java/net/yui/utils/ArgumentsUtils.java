package net.yui.utils;

import android.annotation.SuppressLint;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
        Constructor[] constructors = getNotPrivateConstructors(clazz);

        if (constructors == null || constructors.length == 0) {
            return new Class[0];
        }
        return constructors[0].getParameterTypes();
    }

    /**
     * 是否有 无参构造函数，并且非private（因为cglib不接受private构造函数）.
     *
     * @param clazz
     *
     * @return
     */
    public static boolean hasNoArgumentsConstructor(Class clazz) {
        Constructor[] constructors = getNotPrivateConstructors(clazz);

        if (constructors == null || constructors.length == 0) {
            return true;
        }

        boolean hasNoArguments = false;

        for (Constructor constructor : constructors) {
            Class[] types = constructor.getParameterTypes();
            //            fixModify(constructor);

            if (types == null || types.length == 0) {
                hasNoArguments = true;
                break;
            }
        }

        return hasNoArguments;
    }

    //    /**
    //     * 修改构造函数修饰符（仅仅是短暂修改，重新getDeclaredConstructors()回到解放前）
    //     *
    //     * @param constructor
    //     */
    //    private static void fixModify(Constructor constructor) {
    //        constructor.setAccessible(true);
    //        try {
    //            Field modifiersField = Constructor.class.getDeclaredField("modifiers");
    //            modifiersField.setAccessible(true);
    //
    //            int modify = constructor.getModifiers();
    //
    //            if (Modifier.isPrivate(modify)) {
    //                modifiersField.set(constructor, (modify & ~Modifier.PRIVATE) + Modifier.PUBLIC);
    //            }
    //        } catch (Exception e) {
    //            e.printStackTrace();
    //        }
    //    }

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

    /**
     * 获取非私有Constructor
     *
     * @param clazz
     *
     * @return
     */
    @SuppressLint("NewApi")
    private static Constructor[] getNotPrivateConstructors(Class clazz) {
        Constructor[] constructors = clazz.getDeclaredConstructors();

        if (constructors == null) {
            return new Constructor[0];
        }

        List<Constructor> list = Arrays.asList(constructors).stream()
                                       .filter(new Predicate<Constructor>() {
                                           @Override
                                           public boolean test(Constructor constructor) {
                                               return !Modifier.isPrivate(constructor.getModifiers());
                                           }
                                       })
                                       .collect(Collectors.<Constructor>toList());
        return list.toArray(new Constructor[0]);
    }
}
