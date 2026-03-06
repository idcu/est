package ltd.idcu.est.test.benchmark;

import ltd.idcu.est.web.Web;
import ltd.idcu.est.web.DefaultRouter;
import ltd.idcu.est.web.api.WebApplication;
import ltd.idcu.est.web.api.Router;
import ltd.idcu.est.web.api.HttpMethod;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(1)
@State(Scope.Benchmark)
public class WebBenchmark {

    private Router router;
    private WebApplication webApp;

    @Setup(Level.Iteration)
    public void setup() {
        router = new DefaultRouter();
        for (int i = 0; i < 100; i++) {
            final int index = i;
            router.get("/route" + i, (req, res) -> res.text("route" + index));
        }
        
        webApp = Web.create("Benchmark", "1.0.0");
        webApp.getRouter().get("/", (req, res) -> res.text("Hello"));
    }

    @Benchmark
    public void router_match_simple() {
        router.match("/route50", HttpMethod.GET);
    }

    @Benchmark
    public void router_add_route() {
        Router r = new DefaultRouter();
        r.get("/test", (req, res) -> res.text("test"));
    }

    @Benchmark
    public void router_match_with_params() {
        Router r = new DefaultRouter();
        r.get("/users/{id}/posts/{postId}", (req, res) -> {});
        r.match("/users/123/posts/456", HttpMethod.GET);
    }
}
