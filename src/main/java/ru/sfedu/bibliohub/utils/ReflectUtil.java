package ru.sfedu.bibliohub.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectUtil {
    private static <T> Constructor<T> getConstructor(Class<T> type) {
        try {
            return type.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T getEmptyObject(Class<T> type) {
        Constructor<T> constructor = getConstructor(type);
        try {
            return constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private static <T> Method getIdMethod(Class<T> type) {
        try {
            return type.getMethod(Constants.GET_ID);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> long getId(T bean) {
        Method getId = getIdMethod(bean.getClass());
        try {
            return (long) getId.invoke(bean);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static <T> Method setIdMethod(Class<T> type) {
        try {
            return type.getMethod(Constants.SET_ID, Long.TYPE);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> void setId(T bean, long id) {
        Method getId = setIdMethod(bean.getClass());
        try {
            getId.invoke(bean, id);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
