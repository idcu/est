package ltd.idcu.est.test.benchmark;

import ltd.idcu.est.core.api.Container;
import ltd.idcu.est.core.impl.DefaultContainer;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(1)
@State(Scope.Benchmark)
public class ContainerBenchmark {

    public interface TestService {
        String doSomething();
    }

    public static class TestServiceImpl implements TestService {
        @Override
        public String doSomething() {
            return "test";
        }
    }

    private Container container;

    @Setup(Level.Iteration)
    public void setup() {
        container = new DefaultContainer();
        container.register(TestService.class, TestServiceImpl.class);
    }

    @Benchmark
    public TestService container_get() {
        return container.get(TestService.class);
    }

    @Benchmark
    public boolean container_contains() {
        return container.contains(TestService.class);
    }
}
