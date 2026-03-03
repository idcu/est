package ltd.idcu.est.test.mock;

public interface OngoingStubbing<T> {

    OngoingStubbing<T> thenReturn(T value);

    @SuppressWarnings("unchecked")
    OngoingStubbing<T> thenReturn(T value, T... values);

    OngoingStubbing<T> thenThrow(Throwable... throwables);

    OngoingStubbing<T> thenThrow(Class<? extends Throwable> throwableClass);

    OngoingStubbing<T> thenCallRealMethod();
}
