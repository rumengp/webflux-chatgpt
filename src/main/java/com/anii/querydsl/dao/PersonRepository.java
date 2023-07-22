package com.anii.querydsl.dao;

import com.anii.querydsl.entity.Person;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.PageRequest;
import reactor.core.publisher.Flux;

public interface PersonRepository extends BasePageRepository<Person, Long> {

    Flux<Person> findAllBy(Predicate predicate, PageRequest pageRequest, OrderSpecifier... order);
}
