package pl.pragmatists.users.ui;

import pl.pragmatists.validation.Pair;
import pl.pragmatists.validation.ValidationListener;

import java.util.Objects;

class IdenticalValidator {

    private final ValidationListener result;

    public IdenticalValidator(ValidationListener result) {
        this.result = result;
    }

    public void validate(Pair check) {
        if (!Objects.equals(check.param1(), check.param2())) {
            result.reject(new NotIdentical());
        }
    }

    public static class NotIdentical extends RuntimeException {
        public NotIdentical() {
            super("Not identical");
        }
    }
}