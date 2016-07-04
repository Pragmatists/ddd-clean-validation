package pl.pragmatists.validation;

public class Pair<T1, T2> {

    private T1 param1;
    private T2 param2;

    private Pair(T1 param1, T2 param2) {
        this.param1 = param1;
        this.param2 = param2;
    }

    public static <T1, T2> Pair<T1, T2> of(T1 param1, T2 param2) {
        return new Pair<>(param1, param2);
    }

    public T1 param1() {
        return param1;
    }

    public T2 param2() {
        return param2;
    }
}
