package com.jnape.palatable.ouroboros;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functor.builtin.Lazy;
import org.junit.Test;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static com.jnape.palatable.lambda.functions.builtin.fn3.Times.times;
import static com.jnape.palatable.lambda.functor.builtin.Lazy.lazy;
import static com.jnape.palatable.ouroboros.Catamorphism.cata;
import static com.jnape.palatable.ouroboros.Fix.fix;
import static org.junit.Assert.assertEquals;

public class CatamorphismTest {

    private static final Algebra<Maybe<Lazy<Integer>>, Lazy<Integer>> size = maybeLazyX -> maybeLazyX
            .match(constantly(lazy(0)), lazyX -> lazyX.fmap(x -> x + 1));

    @Test
    public void zero() {
        assertEquals(lazy(0), cata(size, fix(nothing())));
    }

    @Test
    public void one() {
        assertEquals(lazy(1), cata(size, fix(just(fix(nothing())))));
    }

    @Test
    public void many() {
        int n = 100_000;
        assertEquals(lazy(n), cata(size, fix(times(n, m -> just(fix(m)), nothing()))));
    }
}