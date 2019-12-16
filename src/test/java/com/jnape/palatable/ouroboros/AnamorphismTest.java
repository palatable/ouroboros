package com.jnape.palatable.ouroboros;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functor.Functor;
import com.jnape.palatable.lambda.monad.transformer.builtin.MaybeT;
import org.junit.Test;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.monad.transformer.builtin.MaybeT.maybeT;
import static com.jnape.palatable.ouroboros.Anamorphism.ana;
import static com.jnape.palatable.ouroboros.Fix.fix;
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

}