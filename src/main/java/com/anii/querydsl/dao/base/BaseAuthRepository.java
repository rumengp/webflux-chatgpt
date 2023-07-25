package com.anii.querydsl.dao.base;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;
import reactor.core.publisher.Mono;

@NoRepositoryBean
public interface BaseAuthRepository<T, ID> extends Repository<T, ID> {

    Mono<Void> deleteByIdAndUsername(Long id, String username);

    Mono<Void> findByIdAndUsername(Long id, String username);

}
