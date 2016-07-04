package pl.pragmatists.users.domain;

public class UserAlreadyExists extends RuntimeException {
    public UserAlreadyExists(Email email) {
        super(String.format("Login [%s] already taken", email));
    }
}
