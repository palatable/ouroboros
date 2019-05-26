package com.jnape.palatable.ouroboros;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functions.builtin.fn2.LazyRec;
import com.jnape.palatable.lambda.functor.Functor;
import com.jnape.palatable.lambda.functor.builtin.Lazy;

/**
 * A {@link Catamorphism} uses an {@link Algebra} to tear down structure:
 * <pre>
 * {@code
 * Algebra<Maybe<Lazy<Integer>>, Lazy<Integer>> size = maybeLazyX ->
 *     maybeLazyX.match(constantly(lazy(0)), lazyX -> lazyX.fmap(x -> x + 1));
 * cata(size, fix(times(10_000, m -> just(fix(m)), nothing()))).value(); // 10_000
 * }
 * </pre>
 *
 * @param <A>  the carrier type
 * @param <F>  the {@link Functor} witness
 * @param <FA> the {@link Functor} F&lt;{@link Lazy}&lt;A&gt;&gt;
 */
public final class Catamorphism<A, F extends Functor<?, F>, FA extends Functor<Lazy<A>, F>> implements
        Fn2<Algebra<FA, Lazy<A>>, Fix<F, ? extends Functor<? extends Fix<F, ?>, F>>, Lazy<A>> {

    private static final Catamorphism<?, ?, ?> INSTANCE = new Catamorphism<>();

    private Catamorphism() {
    }

    @Override
    public Lazy<A> checkedApply(Algebra<FA, Lazy<A>> algebra, Fix<F, ? extends Functor<? extends Fix<F, ?>, F>> fixF) {
        return LazyRec.<Fix<F, ? extends Functor<?, F>>, A>lazyRec((f, fixed) -> {
            Functor<? extends Fix<F, ?>, F> unfix = fixed.unfix();
            return algebra.apply(unfix.<Lazy<A>>fmap(f).coerce());
        }, fixF);
    }

    @SuppressWarnings("unchecked")
    public static <A, F extends Functor<?, F>, FA extends Functor<Lazy<A>, F>> Catamorphism<A, F, FA> cata() {
        return (Catamorphism<A, F, FA>) INSTANCE;
    }

    public static <A, F extends Functor<?, F>, FA extends Functor<Lazy<A>, F>>
    Fn1<Fix<F, ? extends Functor<? extends Fix<F, ?>, F>>, Lazy<A>> cata(Algebra<FA, Lazy<A>> algebra) {
        return Catamorphism.<A, F, FA>cata().apply(algebra);
    }

    public static <A, F extends Functor<?, F>, FA extends Functor<Lazy<A>, F>> Lazy<A> cata(
            Algebra<FA, Lazy<A>> algebra,
            Fix<F, ? extends Functor<? extends Fix<F, ?>, F>> fixF) {
        return cata(algebra).apply(fixF);
    }
}
