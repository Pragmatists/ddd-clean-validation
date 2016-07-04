package pl.pragmatists.validation;

public class ValidationChainFailed extends RuntimeException{
    private final RuntimeException reason;

    public ValidationChainFailed(RuntimeException reason) {
        this.reason = reason;
    }

    public RuntimeException getReason() {
        return reason;
    }
}
