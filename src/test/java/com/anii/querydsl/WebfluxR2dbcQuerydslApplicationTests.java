package com.anii.querydsl;

import com.anii.querydsl.entity.User;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Function;

//@SpringBootTest
class WebfluxR2dbcQuerydslApplicationTests {

    @FunctionalInterface
    interface SFunction<T, R> extends Function<T, R>, Serializable {
    }

    @Test
    void contextLoads() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException {
        SFunction<User, String> sf = User::getPassword;

        // 直接调用writeReplace
        Method writeReplace = sf.getClass().getDeclaredMethod("writeReplace");
        writeReplace.setAccessible(true);
        //反射调用
        Object sl = writeReplace.invoke(sf);
        java.lang.invoke.SerializedLambda serializedLambda = (java.lang.invoke.SerializedLambda) sl;

        String implMethodName = serializedLambda.getImplMethodName();
        String implMethodSignature = serializedLambda.getImplMethodSignature();
        String implClass = serializedLambda.getImplClass();
        String replace = implClass.replace("/", ".");
        Class<?> aClass = Class.forName(replace);
        Method declaredMethod = aClass.getDeclaredMethod(implMethodName);
        Annotation[] annotations = declaredMethod.getAnnotations();

    }

}
