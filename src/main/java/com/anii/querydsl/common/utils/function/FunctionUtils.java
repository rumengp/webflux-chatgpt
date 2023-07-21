package com.anii.querydsl.common.utils.function;

import lombok.NonNull;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @since 3.9.0
 **/
public class FunctionUtils {

    public static <T> Function<List<T>, List<T>> subList(T item) {
        return list -> {
            if (list.contains(item)) {
                int i = list.indexOf(item);
                return list.subList(0, i);
            }
            return Collections.emptyList();
        };
    }

    public static Function<Set<String>, String> join() {
        return items -> String.join(",", items);
    }

    public static <T> BinaryOperator<T> mergeAcceptLast() {
        return (a, b) -> b;
    }

    public static <T> BinaryOperator<T> mergeAcceptFirst() {
        return (a, b) -> a;
    }

    /**
     * 将字符串列表解析为json中的array
     */
    public static Collector<Object, ?, String> jsonJoining() {
        return Collectors.mapping(str -> {
                    if (str instanceof String) {
                        return "'" + str + "'";
                    }
                    return str.toString();
                },
                Collectors.joining(",", "[", "]"));
    }

    public static <T, R> Predicate<T> mapperAndPre(Function<T, R> mapper, Predicate<R> predicate) {
        return input -> predicate.test(mapper.apply(input));
    }

    public static <T, R> Predicate<T> mapperAndEqual(Function<T, R> mapper, R refObj) {
        return input ->
                Objects.equals(mapper.apply(input), refObj);
    }

    public static <T> List<Tuple2<T, T>> zip(@NonNull List<T> l1, @NonNull List<T> l2) {
        if (!Objects.equals(l1.size(), l2.size())) {
            throw new IllegalArgumentException(String.format("l1 size %d not equal l2 size %d", l1.size(), l2.size()));
        }
        ArrayList<Tuple2<T, T>> tuple2s = new ArrayList<>();
        for (int i = 0; i < l1.size(); i++) {
            tuple2s.add(Tuple2.of(l1.get(i), l2.get(i)));
        }

        return tuple2s;
    }

    public static <T> Predicate<T> and(@NonNull Predicate<T> p1, Predicate<T> p2) {
        return input -> Boolean.logicalAnd(p1.test(input), p2.test(input));
    }

    public static <T> Predicate<T> or(@NonNull Predicate<T> p1, Predicate<T> p2) {
        return input -> Boolean.logicalOr(p1.test(input), p2.test(input));
    }

    /**
     * copy from jdk11
     */
    public static <T> Predicate<T> not(Predicate<T> target) {
        Objects.requireNonNull(target);
        return target.negate();
    }

    public static <T, R> Function<T, R> functionWithDefault(Function<T, R> function, R def) {
        return input -> {
            R out = function.apply(input);
            if (Objects.isNull(out)) {
                return def;
            }
            return out;
        };
    }

    public static ChainEnum chains() {
        return ChainEnum.INSTANCE;
    }

    public static <T> Supplier<T> asSupplier(T obj) {
        return () -> obj;
    }

    public static <R> Function<R, R> asFunction(Consumer<R> consumer) {
        return input -> {
            consumer.accept(input);
            return input;
        };
    }

    public static <T, A, R> Collector<T, A, R> collectingAndThen(
            Collector<T, A, R> downstream,
            Consumer<R> consumer) {
        return Collectors.collectingAndThen(downstream, r -> {
            consumer.accept(r);
            return r;
        });
    }

    /**
     * 需要同时转换成2不同的对象时，可以通过2个Mapper进行转化，并返回Tuple2
     */
    public static <T, R, R2> Collector<T, ?, Tuple2<List<R>, List<R2>>> mappingMulti(
            Function<T, R> mapper,
            Function<T, R2> mapper2) {
        return Collector.of(
                () -> Tuple2.of(ArrayList::new, ArrayList::new),
                (l, r) -> {
                    l.getFirst().add(mapper.apply(r));
                    l.getSecond().add(mapper2.apply(r));
                },
                (t1, t2) -> {
                    t1.getFirst().addAll(t2.getFirst());
                    t1.getSecond().addAll(t2.getSecond());
                    return t1;
                }
        );
    }

    /**
     * 需要同时转换成3不同的对象时，可以通过3个Mapper进行转化，并返回Tuple3
     */
    public static <T, R, R2, R3> Collector<T, ?, Tuple3<List<R>, List<R2>, List<R3>>> mappingMulti(
            Function<T, R> mapper,
            Function<T, R2> mapper2,
            Function<T, R3> mapper3) {
        return Collector.of(
                () -> Tuple3.of(ArrayList::new, ArrayList::new, ArrayList::new),
                (l, r) -> {
                    l.getFirst().add(mapper.apply(r));
                    l.getSecond().add(mapper2.apply(r));
                    l.getThird().add(mapper3.apply(r));
                },
                (l, r) -> {
                    l.getFirst().addAll(r.getFirst());
                    l.getSecond().addAll(r.getSecond());
                    l.getThird().addAll(r.getThird());
                    return l;
                }
        );
    }

    // 链式调用帮助类
    public enum ChainEnum {
        INSTANCE;

        public <T> ChainEnum andThen(Consumer<T> consumer, Supplier<T> supplier) {
            consumer.accept(supplier.get());
            return this;
        }

        public <T> ChainEnum andThenVal(Consumer<T> consumer, T val) {
            consumer.accept(val);
            return this;
        }

        public <T, R> ChainEnum andThen(Consumer<R> consumer, Supplier<T> supplier, Function<T, R> mapper) {
            consumer.accept(mapper.apply(supplier.get()));
            return this;
        }

    }

}
