package ltd.idcu.est.test.benchmark;

import ltd.idcu.est.web.DefaultRouter;
import ltd.idcu.est.web.OptimizedRouter;
import ltd.idcu.est.web.api.HttpMethod;
import ltd.idcu.est.web.api.Router;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 10, time = 1)
@Fork(1)
@State(Scope.Benchmark)
public class RouterComparisonBenchmark {

    private Router defaultRouter;
    private Router optimizedRouter;

    private static final String[] STATIC_PATHS = {
        "/", "/about", "/contact", "/products", "/services",
        "/api/users", "/api/products", "/api/orders", "/api/customers",
        "/admin", "/admin/dashboard", "/admin/users", "/admin/settings"
    };

    private static final String[] PARAM_PATHS = {
        "/users/{id}", "/products/{id}", "/orders/{id}", "/customers/{id}",
        "/api/users/{id}/posts", "/api/users/{id}/comments",
        "/categories/{categoryId}/products/{productId}"
    };

    @Setup(Level.Iteration)
    public void setup() {
        defaultRouter = new DefaultRouter();
        optimizedRouter = new OptimizedRouter();

        for (String path : STATIC_PATHS) {
            defaultRouter.get(path, (req, res) -> res.text("ok"));
            optimizedRouter.get(path, (req, res) -> res.text("ok"));
        }

        for (String path : PARAM_PATHS) {
            defaultRouter.get(path, (req, res) -> res.text("ok"));
            optimizedRouter.get(path, (req, res) -> res.text("ok"));
        }

        for (int i = 0; i < 100; i++) {
            final int index = i;
            String path = "/route" + index;
            defaultRouter.get(path, (req, res) -> res.text("route" + index));
            optimizedRouter.get(path, (req, res) -> res.text("route" + index));
        }
    }

    @Benchmark
    public void default_router_static_match() {
        for (String path : STATIC_PATHS) {
            defaultRouter.match(path, HttpMethod.GET);
        }
    }

    @Benchmark
    public void optimized_router_static_match() {
        for (String path : STATIC_PATHS) {
            optimizedRouter.match(path, HttpMethod.GET);
        }
    }

    @Benchmark
    public void default_router_param_match() {
        String[] testPaths = {
            "/users/123", "/products/456", "/orders/789",
            "/api/users/123/posts", "/categories/electronics/products/laptop"
        };
        for (String path : testPaths) {
            defaultRouter.match(path, HttpMethod.GET);
        }
    }

    @Benchmark
    public void optimized_router_param_match() {
        String[] testPaths = {
            "/users/123", "/products/456", "/orders/789",
            "/api/users/123/posts", "/categories/electronics/products/laptop"
        };
        for (String path : testPaths) {
            optimizedRouter.match(path, HttpMethod.GET);
        }
    }

    @Benchmark
    public void default_router_many_routes_match() {
        for (int i = 0; i < 100; i++) {
            defaultRouter.match("/route" + i, HttpMethod.GET);
        }
    }

    @Benchmark
    public void optimized_router_many_routes_match() {
        for (int i = 0; i < 100; i++) {
            optimizedRouter.match("/route" + i, HttpMethod.GET);
        }
    }
}
