package pl.pragmatists.users.domain;

public class Required extends RuntimeException {
    public Required() {
        super("Is required");
    }
}
