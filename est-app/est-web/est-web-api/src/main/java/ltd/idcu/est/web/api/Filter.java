package ltd.idcu.est.web.api;

import java.util.function.BiConsumer;

public interface Filter {

    String getPath();

    void setPath(String path);

    default boolean matches(String requestPath) {
        if (getPath() == null || getPath().isEmpty()) {
            return true;
        }
        if (getPath().endsWith("*")) {
            String prefix = getPath().substring(0, getPath().length() - 1);
            return requestPath.startsWith(prefix);
        }
        return requestPath.equals(getPath());
    }

    void doFilter(Request request, Response response, FilterChain chain);

    interface FilterChain {
        void doFilter(Request request, Response response);
    }

    interface BeforeFilter extends Filter {
        static BeforeFilter of(String path, BiConsumer<Request, Response> filter) {
            return new BeforeFilter() {
                private String filterPath = path;

                @Override
                public String getPath() {
                    return filterPath;
                }

                @Override
                public void setPath(String path) {
                    this.filterPath = path;
                }

                @Override
                public void doFilter(Request request, Response response, FilterChain chain) {
                    filter.accept(request, response);
                    chain.doFilter(request, response);
                }
            };
        }
    }

    interface AfterFilter extends Filter {
        static AfterFilter of(String path, BiConsumer<Request, Response> filter) {
            return new AfterFilter() {
                private String filterPath = path;

                @Override
                public String getPath() {
                    return filterPath;
                }

                @Override
                public void setPath(String path) {
                    this.filterPath = path;
                }

                @Override
                public void doFilter(Request request, Response response, FilterChain chain) {
                    chain.doFilter(request, response);
                    filter.accept(request, response);
                }
            };
        }
    }
}
