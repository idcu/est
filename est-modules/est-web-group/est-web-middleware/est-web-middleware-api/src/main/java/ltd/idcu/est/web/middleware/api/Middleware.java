package ltd.idcu.est.web.middleware.api;

public interface Middleware {

    String getName();

    int getPriority();

    default boolean before(Object request, Object response) {
        return true;
    }

    default void after(Object request, Object response) {
    }

    default void onError(Object request, Object response, Exception e) {
    }

    default boolean shouldApply(Object request) {
        return true;
    }

    default boolean isGlobal() {
        return false;
    }

    interface Chain {
        void proceed(Object request, Object response);
        Middleware getCurrent();
        Chain getNext();
    }
}
