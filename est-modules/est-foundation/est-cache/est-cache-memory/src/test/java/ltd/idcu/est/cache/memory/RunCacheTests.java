package ltd.idcu.est.cache.memory;

import ltd.idcu.est.test.Tests;

public class RunCacheTests {
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("  运行 EST Cache Memory 模块测试");
        System.out.println("========================================");
        
        Tests.run(MemoryCacheTest.class);
    }
}
