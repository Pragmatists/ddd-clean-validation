package pl.pragmatists.users.domain;

import pl.pragmatists.validation.ExceptionThrowingListener;
import pl.pragmatists.validation.ValidationListener;

public class Password {

    private final String value;

    private final Validator validator = new Validator(new ExceptionThrowingListener());

    private Password(String value) {
        this.value = value;
    }

    public static Password of(String value) {
        Password password = new Password(value);
        password.validate();
        return password;
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
        if (!(obj instanceof Password)) {
            return false;
        }
        Password id = (Password) obj;

        return id.value.equals(value);
    }

    public static class Validator {

        private final ValidationListener listener;

        public Validator(ValidationListener listener) {
            this.listener = listener;
        }

        public void validate(String password) {
            if (password == null) {
                listener.reject(new Required());
            } else if (password.length() < 8) {
                listener.reject(new PasswordTooShort());
            }
        }
    }

}
