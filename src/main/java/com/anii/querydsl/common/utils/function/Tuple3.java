package com.anii.querydsl.common.utils.function;

import lombok.NonNull;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Prm
 */
public class Tuple3<T1, T2, T3> {

    private T1 first;

    private Supplier<T1> firstSupplier;

    private T2 second;

    private Supplier<T2> secondSupplier;

    private T3 third;

    private Supplier<T3> thirdSupplier;

    private Tuple3() {

    }

    private Tuple3(@NonNull T1 first, @NonNull T2 second, @NonNull T3 third) {
        this.first = Objects.requireNonNull(first, "first");
        this.second = Objects.requireNonNull(second, "second");
        this.third = Objects.requireNonNull(third, "third");
    }

    private Tuple3(@NonNull Supplier<T1> firstSupplier, @NonNull Supplier<T2> secondSupplier, @NonNull Supplier<T3> thirdSupplier) {
        this.firstSupplier = Objects.requireNonNull(firstSupplier, "firstSupplier");
        this.secondSupplier = Objects.requireNonNull(secondSupplier, "secondSupplier");
        this.thirdSupplier = Objects.requireNonNull(thirdSupplier, "thirdSupplier");
    }

    public static <T1, T2, T3>
    Tuple3<T1, T2, T3> of(@NonNull T1 first, @NonNull T2 second, @NonNull T3 third) {
        return new Tuple3(first, second, third);
    }

    public static <T1, T2, T3>
    Tuple3<T1, T2, T3> of(@NonNull Supplier<T1> firstSupplier, @NonNull Supplier<T2> secondSupplier, @NonNull Supplier<T3> thirdSupplier) {
        return Tuple3.of(firstSupplier, secondSupplier, thirdSupplier, Boolean.FALSE);
    }

    public static <T1, T2, T3>
    Tuple3<T1, T2, T3> of(@NonNull Supplier<T1> firstSupplier, @NonNull Supplier<T2> secondSupplier, @NonNull Supplier<T3> thirdSupplier, Boolean lazy) {
        if (lazy) {
            return new Tuple3(firstSupplier, secondSupplier, thirdSupplier);
        }
        return new Tuple3(firstSupplier.get(), secondSupplier.get(), thirdSupplier.get());
    }

    public T1 getFirst() {
        return first = Optional.ofNullable(first)
                .orElseGet(firstSupplier);
    }

    public T2 getSecond() {
        return second = Optional.ofNullable(second)
                .orElseGet(secondSupplier);
    }

    public T3 getThird() {
        return third = Optional.ofNullable(third)
                .orElseGet(thirdSupplier);
    }

    @Override
    public String toString() {
        return Stream.of(first, second, third)
                .map(Object::toString)
                .collect(Collectors.joining(",", "[", "]"));
    }
}
