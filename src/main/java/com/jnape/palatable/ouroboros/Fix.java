package com.jnape.palatable.ouroboros;

import com.jnape.palatable.lambda.functor.Functor;

import java.util.Objects;

/**
 * A type-level encoding of the least fixed point of a given functor; that is, given a
 * <code>{@link Functor}&lt;X, F&gt;</code> <code>f</code> and a value <code>x</code> of type <code>X</code>,
 * <code>x</code> is the least fixed point of <code>f</code> if, and only if, for all functions <code>fn</code>,
 * <code>f.fmap(fn) == f</code>.
 * <p>
 * This encoding is foundational to the recursion schemes as it provides a generic, arbitrarily deeply recursive type
 * signature corresponding to inductive and co-inductive functor.
 * <p>
 * For more information, read about
 * <a href="https://www.schoolofhaskell.com/user/bartosz/understanding-algebras" target="_top">Fix</a>.
 *
 * @param <F>       the {@link Functor} witness
 * @param <Unfixed> the type corresponding to the unfixed {@link Functor}
 */
public interface Fix<F extends Functor<?, F>, Unfixed extends Functor<? extends Fix<F, ?>, F>> {

    /**
     * Unfix the currently fixed {@link Functor}.
     *
     * @return the unfixed {@link Functor}
     */
    Unfixed unfix();

    /**
     * Fix a {@link Functor} <code>f</code>.
     *
     * @param unfixed   the unfixed functor
     * @param <F>       the {@link Functor} witness
     * @param <Unfixed> the type corresponding to the unfixed {@link Functor}
     * @return the fixed-point of the functor
     */
    static <F extends Functor<?, F>, Unfixed extends Functor<? extends Fix<F, ?>, F>> Fix<F, ? extends Unfixed> fix(
            Unfixed unfixed) {
        return new Fix<F, Unfixed>() {
            @Override
            public Unfixed unfix() {
                return unfixed;
            }

            @Override
            public boolean equals(Object obj) {
                return (obj instanceof Fix) && Objects.equals(unfixed, ((Fix) obj).unfix());
            }

            @Override
            public int hashCode() {
                return 31 * Objects.hashCode(unfixed);
            }

            @Override
            public String toString() {
                return "fix(" + unfixed + ")";
            }
        };
    }
}
