package com.jnape.palatable.ouroboros;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functor.Functor;

/**
 * A <code>{@link Coalgebra}&lt;A, F&gt;</code> for some carrier type <code>A</code> and some {@link Functor}
 * <code>F</code> is a morphism <code>A -&gt; F&lt;A&gt;</code>.
 *
 * @param <A> the carrier type
 * @param <F> the {@link Functor} witness
 */
@FunctionalInterface
public interface Coalgebra<A, F extends Functor<A, ?>> extends Fn1<A, F> {
}
