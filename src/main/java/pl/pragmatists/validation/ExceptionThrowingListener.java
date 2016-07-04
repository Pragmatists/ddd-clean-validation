package pl.pragmatists.validation;

public class ExceptionThrowingListener implements ValidationListener {

    @Override
    public void reject(RuntimeException reason) {
        throw reason;
    }
}
