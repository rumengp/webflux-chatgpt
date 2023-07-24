package com.anii.querydsl.service;

import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 模仿mybatis-plus生命一个基础的service
 * @param <T> 实体类
 * @param <ID> id类型
 */
public interface IService<T, ID> {

    <S extends T> Mono<S> save(S entity);

    <S extends T> Mono<List<S>> saveAll(Iterable<S> entities);

    <S extends T> Mono<List<S>> saveAll(Publisher<S> entities);

    Mono<T> findById(ID id);

    Mono<T> findById(Publisher<ID> ids);

    Mono<Boolean> existsById(ID id);

    Mono<Boolean> existsById(Publisher<ID> id);

    Mono<List<T>> findAll();

    Mono<List<T>> findAllById(Iterable<ID> ids);

    Mono<List<T>> findAllById(Publisher<ID> ids);

    Mono<Long> count();

    Mono<Void> deleteById(ID id);

    Mono<Void> deleteById(Publisher<ID> ids);

    Mono<Void> delete(T entity);

    Mono<Void> deleteAllById(Iterable<? extends ID> ids);

    Mono<Void> deleteAll(Iterable<? extends T> entities);

    Mono<Void> deleteAll(Publisher<? extends T> entities);
}
