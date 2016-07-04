package pl.pragmatists.users.domain;

public class PasswordTooShort extends RuntimeException {
    public PasswordTooShort() {
        super("Is too short");
    }
}
