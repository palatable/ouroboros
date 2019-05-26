package com.jnape.palatable.ouroboros;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functor.Functor;

/**
 * An <code>{@link Algebra}&lt;F, A&gt;</code> for some {@link Functor} <code>F</code> and some carrier type
 * <code>A</code> is a morphism <code>F&lt;A&gt; -&gt; A</code>.
 *
 * @param <F> the {@link Functor} witness
 * @param <A> the carrier type
 */
@FunctionalInterface
public interface Algebra<F extends Functor<A, ?>, A> extends Fn1<F, A> {
}
