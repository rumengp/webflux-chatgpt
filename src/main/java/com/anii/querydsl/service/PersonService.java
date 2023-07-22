package com.anii.querydsl.service;

import com.anii.querydsl.entity.Person;
import com.anii.querydsl.request.PersonPageRequest;
import org.springframework.data.domain.Page;
import reactor.core.publisher.Mono;

import java.util.List;

public interface PersonService{
    Mono<Page<Person>> pagePerson(PersonPageRequest personPageRequest);

    Mono<List<Person>> findByName(String name);
}
