package com.anii.querydsl.common.utils.function;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.stereotype.Component;

import java.lang.invoke.SerializedLambda;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 根据方法引用获得数据库字段名
 */
@Component
public class LambdaUtils {

    private static ClassLoader classLoader;

    private static Map<String, WeakReference<String>> COLUMN_MAP = new ConcurrentHashMap<>();

    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) {
        classLoader = applicationContext.getClassLoader();
    }

    public static <T, R> String extract(SFunction<T, R> func) {
        // 直接调用writeReplace
        SerializedLambda resolve = resolve(func);
        return getColumnName(resolve);
    }

    private static <T, R> SerializedLambda resolve(SFunction<T, R> func) {
        try {
            // 直接调用writeReplace
            Method writeReplace = func.getClass().getDeclaredMethod("writeReplace");
            writeReplace.setAccessible(true);
            //反射调用
            Object sl = writeReplace.invoke(func);
            SerializedLambda serializedLambda = (SerializedLambda) sl;
            return serializedLambda;
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getColumnName(SerializedLambda sl) {
        String implMethodName = sl.getImplMethodName();
        String name = Optional.ofNullable(implMethodName)
                .map(COLUMN_MAP::get)
                .map(WeakReference::get)
                .orElseGet(() -> {
                    try {
                        String implClass = sl.getImplClass().replace("/", ".");
                        Class<?> clazz = classLoader.loadClass(implClass);
                        String columnByMethod = getColumnByMethod(sl, clazz);
                        if (StringUtils.isNotBlank(columnByMethod)) {
                            return columnByMethod;
                        }

                        String columnNameByField = getColumnNameByField(sl, clazz);
                        return columnNameByField;
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                });
        if (StringUtils.isNotBlank(name)) {
            COLUMN_MAP.put(implMethodName, new WeakReference<>(name));
            return name;
        }
        throw new RuntimeException("不支持读取该方法引用-" + implMethodName);
    }

    private static String getColumnByMethod(SerializedLambda sl, Class clazz) {
        try {
            String implMethodName = sl.getImplMethodName();
            Method declaredMethod = clazz.getDeclaredMethod(implMethodName);

            // 查找是否有方法注解
            Column column = declaredMethod.getAnnotation(Column.class);
            String name = Optional.ofNullable(column)
                    .map(Column::value)
                    .filter(StringUtils::isNotBlank)
                    .orElse("");
            return name;
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getColumnNameByField(SerializedLambda sl, Class clazz) {

        String implMethodName = sl.getImplMethodName();
        String fieldName = getFieldNameFromGetter(implMethodName);
        String name = fieldName;
        try {
            Field declaredField = clazz.getDeclaredField(fieldName);
            Column column = declaredField.getAnnotation(Column.class);
            if (Objects.nonNull(column) && StringUtils.isNotBlank(column.value())) {
                name = column.value();
            }
        } catch (NoSuchFieldException e) {
        }
        return name;
    }

    private static String getFieldNameFromGetter(String getterName) {
        if (getterName.startsWith("get")) {
            return getterName.substring(3, 4).toLowerCase() + getterName.substring(4);
        } else if (getterName.startsWith("is")) {
            return getterName.substring(2, 3).toLowerCase() + getterName.substring(3);
        } else {
            throw new IllegalArgumentException("Invalid getter method name: " + getterName);
        }
    }

}
