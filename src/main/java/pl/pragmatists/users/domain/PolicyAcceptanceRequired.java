package pl.pragmatists.users.domain;

public class PolicyAcceptanceRequired extends RuntimeException {
    public PolicyAcceptanceRequired() {
        super("Requires Promotion Policy Acceptance");
    }
}
