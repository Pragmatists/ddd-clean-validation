package pl.pragmatists.users.domain;

public class UserNotFound extends RuntimeException {
    public UserNotFound(Email email) {
        super(String.format("User with email '%s' doesn't exist", email));
    }
}
