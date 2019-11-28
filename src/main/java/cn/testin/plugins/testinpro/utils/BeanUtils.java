package cn.testin.plugins.testinpro.utils;

import cn.testin.plugins.testinpro.annotation.Nullable;
import cn.testin.plugins.testinpro.enums.ErrorCode;
import cn.testin.plugins.testinpro.exception.CommonException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import static cn.testin.plugins.testinpro.utils.ClassUtils.getDefaultClassLoader;
import static cn.testin.plugins.testinpro.utils.other.ExceptionUtils.handlerException;
import static cn.testin.plugins.testinpro.utils.verify.ObjectUtils.isEmpty;

/**
 * @author lichengliang
 *
 * date 2019/10/29
 *
 * 用于bean反射工具
 */
public abstract class BeanUtils {

    /**
     * 实例化bean对象
     * @param clazz 需要的class
     * @param arg 参数
     * @param <T> bean的类型
     * @return bean的实例化
     * @throws ClassNotFoundException class 在classLoader中找不到时会异常抛出
     */
    @Nullable
    public static <T> T instantiation(String clazz, Object... arg) throws ClassNotFoundException {
        ClassLoader classLoader = getDefaultClassLoader();
        if (isEmpty(classLoader)) {
            return null;
        }
        Class<?> aClass = classLoader.loadClass(clazz);
        if (isEmpty(aClass)) {
            return null;
        }
        return instantiationClass((Class<T>) aClass, arg);
    }

    /**
     * 实例化bean对象
     * @param clazz 需要的class
     * @param arg 参数
     * @param <T> bean的类型
     * @return bean的实例化
     */
    @Nullable
    public static <T> T instantiationClass(Class<T> clazz, Object... arg) {
        if (isEmpty(clazz)) {
            return null;
        }
        if (clazz.isInterface()) {
            handlerException("Specified class is an interface");
        }
        Constructor<?>[] declaredConstructors = clazz.getDeclaredConstructors();
        if (isEmpty(declaredConstructors)) {
            return null;
        }

        constructor:
        for (Constructor<?> declaredConstructor : declaredConstructors) {
            int argCount = 0;
            if (!isEmpty(arg)) {
                argCount = arg.length;
            }
            int parameterCount = declaredConstructor.getParameterCount();
            if (argCount == parameterCount) {
                Class<?>[] parameterTypes = declaredConstructor.getParameterTypes();
                for (int i = 0; i < argCount; i++) {
                    if (!getAllClass(arg[i].getClass()).contains(parameterTypes[i])) {
                        continue constructor;
                    }
                }
                try {
                    makeAccessible(declaredConstructor);
                    return (T) declaredConstructor.newInstance(arg);
                } catch (Exception e) {
                    e.printStackTrace();
                    handlerException("Is it an abstract class?/Is the constructor accessible?");
                }
            }
        }
        return null;
    }

    /**
     * 获取对象所有的父class
     * @param clazz 当前对象的class
     * @return 所有class集合
     */
    public static List<Class<?>> getAllClass(Class<?> clazz) {
        if (isEmpty(clazz)) {
            return new ArrayList<>();
        }
        List<Class<?>> result = new ArrayList<>();
        result.add(clazz);
        Class<?> c = clazz;
        while (!isEmpty(c.getSuperclass())) {
            result.add(c = c.getSuperclass());
        }
        return result;
    }

    /**
     * 反射设置对象的操作许可
     * @param ctor 操作对象
     */
    public static void makeAccessible(Constructor<?> ctor) {
        boolean isPublic =
                Modifier.isPublic(ctor.getModifiers())
                || Modifier.isPublic(ctor.getDeclaringClass().getModifiers());

        if (!isPublic && !ctor.isAccessible()) {
            ctor.setAccessible(true);
        }
    }
}
