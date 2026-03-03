package ltd.idcu.est.web.api;

public interface Controller {

    Request getRequest();

    Response getResponse();

    default String render(String viewName) {
        return render(viewName, null);
    }

    String render(String viewName, Object model);

    default String redirect(String url) {
        getResponse().redirect(url);
        return null;
    }

    default String redirect(String url, HttpStatus status) {
        getResponse().redirect(url, status);
        return null;
    }

    default void setContentType(String contentType) {
        getResponse().setContentType(contentType);
    }

    default void setHeader(String name, String value) {
        getResponse().setHeader(name, value);
    }

    default String getParameter(String name) {
        return getRequest().getParameter(name);
    }

    default String getParameterOrDefault(String name, String defaultValue) {
        return getRequest().getParameterOrDefault(name, defaultValue);
    }

    default int getIntParameter(String name, int defaultValue) {
        return getRequest().getIntParameter(name, defaultValue);
    }

    default long getLongParameter(String name, long defaultValue) {
        return getRequest().getLongParameter(name, defaultValue);
    }

    default boolean getBooleanParameter(String name, boolean defaultValue) {
        return getRequest().getBooleanParameter(name, defaultValue);
    }

    default String getHeader(String name) {
        return getRequest().getHeader(name);
    }

    default String getHeaderOrDefault(String name, String defaultValue) {
        return getRequest().getHeaderOrDefault(name, defaultValue);
    }

    default Session getSession() {
        return getRequest().getSession();
    }

    default Session getSession(boolean create) {
        return getRequest().getSession(create);
    }

    default String getCookie(String name) {
        return getRequest().getCookie(name);
    }

    default void setCookie(String name, String value) {
        getResponse().setCookie(name, value);
    }

    default void setCookie(String name, String value, int maxAge) {
        getResponse().setCookie(name, value, maxAge);
    }

    default void removeCookie(String name) {
        getResponse().removeCookie(name);
    }
}
