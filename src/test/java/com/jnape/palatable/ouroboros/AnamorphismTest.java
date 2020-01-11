package com.jnape.palatable.ouroboros;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functions.builtin.fn2.Cons;
import com.jnape.palatable.lambda.functor.Functor;
import com.jnape.palatable.lambda.functor.builtin.Lazy;
import com.jnape.palatable.lambda.monad.transformer.builtin.MaybeT;
import org.junit.Test;

import java.util.ArrayList;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Head.head;
import static com.jnape.palatable.lambda.functions.builtin.fn2.ToCollection.toCollection;
import static com.jnape.palatable.lambda.functor.builtin.Lazy.lazy;
import static com.jnape.palatable.lambda.monad.transformer.builtin.MaybeT.maybeT;
import static com.jnape.palatable.ouroboros.Anamorphism.ana;
import static com.jnape.palatable.ouroboros.Catamorphism.cata;
import static com.jnape.palatable.ouroboros.Fix.fix;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;

public class AnamorphismTest {

    @Test
    public void fromMaybe() {
        Coalgebra<Integer, Maybe<Integer>> generateToSize = x -> x < 3 ? just(x + 1) : nothing();
        Functor<? extends Fix<Maybe<?>, ?>, Maybe<?>> unfix = ana(generateToSize, 0).unfix();

        assertEquals(unfix, just(fix(just(fix(just(fix(nothing())))))));
    }

    @Test
    public void collatzFromTuple() {
        Coalgebra<Integer, MaybeT<Tuple2<Integer, ?>, Integer>> coalgebra = x -> x == 1 ? maybeT(tuple(x, nothing()))
                : x % 2 == 0 ? maybeT(tuple(x, just(x / 2))) : maybeT(tuple(x, just(3 * x + 1)));

        assertEquals(ana(coalgebra, 4).unfix(),
                maybeT(tuple(4, just(fix(maybeT(tuple(2, just(fix(maybeT(tuple(1, nothing())))))))))));
    }

    @Test
    public void collatzToCata() {
        Coalgebra<Lazy<Iterable<Integer>>, MaybeT<Tuple2<Integer, ?>, Lazy<Iterable<Integer>>>> coalgebra = x -> {
            Integer value = head(x.value()).orElse(1);
            return value == 1 ? maybeT(tuple(value, nothing()))
                    : value % 2 == 0 ? maybeT(tuple(value, just(lazy(singletonList(value / 2))))) : maybeT(tuple(value, just(lazy(singletonList(3 * value + 1)))));
        };

        Algebra<MaybeT<Tuple2<Integer, ?>, Lazy<Iterable<Integer>>>, Lazy<Iterable<Integer>>> algebra = mtii -> {
            Tuple2<Integer, Maybe<Lazy<Iterable<Integer>>>> run = mtii.run();
            return lazy(Cons.cons(run._1(), run._2().match(constantly(emptyList()),
                    Lazy::value)));
        };

        assertEquals(toCollection(ArrayList::new, cata(algebra, ana(coalgebra, lazy(singletonList(3)))).value()),
                asList(3, 10, 5, 16, 8, 4, 2, 1));
    }

}