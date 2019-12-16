package com.jnape.palatable.ouroboros;

import com.jnape.palatable.lambda.functions.Fn0;
import com.jnape.palatable.lambda.functor.Functor;
import com.jnape.palatable.lambda.functor.builtin.Lazy;

import java.util.Objects;

import static com.jnape.palatable.lambda.functor.builtin.Lazy.lazy;

public interface FixLazy<F extends Functor<?, F>, Unfixed extends Functor<? extends FixLazy<F, ?>, F>> {

    Lazy<Unfixed> unfixLazy();

    static <F extends Functor<?, F>, Unfixed extends Functor<? extends FixLazy<F, ?>, F>> FixLazy<F, ? extends Unfixed> fixLazy(
            Fn0<Unfixed> unfixed) {
        return new FixLazy<F, Unfixed>() {
            @Override
            public Lazy<Unfixed> unfixLazy() {
                return lazy(unfixed);
            }

            @Override
            public boolean equals(Object obj) {
                return (obj instanceof FixLazy) && Objects.equals(unfixed, ((FixLazy) obj).unfixLazy());
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
