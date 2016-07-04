package pl.pragmatists.users.ui;

import org.assertj.core.api.StrictAssertions;
import org.junit.Test;
import pl.pragmatists.validation.AccumulatingValidationListener;
import pl.pragmatists.validation.ExceptionThrowingListener;
import pl.pragmatists.validation.ValidationChain;
import pl.pragmatists.users.domain.Email;
import pl.pragmatists.validation.ValidationListener;

import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.StrictAssertions.failBecauseExceptionWasNotThrown;

public class ValidationChainTest {

    static class Adult {

        private final Validator validator = new Validator(new ExceptionThrowingListener());

        private final Integer value;

        private Adult(Integer value) {
            this.value = value;
        }

        static Adult of(Integer value) {
            Adult adult = new Adult(value);
            adult.validate();
            return adult;
        }

        private void validate() {
            validator.validate(value);
        }

        public static void test(Adult adult) {

        }

        public static class Validator {
            private final ValidationListener listener;

            public Validator(ValidationListener listener) {
                this.listener = listener;
            }

            public void validate(Integer value) {
                if (value == null || value < 18) {
                    listener.reject(new KidIsInDaHouse());
                }
            }
        }

        static class KidIsInDaHouse extends RuntimeException {
            public KidIsInDaHouse() {
                super("No no no.");
            }
        }
    }

    @Test
    public void failsWhenAgeIsInvalid() {
        try {
            Adult.of(-1);
            failBecauseExceptionWasNotThrown(Adult.KidIsInDaHouse.class);
        } catch (Exception e) {
            StrictAssertions.assertThat(e).isInstanceOf(Adult.KidIsInDaHouse.class);
        }
    }

    @Test
    public void notFailsWhenAgeIsValid() {
        Adult one = Adult.of(23);

        assertThat(one).isNotNull();
    }

    @Test
    public void accumulatesDomainValidation() throws Exception {
        AccumulatingValidationListener errors = AccumulatingValidationListener.create();
        Adult.Validator adult = new Adult.Validator(errors.forPath("age"));

        ValidationChain.of(Integer.class)
                .check(adult::validate)
                .test(3);

        assertThat(errors.list()).containsOnly(new AccumulatingValidationListener.Error("age", "No no no."));
    }

    @Test
    public void accumulatesPrevViolations() throws Exception {
        AccumulatingValidationListener errors = AccumulatingValidationListener.create();
        LessThan lessThan24 = new LessThan(24, errors.forPath("age"));
        Adult.Validator adult = new Adult.Validator(errors.forPath("age"));
        NoopAdultValidator noopAdultValidator = new NoopAdultValidator(errors.forPath("adult"));

        ValidationChain.of(Integer.class)
                .check(lessThan24::validate)
                .check(adult::validate)
                .then(Adult::of)
                .check(noopAdultValidator::validate)
                .test(20);

        assertThat(errors.list()).containsOnly(new AccumulatingValidationListener.Error("age", "Less than 24"));
    }


    @Test
    public void stopOnFailingSection() throws Exception {
        AccumulatingValidationListener errors = AccumulatingValidationListener.create();
        LessThan lessThan10 = new LessThan(10, errors.forPath("age"));
        Adult.Validator adult = new Adult.Validator(errors.forPath("age"));
        NoopAdultValidator noopAdultValidator = new NoopAdultValidator(errors.forPath("adult"));

        ValidationChain.of(Integer.class)
                .check(lessThan10::validate)
                .check(adult::validate)
                .then(Adult::of)
                .check(noopAdultValidator::validate)
                .test(8);

        assertThat(errors.list()).containsOnly(new AccumulatingValidationListener.Error("age", "Less than 10"));
    }

    @Test
    public void aggregateNull() throws Exception {
        AccumulatingValidationListener errors = AccumulatingValidationListener.create();
        RequiredValidator required = new RequiredValidator(errors.forPath("age"));
        Adult.Validator age = new Adult.Validator(errors.forPath("age"));


        ValidationChain.of(Integer.class)
                .check(required::validate)
                .check(age::validate)
                .then(Adult::of)
                .test(null);

        assertThat(errors.list()).extracting("message").containsOnly("Is required");
    }

    @Test
    public void aggregateErrorForNull() throws Exception {
        AccumulatingValidationListener errors = AccumulatingValidationListener.create();
        RequiredValidator required = new RequiredValidator(errors.forPath("age"));
        Email.Validator age = new Email.Validator(errors.forPath("age"));


        ValidationChain.of(String.class)
                .check(required::validate)
                .check(age::validate)
                .then(Email::of)
                .test(null);

        assertThat(errors.list()).extracting("message").containsOnly("Is required");
    }

    @Test
    public void failOnFirst() throws Exception {
        AccumulatingValidationListener listener = AccumulatingValidationListener.create();
        Adult.Validator age = new Adult.Validator(listener.forPath("age"));

        LessThan lessThan2 = new LessThan(2, listener.forPath("age"));
        LessThan lessThan3 = new LessThan(3, listener.forPath("age"));

        ValidationChain.of(Integer.class)
                .check(lessThan2::validate)
                .then(Function.identity())
                .check(lessThan3::validate)
                .check(age::validate)
                .then(Adult::of)
                .test(1);
        assertThat(listener.list()).extracting("message").containsOnly("Less than 2");
    }

    @Test
    public void failOnSecond() throws Exception {
        AccumulatingValidationListener listener = AccumulatingValidationListener.create();
        Adult.Validator age = new Adult.Validator(listener.forPath("age"));

        LessThan lessThan2 = new LessThan(2, listener.forPath("age"));
        LessThan lessThan3 = new LessThan(3, listener.forPath("age"));
        LessThan lessThan4 = new LessThan(4, listener.forPath("age"));

        ValidationChain.of(Integer.class)
                .check(lessThan2::validate)
                .check(lessThan3::validate)
                .check(lessThan4::validate)
                .then(Function.identity())
                .check(age::validate)
                .then(Adult::of)

                .test(2);
        assertThat(listener.list()).extracting("message")
                .containsOnly("Less than 3");
    }

    @Test
    public void notFail() throws Exception {
        AccumulatingValidationListener listener = AccumulatingValidationListener.create();
        Adult.Validator age = new Adult.Validator(listener.forPath("age"));

        LessThan lessThan2 = new LessThan(2, listener.forPath("age"));
        LessThan lessThan3 = new LessThan(3, listener.forPath("age"));
        LessThan lessThan4 = new LessThan(4, listener.forPath("age"));

        ValidationChain.of(Integer.class)
                .check(lessThan2::validate)
                .check(lessThan3::validate)
                .check(lessThan4::validate)
                .then(Function.identity())
                .check(age::validate)
                .then(Adult::of)
                .check(Adult::test)

                .test(30);
        assertThat(listener.list()).isEmpty();
    }

    @Test
    public void failThrowException() throws Exception {
        try {
            ValidationChain.of(Integer.class)
                    .then(Adult::of)
                    .test(-1);
            failBecauseExceptionWasNotThrown(Adult.KidIsInDaHouse.class);
        } catch (Exception e) {
            StrictAssertions.assertThat(e).isInstanceOf(Adult.KidIsInDaHouse.class);
        }
    }

    static class LessThan {
        private final ValidationListener result;
        private final int limit;

        LessThan(int limit, ValidationListener result) {
            this.result = result;
            this.limit = limit;
        }

        void validate(Integer source) {
            if (source < limit) {
                result.reject(new RuntimeException("Less than " + limit));
            }
        }
    }

    static class NoopAdultValidator {
        private final ValidationListener result;

        NoopAdultValidator(ValidationListener result) {
            this.result = result;
        }

        void validate(Adult source) {
            throw new RuntimeException("Should not go here");
        }
    }


}