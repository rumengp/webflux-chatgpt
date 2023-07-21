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
        return in(CollectionUtils.isNotEmpty(values), func, values);
    }

    public static <T, R> Criteria in(Boolean b, SFunction<T, R> func, Collection<R> values) {
        if (!b) {
            return Criteria.empty();
        }
        return Criteria.where(LambdaUtils.extract(func)).in(values);
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
