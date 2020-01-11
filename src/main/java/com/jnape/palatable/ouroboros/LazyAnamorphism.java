package com.jnape.palatable.ouroboros;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functions.builtin.fn2.LazyRec;
import com.jnape.palatable.lambda.functor.Functor;
import com.jnape.palatable.lambda.functor.builtin.Lazy;

import static com.jnape.palatable.lambda.functor.builtin.Lazy.lazy;

/**
 * A LazyAnamorphism builds up structure using a {@link Coalgebra}.  Unlike an {@link Anamorphism}, a LazyAnamorphism may
 * recurse infinitely.
 *
 * @param <A>  the carrier type
 * @param <F>  the {@link Functor} witness
 * @param <FA> the {@link Functor} F&lt;{@link Lazy}&lt;A&gt;&gt;
 */
public class LazyAnamorphism<A,
        F extends Functor<?, F>,
        FA extends Functor<A, F>>
        implements Fn2<Coalgebra<A, FA>, A, FixLazy<F, ? extends Functor<? extends FixLazy<F, ?>, F>>> {
    private static final LazyAnamorphism<?, ?, ?> INSTANCE = new LazyAnamorphism<>();

    private LazyAnamorphism() {
    }

    @Override
    public FixLazy<F, ? extends Functor<? extends FixLazy<F, ?>, F>> checkedApply(Coalgebra<A, FA> coalgebra, A a) throws Throwable {
        return LazyRec.<A, FixLazy<F, ? extends Functor<? extends FixLazy<F, ?>, F>>>lazyRec(
                (f, anotherA) -> lazy(() -> FixLazy.<F, Functor<? extends FixLazy<F, ?>, F>>fixLazy(() -> coalgebra.apply(anotherA)
                        .fmap(f)
                        .fmap(Lazy::value))),
                a).value();
    }

    @SuppressWarnings("unchecked")
    public static <A,
            F extends Functor<?, F>,
            FA extends Functor<A, F>>
    LazyAnamorphism<A, F, FA> lazyAna() {
        return (LazyAnamorphism<A, F, FA>) INSTANCE;
    }

    public static <A,
            F extends Functor<?, F>,
            FA extends Functor<A, F>>
    Fn1<A, FixLazy<F, ? extends Functor<? extends FixLazy<F, ?>, F>>> lazyAna(Coalgebra<A, FA> coalgebra) {
        return LazyAnamorphism.<A, F, FA>lazyAna().apply(coalgebra);
    }

    public static <A,
            F extends Functor<?, F>,
            FA extends Functor<A, F>>
    FixLazy<F, ? extends Functor<? extends FixLazy<F, ?>, F>> lazyAna(Coalgebra<A, FA> coalgebra, A a) {
        return lazyAna(coalgebra).apply(a);
    }
}