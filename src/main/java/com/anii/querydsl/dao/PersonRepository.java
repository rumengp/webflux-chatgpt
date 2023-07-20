package com.anii.querydsl.dao;

import com.anii.querydsl.entity.Person;
import com.infobip.spring.data.r2dbc.QuerydslR2dbcRepository;
import org.springframework.data.querydsl.ReactiveQuerydslPredicateExecutor;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;

public interface PersonRepository extends QuerydslR2dbcRepository<Person, Long> {
}
