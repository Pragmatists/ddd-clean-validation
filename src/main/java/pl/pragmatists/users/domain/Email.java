package pl.pragmatists.users.domain;

import pl.pragmatists.validation.ExceptionThrowingListener;
import pl.pragmatists.validation.ValidationListener;

import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class Email {

    private final String value;
    private final Validator validator = new Validator(new ExceptionThrowingListener());

    public static Email of(String value) {
        Email email = new Email(value);
        email.validate();
        return email;
    }

    private Email(String value) {
        this.value = value;
    }

    private void validate() {
        validator.validate(value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Email)) {
            return false;
        }
        Email id = (Email) obj;

        return id.value.equals(value);
    }

    @Override
    public String toString() {
        return value;
    }

    public static class Validator {

        private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

        private final ValidationListener listener;

        public Validator(ValidationListener listener) {
            this.listener = listener;
        }

        public void validate(String email) {
            if (isBlank(email) || !EMAIL_PATTERN.matcher(email).matches()) {
                listener.reject(new InvalidEmail(email));
            }
        }
    }
}
