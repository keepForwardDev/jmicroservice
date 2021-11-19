package com.jcloud.common.util;

import org.springframework.util.ReflectionUtils;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ReflectUtil {

    /**
     * 反射获取值
     * @param fieldName
     * @param clazz
     * @param target
     * @return
     */
    public static Object getFieldValue(String fieldName, Class clazz, Object target) {
        Field field = ReflectionUtils.findField(clazz, fieldName);
        if (field != null) {
            field.setAccessible(true);
            try {
                return field.get(target);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 反射获取值
     * @param field
     * @param target
     * @return
     */
    public static Object getFieldValue(Field field, Object target) {
        field.setAccessible(true);
        try {
            return field.get(target);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 反射赋值
     * @param fieldName
     * @param value
     * @param clazz
     * @param target
     */
    public static void setFieldValue(String fieldName, Object value, Class clazz, Object target) {
        Field field = ReflectionUtils.findField(clazz, fieldName);
        if (field != null) {
            field.setAccessible(true);
            try {
                field.set(target, value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public static void setFieldValue(Field field, Object target, Object value) {
        field.setAccessible(true);
        try {
            field.set(target, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    public static <T> T instanceClazz(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取obj对象fieldName的Field
     *
     * @param obj
     * @param fieldName
     * @return
     */
    public static Field getFieldByFieldName(Object obj, String fieldName) {
        for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass
                .getSuperclass()) {
            try {
                return superClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
            }
        }
        return null;
    }

    /**
     * 获取obj对象fieldName的属性值
     *
     * @param obj
     * @param fieldName
     * @return
     * @throws SecurityException
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static Object getValueByFieldName(Object obj, String fieldName)
            throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field field = getFieldByFieldName(obj, fieldName);
        Object value = null;
        if (field != null) {
            if (field.isAccessible()) {
                value = field.get(obj);
            } else {
                field.setAccessible(true);
                value = field.get(obj);
                field.setAccessible(false);
            }
        }
        return value;
    }

    /**
     * 设置obj对象fieldName的属性值
     *
     * @param obj
     * @param fieldName
     * @param value
     * @throws SecurityException
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static void setValueByFieldName(Object obj, String fieldName, Object value)
            throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        // Field field = obj.getClass().getDeclaredField(fieldName);
        Field field = getDeclaredField(obj, fieldName);
        if (field.isAccessible()) {
            field.set(obj, value);
        } else {
            field.setAccessible(true);
            field.set(obj, value);
            field.setAccessible(false);
        }
    }

    ////////////////////////////////

    /**
     * 循环向上转型, 获 * @param object : 子类对象
     *
     * @param methodName     : 父类中的方法名
     * @param parameterTypes : 父类中的方法参数类型
     * @return 父类中的方法对象
     */

    public static Method getDeclaredMethod(Object object, String methodName, Class<?>... parameterTypes) {
        Method method = null;

        for (Class<?> clazz = object.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                method = clazz.getDeclaredMethod(methodName, parameterTypes);
                return method;
            } catch (Exception e) {
                e.printStackTrace();
                // 这里甚么都不能抛出去。
                // 如果这里的异常打印或者往外抛，则就不会进入
            }
        }

        return null;
    }

    /**
     * 直接调用对象方法, 而忽略修饰符(private, protected, default)
     *
     * @param object         : 子类对象
     * @param methodName     : 父类中的方法名
     * @param parameterTypes : 父类中的方法参数类型
     * @param parameters     : 父类中的方法参数
     * @return 父类中方法的执行结果
     */

    public static Object invokeMethod(Object object, String methodName, Class<?>[] parameterTypes,
                                      Object[] parameters) {
        // 根据 对象、方法名和对应的方法参数 通过取 Method 对象
        Method method = getDeclaredMethod(object, methodName, parameterTypes);

        // 抑制Java对方法进行检查,主要是针对私有方法而言
        method.setAccessible(true);

        try {
            if (null != method) {

                // 调用object 的 method 所代表的方法，其方法的参数是 parameters
                return method.invoke(object, parameters);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 循环向上转型, 获 * @param object : 子类对象
     *
     * @param fieldName : 父类中 * @return 父类中
     */

    public static Field getDeclaredField(Object object, String fieldName) {
        Field field = null;

        Class<?> clazz = object.getClass();

        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {

                field = clazz.getDeclaredField(fieldName);
                return field;
            } catch (Exception e) {
                e.printStackTrace();
                // 这里甚么都不能抛出去。
                // 如果这里的异常打印或者往外抛，则就不会进入
            } finally {

            }
        }

        return null;
    }

    /**
     * 直接设置对象属性值, 忽略 private/protected 修饰符, 也 * @param object : 子类对象
     *
     * @param fieldName : 父类中 * @param value : 将要设置的值
     */

    public static void setFieldValue(Object object, String fieldName, Object value) {

        // 根据 对象和属性名通过取 Field对象
        // Field field = getDeclaredField(object, fieldName) ;
        Field field = getFieldByFieldName(object, fieldName);
        // System.out.println(field.getName());

        // 抑制Java对其的检查
        field.setAccessible(true);

        try {
            // 将 object 中 field 所代表的值 设置为 value
            field.set(object, value);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    /**
     * 直接读的属性值, 忽略 private/protected 修饰符, 也 * @param object : 子类对象
     *
     * @param fieldName : 父类中 * @return : 父类中
     */

    public static Object getFieldValue(Object object, String fieldName) {

        // 根据 对象和属性名通过取 Field对象
        Field field = getDeclaredField(object, fieldName);

        // 抑制Java对其的检查
        field.setAccessible(true);

        try {
            // 获的属性值
            return field.get(object);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /*
     * 获取说有属性，包括父类
     */
    public static Field[] getFieldAll(Class clz) {
        List<Field> fs = new ArrayList<Field>();
        Field[] parent = clz.getSuperclass().getDeclaredFields();
        Field[] field = clz.getDeclaredFields();
        fs.addAll(new ArrayList<Field>(Arrays.asList(parent)));
        fs.addAll(new ArrayList<Field>(Arrays.asList(field)));

        Field[] fields = new Field[fs.size()];
        return fs.toArray(fields);

    }

    /*
     * 获取所有父类属性 无线继承
     */
    public static Field[] getAllField(Class clzz) {
        List<Field> fs = new ArrayList<Field>();
        Field[] field = clzz.getDeclaredFields();
        fs.addAll(new ArrayList<Field>(Arrays.asList(field)));
        Class parentclass = clzz.getSuperclass();
        boolean parent = true;
        while (parent) {
            if (parentclass != null) {
                Field[] parentarr = parentclass.getDeclaredFields();
                fs.addAll(new ArrayList<Field>(Arrays.asList(parentarr)));
            } else {
                parent = false;
                break;
            }
            parentclass = parentclass.getSuperclass();
        }

        Field[] fields = new Field[fs.size()];
        return fs.toArray(fields);

    }

    /**
     * 用户获取类上的泛型 class 类型 示例 B<A> 获取A类型为， (B.class, 0)
     *
     * @author jiaxm
     * @date 2020年5月25日
     * @param clazz
     * @param index
     * @return
     */
    public static Class<?> getSuperClassGenericType(final Class<?> clazz, final int index) {
        Type genType = clazz.getGenericSuperclass();
        if (!(genType instanceof ParameterizedType)) {
            System.out.println(String.format("Warn: %s's superclass not ParameterizedType", clazz.getSimpleName()));
            return Object.class;
        } else {
            Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
            if (index < params.length && index >= 0) {
                if (!(params[index] instanceof Class)) {
                    System.out
                            .println(String.format("Warn: %s not set the actual class on superclass generic parameter",
                                    clazz.getSimpleName()));
                    return Object.class;
                } else {
                    return (Class) params[index];
                }
            } else {
                System.out.println(String.format("Warn: Index: %s, Size of %s's Parameterized Type: %s .", index,
                        clazz.getSimpleName(), params.length));
                return Object.class;
            }
        }
    }

    /**
     * 用于获取实体所有字段，跟上面不同的是，支持注解@Transient，过滤静态字段
     *
     * @author jiaxm
     * @date 2020年5月26日
     * @param clazz
     * @return
     */
    public static List<Field> getEntityAllField(Class<?> clazz) {
        if (null == clazz) {
            return null;
        }
        List<Field> fieldList = new LinkedList<Field>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            // support serialize rule
            if (Modifier.isTransient(field.getModifiers())) {
                continue;
            }
            fieldList.add(field);
        }
        // handle super class
        Class<?> superClass = clazz.getSuperclass();
        if (superClass.equals(Object.class)) {
            return fieldList;
        }
        fieldList.addAll(getEntityAllField(superClass));
        return fieldList;
    }

}
