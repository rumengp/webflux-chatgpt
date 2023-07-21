package com.anii.querydsl.common.utils.function;

import lombok.NonNull;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Prm
 * helper class
 */
public class Tuple2<T, U> {

    private T first;

    private Supplier<T> firstSupplier;

    private U second;

    private Supplier<U> secondSupplier;

    private Tuple2() {

    }

    private Tuple2(@NonNull T first, @NonNull U second) {
        this.first = Objects.requireNonNull(first, "first");
        this.second = Objects.requireNonNull(second, "second");
    }

    private Tuple2(@NonNull Supplier<T> firstSupplier, @NonNull Supplier<U> secondSupplier) {
        this.firstSupplier = Objects.requireNonNull(firstSupplier, "firstSupplier");
        this.secondSupplier = Objects.requireNonNull(secondSupplier, "secondSupplier");
    }

    public static <T, U>
    Tuple2<T, U> of(@NonNull T first, @NonNull U second) {
        return new Tuple2(first, second);
    }

    public static <T, U>
    Tuple2<T, U> of(@NonNull Supplier<T> firstSupplier, @NonNull Supplier<U> secondSupplier) {
        return Tuple2.of(firstSupplier, secondSupplier, Boolean.FALSE);
    }

    public static <T, U>
    Tuple2<T, U> of(@NonNull Supplier<T> firstSupplier, @NonNull Supplier<U> secondSupplier, Boolean lazy) {
        if (lazy) {
            return new Tuple2(firstSupplier, secondSupplier);
        }
        return new Tuple2(firstSupplier.get(), secondSupplier.get());
    }

    public T getFirst() {
        return first = Optional.ofNullable(first)
                .orElseGet(firstSupplier);
    }

    public U getSecond() {
        return second = Optional.ofNullable(second)
                .orElseGet(secondSupplier);
    }

    @Override
    public String toString() {
        return Stream.of(getFirst(), getSecond())
                .map(Object::toString)
                .collect(Collectors.joining(",", "[", "]"));
    }
}
