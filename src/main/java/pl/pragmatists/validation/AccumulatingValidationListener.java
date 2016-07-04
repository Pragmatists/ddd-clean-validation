package pl.pragmatists.validation;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AccumulatingValidationListener implements ValidationListener {

    private final String path;

    private final List<Error> errors;

    private AccumulatingValidationListener(String path, List<Error> errors) {
        this.path = path;
        this.errors = errors;
    }

    public static AccumulatingValidationListener create() {
        return new AccumulatingValidationListener("", new ArrayList<>());
    }

    public AccumulatingValidationListener forPath(String path) {
        return new AccumulatingValidationListener(path, errors);
    }

    @Override
    public void reject(RuntimeException reason) {
        errors.add(new Error(path, reason.getMessage()));
        throw new ValidationChainFailed(reason);
    }

    public List<Error> list() {
        return errors;
    }

    public static class Error {
        private final String path;
        private final String message;

        public Error(String path, String message) {
            this.path = path;
            this.message = message;
        }

        public String path() {
            return path;
        }

        public String message() {
            return message;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Error)) return false;
            Error error = (Error) o;
            return Objects.equals(path, error.path) &&
                Objects.equals(message, error.message);
        }

        @Override
        public int hashCode() {
            return Objects.hash(path, message);
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
        }
    }
}
