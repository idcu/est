package ltd.idcu.est.web.api;

public interface Middleware {

    String getName();

    int getPriority();

    default boolean before(Request request, Response response) {
        return true;
    }

    default void after(Request request, Response response) {
    }

    default void onError(Request request, Response response, Exception e) {
        response.sendError(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    default boolean shouldApply(Request request) {
        return true;
    }

    default boolean isGlobal() {
        return false;
    }

    interface Chain {
        void proceed(Request request, Response response);
        Middleware getCurrent();
        Chain getNext();
    }
}
