package com.anii.query;

import com.anii.querydsl.WebfluxR2dbcQuerydslApplication;
import com.anii.querydsl.dao.PersonRepository;
import com.anii.querydsl.entity.Person;
import com.anii.querydsl.entity.QPerson;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import reactor.core.publisher.Flux;

@SpringBootTest(classes = WebfluxR2dbcQuerydslApplication.class)
public class PersonRepositoryTest {

    @Resource
    private PersonRepository personRepository;

    @Test
    public void testFindAll() {
        QPerson person = QPerson.person;

        Flux<Person> all = personRepository.findAll(person.id.in(1, 2, 3));
        all.subscribe(System.out::println);

    }
}
