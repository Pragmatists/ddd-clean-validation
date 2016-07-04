package pl.pragmatists.users.domain;

public class InvalidEmail extends RuntimeException {
    public InvalidEmail(String email) {
        super(String.format("Provided email [%s] is not valid", email));
    }
}
