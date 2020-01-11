package com.jnape.palatable.ouroboros;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functor.Functor;
import org.junit.Test;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.ouroboros.LazyAnamorphism.lazyAna;

public class LazyAnamorphismTest {
    @Test
    public void doesntStackOverflow() {
        Coalgebra<Integer, Tuple2<Integer, Integer>> coalgebra = x -> x % 2 == 0 ? tuple(x, x / 2) : tuple(x, 3 * x + 1);

        FixLazy<Tuple2<Integer, ?>, ? extends Functor<? extends FixLazy<Tuple2<Integer, ?>, ?>, Tuple2<Integer, ?>>> ana = lazyAna(coalgebra, 15);
        ana.unfixLazy();
    }
}