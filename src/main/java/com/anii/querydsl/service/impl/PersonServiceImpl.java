package com.anii.querydsl.service.impl;

import com.anii.querydsl.dao.PersonRepository;
import com.anii.querydsl.entity.Person;
import com.anii.querydsl.entity.QPerson;
import com.anii.querydsl.request.PersonPageRequest;
import com.anii.querydsl.service.PersonService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl extends ServiceImpl<PersonRepository, Person, Long> implements PersonService {

    private static QPerson person = QPerson.person;

    @Override
    public Mono<Page<Person>> pagePerson(PersonPageRequest personPageRequest) {

        PageRequest of = PageRequest.of(personPageRequest.getPageNo(), personPageRequest.getPageSize());
        Mono<Page<Person>> page = repository.findAll(query ->
                        query.select(repository.entityProjection())
                                .from(person)
                                .where(person.name.like(personPageRequest.getName() + "%"))
                                .orderBy(person.id.asc())
                , of);
        return page;
//        return personRepository.findAllBy(like, of)
//                .collectList()
//                .zipWith(personRepository.count(like), (people, count) -> new PageImpl<>(people, of, count));
    }

    @Override
    public Mono<List<Person>> findByName(String name) {
        return repository.query(query ->
                        query.select(repository.entityProjection())
                                .from(person)
                                .where(person.name.like(name))
                )
                .all().collectList();
    }

    @Data
    public class NameOnly {
        private String name;
    }
}
