package com.anii.querydsl;

import com.anii.querydsl.common.utils.JSONUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Test;

import java.util.List;

public class JSONUtilsTest {

    @Test
    void parseObjectTest() {
        String personJson = """
                {"name":{"first":"anii","last":"123"},"age":18}
                """;
        String personJsons = """
                [{"name":{"first":"anii","last":"123"},"age":18},{"name":{"first":"anii","last":"123"},"age":18},{"name":{"first":"anii","last":"123"},"age":18}]
                """;
        Person<String> person = JSONUtils.parseObject(personJson, new TypeReference<>() {
        });
        List<Person> people = JSONUtils.parseList(personJsons, Person.class);
        System.out.println(people);
        System.out.println(person);
    }

    record Name<T>(T first, T last) {

    }

    record Person<T>(Name<T> name, Integer age) {
    }
}
