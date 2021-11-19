package com.jcloud.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

/**
 * 此方法性能中等但是功能比较全,大量数据是建议使用cglibCopy哈
 * 手动Copy > cglibCopy > springBeanUtils > apachePropertyUtils > apacheBeanUtils 可以理解为: 手工复制 > cglib > 反射 > Dozer
 */
//区创属性拷贝工具类     属性为空时根据数据类型给默认值
@Slf4j
public abstract class HBeanUtils extends org.springframework.beans.BeanUtils {

    /**
     * 从org.springframework.beans.BeanUtils类中直接复制过来
     *
     * @param source
     * @param target
     * @throws BeansException
     */
    public static void copyProperties(Object source, Object target) throws BeansException {
        copyProperties(source, target, null, null, (String[]) null);
    }

    //无参构造
    public HBeanUtils() {
    }

    //三个参数
    public static void copyProperties(Object source, Object target,String[] includes, String... ignoreProperties) throws BeansException {
        copyProperties(source, target, (Class) null, includes, ignoreProperties);
    }

    /**
     * 从org.springframework.beans.BeanUtils类中直接复制过来,修改部分代码
     *
     * @param source
     * @param target
     * @param editable
     * @param includeProperties
     * @param ignoreProperties
     * @throws BeansException
     */
    private static void copyProperties(Object source, Object target, Class<?> editable, String[] includeProperties, String[] ignoreProperties)
            throws BeansException {

        Assert.notNull(source, "Source must not be null");
        Assert.notNull(target, "Target must not be null");

        Class<?> actualEditable = target.getClass();
        if (editable != null) {
            if (!editable.isInstance(target)) {
                throw new IllegalArgumentException("Target class [" + target.getClass().getName() +
                        "] not assignable to Editable class [" + editable.getName() + "]");
            }
            actualEditable = editable;
        }
        java.beans.PropertyDescriptor[] targetPds = getPropertyDescriptors(actualEditable);
        java.util.List<String> ignoreList = (ignoreProperties != null) ? Arrays.asList(ignoreProperties) : null;
        java.util.List<String> includeList = (includeProperties != null) ? Arrays.asList(includeProperties) : null;
        for (java.beans.PropertyDescriptor targetPd : targetPds) {
            Method writeMethod = targetPd.getWriteMethod();
            if (writeMethod != null) {
                if (includeProperties != null && !includeList.isEmpty()) {
                    if (includeList.contains(targetPd.getName())) {
                        setValue(targetPd, writeMethod, source, target);
                    }
                    continue;
                }

                if (ignoreProperties == null || !ignoreList.contains(targetPd.getName())) {
                    setValue(targetPd, writeMethod, source, target);
                }
            }
        }
    }


    private static void setValue(java.beans.PropertyDescriptor targetPd, Method writeMethod, Object source, Object target) {
        java.beans.PropertyDescriptor sourcePd = getPropertyDescriptor(source.getClass(), targetPd.getName());
        if (sourcePd != null) {
            Method readMethod = sourcePd.getReadMethod();
            if (readMethod != null &&
                    ClassUtils.isAssignable(writeMethod.getParameterTypes()[0], readMethod.getReturnType())) {
                try {
                    if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                        readMethod.setAccessible(true);
                    }
                    Object value = readMethod.invoke(source);
                    // 判断被复制的属性是否为null, 如果不为null才复制
                    if (value != null) {
                        if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                            writeMethod.setAccessible(true);
                        }
                        writeMethod.invoke(target, value);
                    } else { // 忽略none
//                                if (sourcePd.getPropertyType().getName().equals("java.lang.String")) {
//                                    value = "";
//                                } else if (sourcePd.getPropertyType().getName().equals("java.lang.Integer")){
//                                    value = 0;
//                                } else if (sourcePd.getPropertyType().getName().equals("java.lang.Long")){
//                                    value = 0L;
//                                }
//                                if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
//                                    writeMethod.setAccessible(true);
//                                }
//                                writeMethod.invoke(target, value);
                    }
                } catch (Throwable ex) {
                    throw new FatalBeanException(
                            "Could not copy property '" + targetPd.getName() + "' from source to target", ex);
                }
            }
        }
    }

}
