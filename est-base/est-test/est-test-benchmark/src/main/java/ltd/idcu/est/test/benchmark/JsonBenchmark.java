package ltd.idcu.est.test.benchmark;

import ltd.idcu.est.utils.format.json.JsonUtils;
import org.openjdk.jmh.annotations.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(1)
@State(Scope.Benchmark)
public class JsonBenchmark {

    private String testJson;
    private Map<String, Object> testObject;
    private TestPojo testPojo;

    @Setup(Level.Iteration)
    public void setup() {
        testObject = new HashMap<>();
        testObject.put("name", "John Doe");
        testObject.put("age", 30);
        testObject.put("email", "john@example.com");
        testObject.put("active", true);
        
        testJson = JsonUtils.toJson(testObject);
        
        testPojo = new TestPojo();
        testPojo.setName("Jane Doe");
        testPojo.setAge(25);
        testPojo.setEmail("jane@example.com");
        testPojo.setActive(false);
    }

    @Benchmark
    public String json_serialize_map() {
        return JsonUtils.toJson(testObject);
    }

    @Benchmark
    public String json_serialize_pojo() {
        return JsonUtils.toJson(testPojo);
    }

    @Benchmark
    public Object json_parse() {
        return JsonUtils.parse(testJson);
    }

    @Benchmark
    public boolean json_validate() {
        return JsonUtils.isValidJson(testJson);
    }

    @Benchmark
    public String json_pretty_print() {
        return JsonUtils.toPrettyJson(testObject);
    }

    public static class TestPojo {
        private String name;
        private int age;
        private String email;
        private boolean active;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public int getAge() { return age; }
        public void setAge(int age) { this.age = age; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public boolean isActive() { return active; }
        public void setActive(boolean active) { this.active = active; }
    }
}
