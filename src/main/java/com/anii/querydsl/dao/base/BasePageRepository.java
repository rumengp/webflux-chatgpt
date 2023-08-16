package com.anii.querydsl.dao.base;

import com.infobip.spring.data.r2dbc.QuerydslR2dbcRepository;
import com.querydsl.sql.SQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.NoRepositoryBean;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@NoRepositoryBean
public interface BasePageRepository<T, ID> extends QuerydslR2dbcRepository<T, ID> {

    default <R> Mono<Page<R>> findAll(Function<SQLQuery<?>, SQLQuery<R>> builder, PageRequest pageRequest) {

        Mono<Long> count = this.query(builder).all().count();
        Function<SQLQuery<?>, SQLQuery<R>> records = builder.andThen(query ->
                query.limit(pageRequest.getPageSize()).offset(pageRequest.getOffset())
        );
        return this.query(records).all()
                .collectList()
                .zipWith(count, (list, c) -> new PageImpl<>(list, pageRequest, c));
    }
}
