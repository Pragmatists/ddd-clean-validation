package pl.pragmatists.validation;

public interface ValidationListener {

    void reject(RuntimeException reason);

}
