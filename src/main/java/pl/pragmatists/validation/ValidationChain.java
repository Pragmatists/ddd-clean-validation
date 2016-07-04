package pl.pragmatists.validation;

import java.util.function.Consumer;
import java.util.function.Function;

public class ValidationChain {

    public static <S> ValidationStep<S, S> of(Class<S> clazz) {
        return new ValidationStep<>(null, Function.identity());
    }

    public static <S, Z> ValidationStep<Tuple2<S, Z>, S> of(Class<S> clazz, Class<Z> clazz2) {
        return new ValidationStep<>(null, Function.identity());
    }

    public static class ValidationStep<S, C> {

        private final ValidationStep<S, ?> prev;
        private final Function<?, C> action;

        private <X> ValidationStep(ValidationStep<S, X> prev, Function<X, C> action) {
            this.prev = prev;
            this.action = action;
        }

        private <C> C doTest(S value) {
            Object result = prev == null ? value : prev.doTest(value);
            Function rawAction = action;
            return (C) rawAction.apply(result);
        }

        public void test(S value) {
            try {
                doTest(value);
            } catch (ValidationChainFailed validationChainFailed) {

            }
        }

        public ValidationStep<S, C> check(Consumer<C> action) {
            Function f = new Function<C, C>() {
                @Override
                public C apply(C c) {
                    action.accept(c);
                    return c;
                }
            };
            return new ValidationStep<>(this, f);
        }

        public <X> ValidationStep<S, X> then(Function<C, X> action) {
            return new ValidationStep<>(this, action);
        }
    }

    private interface Tuple {
    }

    private static class Tuple2<S, Z> implements Tuple {
    }

    private static class Tuple1<S> implements Tuple {
    }

}
