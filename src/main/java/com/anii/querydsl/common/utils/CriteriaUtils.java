package com.anii.querydsl.common.utils;

import com.anii.querydsl.common.utils.function.LambdaUtils;
import com.anii.querydsl.common.utils.function.SFunction;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.relational.core.query.Criteria;

import java.util.Collection;
import java.util.Objects;

public class CriteriaUtils {

    public static <T, R> Criteria in(SFunction<T, R> func, Collection<R> values) {
        return in(predicate(values), func, values);
    }

    public static <T, R> Criteria in(Boolean b, SFunction<T, R> func, Collection<R> values) {
        return b ? Criteria.where(LambdaUtils.extract(func)).in(values) : Criteria.empty();
    }

    public static <T, R> Criteria is(SFunction<T, R> func, R value) {
        return is(predicate(value), func, value);
    }

    public static <T, R> Criteria is(Boolean b, SFunction<T, R> func, R value) {
        return b ? Criteria.where(LambdaUtils.extract(func)).is(value) : Criteria.empty();
    }

    public static <T> Criteria likeRight(SFunction<T, String> func, String value) {
        return likeRight(predicate(value), func, value);
    }

    public static <T> Criteria likeRight(Boolean b, SFunction<T, String> func, String value) {
        return b ? Criteria.where(LambdaUtils.extract(func)).like("%" + value) : Criteria.empty();
    }

    public static <T> Criteria like(SFunction<T, String> func, String value) {
        return like(predicate(value), func, value);
    }

    public static <T> Criteria like(Boolean b, SFunction<T, String> func, String value) {
        return b ? Criteria.where(LambdaUtils.extract(func)).like("%" + value + "%") : Criteria.empty();
    }

    public static <T, R> Criteria between(SFunction<T, R> func, R left, R right) {
        return between(predicate(left) && predicate(right), func, left, right);
    }

    public static <T, R> Criteria between(Boolean b, SFunction<T, R> func, R left, R right) {
        return b ? Criteria.where(LambdaUtils.extract(func)).between(left, right) : Criteria.empty();
    }

    public static <T, R> Criteria gt(SFunction<T, R> func, R value) {
        return gt(predicate(value), func, value);
    }

    public static <T, R> Criteria gt(Boolean b, SFunction<T, R> func, R value) {
        return b ? Criteria.where(LambdaUtils.extract(func)).greaterThan(value) : Criteria.empty();
    }

    public static <T, R> Criteria lt(SFunction<T, R> func, R value) {
        return lt(predicate(value), func, value);
    }

    public static <T, R> Criteria lt(Boolean b, SFunction<T, R> func, R value) {
        return b ? Criteria.where(LambdaUtils.extract(func)).lessThan(value) : Criteria.empty();
    }

    public static <T, R> Criteria ge(SFunction<T, R> func, R value) {
        return ge(predicate(value), func, value);
    }

    public static <T, R> Criteria ge(Boolean b, SFunction<T, R> func, R value) {
        return b ? Criteria.where(LambdaUtils.extract(func)).greaterThanOrEquals(value) : Criteria.empty();
    }

    public static <T, R> Criteria le(SFunction<T, R> func, R value) {
        return le(predicate(value), func, value);
    }

    public static <T, R> Criteria le(Boolean b, SFunction<T, R> func, R value) {
        return b ? Criteria.where(LambdaUtils.extract(func)).lessThanOrEquals(value) : Criteria.empty();
    }

    public static <T, R> Criteria not(SFunction<T, R> func, R value) {
        return not(predicate(value), func, value);
    }

    public static <T, R> Criteria not(Boolean b, SFunction<T, R> func, R value) {
        return b ? Criteria.where(LambdaUtils.extract(func)).not(value) : Criteria.empty();
    }

    public static <T, R> Criteria notIn(SFunction<T, R> func, Collection<R> value) {
        return notIn(predicate(value), func, value);
    }

    public static <T, R> Criteria notIn(Boolean b, SFunction<T, R> func, Collection<R> value) {
        return b ? Criteria.where(LambdaUtils.extract(func)).notIn(value) : Criteria.empty();
    }

    private static Boolean predicate(Object obj) {
        if (obj instanceof String str) {
            return StringUtils.isNotEmpty(str);
        }

        if (obj instanceof Collection<?> coll) {
            return CollectionUtils.isNotEmpty(coll);
        }

        if (obj instanceof Number num) {
            return num.doubleValue() > 0.0;
        }

        return Objects.nonNull(obj);
    }

}
