package ltd.idcu.est.test.benchmark;

import ltd.idcu.est.data.api.ConnectionPool;
import ltd.idcu.est.data.api.DataConfig;
import ltd.idcu.est.data.jdbc.DefaultConnectionPool;
import ltd.idcu.est.data.jdbc.OptimizedConnectionPool;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@Warmup(iterations = 3, time = 2, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 3, timeUnit = TimeUnit.SECONDS)
@Fork(value = 1, warmups = 0)
public class ConnectionPoolComparisonBenchmark {

    private ConnectionPool defaultPool;
    private ConnectionPool optimizedPool;

    @Setup(Level.Trial)
    public void setup() {
        DataConfig config = DataConfig.builder()
                .url("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1")
                .username("sa")
                .password("")
                .minPoolSize(10)
                .maxPoolSize(50)
                .connectionTimeout(5000)
                .build();
        config.setMaxStatementCacheSize(100);
        config.setMaxIdleTime(60000);
        config.setTestOnBorrow(false);

        defaultPool = new DefaultConnectionPool(config);
        optimizedPool = new OptimizedConnectionPool(config);

        initializeTestData(defaultPool);
    }

    private void initializeTestData(ConnectionPool pool) {
        try (Connection conn = pool.getConnection();
             PreparedStatement stmt = conn.prepareStatement("CREATE TABLE IF NOT EXISTS test (id INT PRIMARY KEY, name VARCHAR(100))")) {
            stmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try (Connection conn = pool.getConnection();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO test (id, name) VALUES (?, ?)")) {
            for (int i = 0; i < 100; i++) {
                stmt.setInt(1, i);
                stmt.setString(2, "Test" + i);
                stmt.addBatch();
            }
            stmt.executeBatch();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @TearDown(Level.Trial)
    public void tearDown() {
        try {
            defaultPool.close();
            optimizedPool.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Benchmark
    @Threads(8)
    public void testDefaultConnectionPool(Blackhole blackhole) {
        try (Connection conn = defaultPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM test WHERE id = ?")) {
            stmt.setInt(1, (int) (System.currentTimeMillis() % 100));
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    blackhole.consume(rs.getInt("id"));
                    blackhole.consume(rs.getString("name"));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Benchmark
    @Threads(8)
    public void testOptimizedConnectionPool(Blackhole blackhole) {
        try (Connection conn = optimizedPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM test WHERE id = ?")) {
            stmt.setInt(1, (int) (System.currentTimeMillis() % 100));
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    blackhole.consume(rs.getInt("id"));
                    blackhole.consume(rs.getString("name"));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Benchmark
    @Threads(16)
    public void testDefaultConnectionPoolHighConcurrency(Blackhole blackhole) {
        try (Connection conn = defaultPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM test WHERE id = ?")) {
            stmt.setInt(1, (int) (System.currentTimeMillis() % 100));
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    blackhole.consume(rs.getInt("id"));
                    blackhole.consume(rs.getString("name"));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Benchmark
    @Threads(16)
    public void testOptimizedConnectionPoolHighConcurrency(Blackhole blackhole) {
        try (Connection conn = optimizedPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM test WHERE id = ?")) {
            stmt.setInt(1, (int) (System.currentTimeMillis() % 100));
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    blackhole.consume(rs.getInt("id"));
                    blackhole.consume(rs.getString("name"));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(ConnectionPoolComparisonBenchmark.class.getSimpleName())
                .forks(1)
                .build();

        new Runner(opt).run();
    }
}
