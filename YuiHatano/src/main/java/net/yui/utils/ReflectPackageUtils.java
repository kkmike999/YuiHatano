package net.yui.utils;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by kkmike999 on 2018/03/01.
 */
public class ReflectPackageUtils {

    public static List<Class> getClasses(String codePath, String packageName) {
        List<String> classesName = getClassesName(codePath, packageName);
        List<Class>  classes     = new ArrayList<>();
        try {
            for (String name : classesName) {
                classes.add(Class.forName(name));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return classes;
    }

    public static List<String> getClassesName(String codePath, String packageName) {
        File dir = new File(codePath, packageName.replace(".", "/"));

        if (!dir.exists() || !dir.isDirectory() || dir.length() == 0) {
            return new ArrayList<>();
        }
        String[] names = dir.list(new FilenameFilter() {
            @Override
            public boolean accept(File file, String name) {
                return name.endsWith(".java");
            }
        });

        for (int i = 0; i < names.length; i++) {
            String name = names[i];
            names[i] = packageName + "." + name.substring(0, name.length() - 5);
        }

        return Arrays.asList(names);
    }

    /**
     * 筛选出 该父类的子类（有可能是接口）
     *
     * @param classes
     * @param superclass 父类
     * @param <T>
     *
     * @return
     */
    public static <T> List<Class<T>> filter(List<Class> classes, Class<T> superclass) {
        List<Class<T>> derivedClasses = new ArrayList<>();// 子类

        for (Class clazz : classes) {
            if (superclass.isAssignableFrom(clazz) && clazz != superclass) {
                derivedClasses.add(clazz);
            }
        }

        return derivedClasses;
    }
}
