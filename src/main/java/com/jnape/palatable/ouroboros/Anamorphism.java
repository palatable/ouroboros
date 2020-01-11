package com.jnape.palatable.ouroboros;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functor.Functor;
import com.jnape.palatable.lambda.functor.builtin.Lazy;

import static com.jnape.palatable.ouroboros.Fix.fix;

/**
 * An {@link Anamorphism} uses a {@link Coalgebra} to build up structure:
 * <pre>
 * {@code
 * Coalgebra<Integer, Maybe<Integer>> generateToSize = x -> x < 3 ? just(x + 1) : nothing();
 * ana(generateToSize, 0).unfix(); // Just fix(Just fix(Just fix(Nothing)))
 * }
 * </pre>
 *
 * @param <A>  the carrier type
 * @param <F>  the {@link Functor} witness
 * @param <FA> the {@link Functor} F&lt;{@link Lazy}&lt;A&gt;&gt;
 */
public final class Anamorphism<A,
        F extends Functor<?, F>,
        FA extends Functor<A, F>>
        implements Fn2<Coalgebra<A, FA>, A, Fix<F, ? extends Functor<? extends Fix<F, ?>, F>>> {
    private static final Anamorphism<?, ?, ?> INSTANCE = new Anamorphism<>();

    private Anamorphism() {
    }

    @Override
    public Fix<F, ? extends Functor<? extends Fix<F, ?>, F>> checkedApply(Coalgebra<A, FA> coalgebra, A a) throws Throwable {
        return fix(coalgebra.apply(a).fmap(ana(coalgebra)));
    }

    @SuppressWarnings("unchecked")
    public static <A,
            F extends Functor<?, F>,
            FA extends Functor<A, F>> Anamorphism<A, F, FA> ana() {
        return (Anamorphism<A, F, FA>) INSTANCE;
    }

    public static <A,
            F extends Functor<?, F>,
            FA extends Functor<A, F>> Fn1<A, Fix<F, ? extends Functor<? extends Fix<F, ?>, F>>> ana(Coalgebra<A, FA> coalgebra) {
        return Anamorphism.<A, F, FA>ana().apply(coalgebra);
    }

    public static <A,
            F extends Functor<?, F>,
            FA extends Functor<A, F>> Fix<F, ? extends Functor<? extends Fix<F, ?>, F>> ana(Coalgebra<A, FA> coalgebra,
                                                                                            A a) {
        return ana(coalgebra).apply(a);
    }
}
