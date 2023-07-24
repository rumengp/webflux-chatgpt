package com.anii.querydsl.service.impl;

import com.anii.querydsl.service.IService;
import jakarta.annotation.Resource;
import org.reactivestreams.Publisher;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.List;

public abstract class ServiceImpl<R extends ReactiveCrudRepository<T, ID>, T, ID> implements IService<T, ID> {

    @Resource
    protected R repository;

    @Override
    public <S extends T> Mono<S> save(S entity) {
        return repository.save(entity);
    }

    @Override
    public <S extends T> Mono<List<S>> saveAll(Iterable<S> entities) {
        return repository.saveAll(entities).collectList();
    }

    @Override
    public <S extends T> Mono<List<S>> saveAll(Publisher<S> entities) {
        return repository.saveAll(entities).collectList();
    }

    @Override
    public Mono<T> findById(ID id) {
        return this.repository.findById(id);
    }

    @Override
    public Mono<T> findById(Publisher<ID> ids) {
        return this.repository.findById(ids);
    }

    @Override
    public Mono<Boolean> existsById(ID id) {
        return repository.existsById(id);
    }

    @Override
    public Mono<Boolean> existsById(Publisher<ID> id) {
        return repository.existsById(id);
    }

    @Override
    public Mono<List<T>> findAll() {
        return repository.findAll().collectList();
    }

    @Override
    public Mono<List<T>> findAllById(Iterable<ID> ids) {
        return repository.findAllById(ids).collectList();
    }

    @Override
    public Mono<List<T>> findAllById(Publisher<ID> ids) {
        return repository.findAllById(ids).collectList();
    }

    @Override
    public Mono<Long> count() {
        return repository.count();
    }

    @Override
    public Mono<Void> deleteById(ID id) {
        return repository.deleteById(id);
    }

    @Override
    public Mono<Void> deleteById(Publisher<ID> ids) {
        return repository.deleteById(ids);
    }

    @Override
    public Mono<Void> delete(T entity) {
        return repository.delete(entity);
    }

    @Override
    public Mono<Void> deleteAllById(Iterable<? extends ID> ids) {
        return repository.deleteAllById(ids);
    }

    @Override
    public Mono<Void> deleteAll(Iterable<? extends T> entities) {
        return repository.deleteAll(entities);
    }

    @Override
    public Mono<Void> deleteAll(Publisher<? extends T> entities) {
        return repository.deleteAll(entities);
    }
}
