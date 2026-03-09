package ltd.idcu.est.config.impl;

import ltd.idcu.est.test.Tests;

public class RunConfigTests {
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("  运行 EST Config Impl 模块测试");
        System.out.println("========================================");
        
        Tests.run(AesConfigEncryptorTest.class);
        Tests.run(DefaultConfigVersionManagerTest.class);
    }
}
