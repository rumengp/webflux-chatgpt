package com.anii.querydsl;

import com.anii.querydsl.entity.User;
import com.anii.querydsl.enums.chat.ModelTypeEnum;
import com.anii.querydsl.gpt.chat.Completion;
import com.anii.querydsl.gpt.DefaultGPTClient;
import com.anii.querydsl.gpt.GPTClient;
import com.anii.querydsl.gpt.chat.Message;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.function.Function;

@SpringBootTest
class WebfluxR2dbcQuerydslApplicationTests {

    @FunctionalInterface
    interface SFunction<T, R> extends Function<T, R>, Serializable {
    }

    @Autowired
    GPTClient client;

    @Test
    void contextLoads() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException {
        SFunction<User, String> sf = User::getPassword;

        // 直接调用writeReplace
        Method writeReplace = sf.getClass().getDeclaredMethod("writeReplace");
        writeReplace.setAccessible(true);
        // 反射调用
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

    @Test
    public void testGetYoutube() throws InterruptedException {
        if (client instanceof DefaultGPTClient w) {
            w.getWebClient()
                    .get()
                    .uri("https://www.youtube.com")
                    .retrieve()
                    .bodyToMono(String.class)
                    .subscribe(System.out::println);
            Thread.currentThread().join();
        }
    }

    @Test
    public void testChat() throws InterruptedException {
        Completion completion = Completion.builder()
                .model(ModelTypeEnum.GPT_35_TURBO.name())
                .messages(List.of(Message.builder().content("你是谁").build()))
                .build();
        Mono<String> chat = client.chat(completion);
        chat.subscribe(str -> System.out.println("---------------------------" + str));
        Thread.currentThread().join();
    }

}
