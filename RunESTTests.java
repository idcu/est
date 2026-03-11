import ltd.idcu.est.test.runner.TestRunner;

public class RunESTTests {
    public static void main(String[] args) throws Exception {
        System.out.println("========================================");
        System.out.println("EST Framework Test Suite");
        System.out.println("========================================");
        System.out.println();

        TestRunner runner = new TestRunner();

        System.out.println("Running core container tests...");
        runner.runClass(Class.forName("ltd.idcu.est.core.container.impl.DefaultContainerTest"));
        System.out.println();

        System.out.println("Running core config tests...");
        runner.runClass(Class.forName("ltd.idcu.est.core.config.impl.DefaultConfigTest"));
        System.out.println();

        System.out.println("Running patterns tests...");
        runner.runClass(Class.forName("ltd.idcu.est.patterns.impl.creational.DefaultFactoryTest"));
        runner.runClass(Class.forName("ltd.idcu.est.patterns.impl.creational.DefaultSingletonTest"));
        runner.runClass(Class.forName("ltd.idcu.est.patterns.impl.structural.DefaultAdapterTest"));
        runner.runClass(Class.forName("ltd.idcu.est.patterns.impl.structural.DefaultProxyTest"));
        runner.runClass(Class.forName("ltd.idcu.est.patterns.impl.behavioral.DefaultStrategyTest"));
        runner.runClass(Class.forName("ltd.idcu.est.patterns.impl.behavioral.DefaultCommandInvokerTest"));
        System.out.println();

        System.out.println("Running collection tests...");
        runner.runClass(Class.forName("ltd.idcu.est.collection.impl.SeqTest"));
        runner.runClass(Class.forName("ltd.idcu.est.collection.impl.MapSeqsTest"));
        System.out.println();

        System.out.println("Running utils tests...");
        runner.runClass(Class.forName("ltd.idcu.est.utils.common.AssertUtilsTest"));
        runner.runClass(Class.forName("ltd.idcu.est.utils.common.ObjectUtilsTest"));
        runner.runClass(Class.forName("ltd.idcu.est.utils.common.StringUtilsTest"));
        runner.runClass(Class.forName("ltd.idcu.est.utils.common.ValidateUtilsTest"));
        runner.runClass(Class.forName("ltd.idcu.est.utils.common.EncryptUtilsTest"));
        runner.runClass(Class.forName("ltd.idcu.est.utils.common.PerformanceUtilsTest"));
        runner.runClass(Class.forName("ltd.idcu.est.utils.common.CollectionOptimizerUtilsTest"));
        System.out.println();

        System.out.println("Running event tests...");
        runner.runClass(Class.forName("ltd.idcu.est.event.local.LocalEventBusTest"));
        System.out.println();

        System.out.println("Running cache tests...");
        runner.runClass(Class.forName("ltd.idcu.est.cache.memory.MemoryCacheTest"));
        System.out.println();

        System.out.println("Running workflow tests...");
        runner.runClass(Class.forName("ltd.idcu.est.workflow.core.WorkflowEngineTest"));
        runner.runClass(Class.forName("ltd.idcu.est.workflow.core.WorkflowContextTest"));
        runner.runClass(Class.forName("ltd.idcu.est.workflow.core.MemoryWorkflowRepositoryTest"));
        runner.runClass(Class.forName("ltd.idcu.est.workflow.core.JsonWorkflowDefinitionParserTest"));
        runner.runClass(Class.forName("ltd.idcu.est.workflow.core.ParallelGatewayTest"));
        System.out.println();

        System.out.println("Running admin tests...");
        runner.runClass(Class.forName("ltd.idcu.est.admin.DefaultUserServiceTest"));
        runner.runClass(Class.forName("ltd.idcu.est.admin.DefaultRoleServiceTest"));
        runner.runClass(Class.forName("ltd.idcu.est.admin.DefaultMenuServiceTest"));
        runner.runClass(Class.forName("ltd.idcu.est.admin.DefaultDepartmentServiceTest"));
        runner.runClass(Class.forName("ltd.idcu.est.admin.DefaultTenantServiceTest"));
        System.out.println();

        System.out.println("Running AI tests...");
        runner.runClass(Class.forName("ltd.idcu.est.ai.impl.config.DefaultAiConfigTest"));
        runner.runClass(Class.forName("ltd.idcu.est.ai.impl.storage.MemoryStorageProviderTest"));
        runner.runClass(Class.forName("ltd.idcu.est.ai.impl.config.LlmProviderConfigTest"));
        System.out.println();

        System.out.println("Running RAG tests...");
        runner.runClass(Class.forName("ltd.idcu.est.rag.impl.DefaultRagEngineTest"));
        runner.runClass(Class.forName("ltd.idcu.est.rag.impl.InMemoryVectorStoreTest"));
        runner.runClass(Class.forName("ltd.idcu.est.rag.impl.FixedSizeTextSplitterTest"));
        runner.runClass(Class.forName("ltd.idcu.est.rag.impl.DocumentTest"));
        System.out.println();

        System.out.println("Running plugin marketplace tests...");
        runner.runClass(Class.forName("ltd.idcu.est.plugin.marketplace.impl.DefaultPluginMarketplaceTest"));
        runner.runClass(Class.forName("ltd.idcu.est.plugin.marketplace.impl.LocalPluginRepositoryTest"));
        runner.runClass(Class.forName("ltd.idcu.est.plugin.marketplace.impl.DefaultPluginReviewServiceTest"));
        System.out.println();

        System.out.println("Running observability tests...");
        runner.runClass(Class.forName("ltd.idcu.est.observability.api.DefaultObservabilityTest"));
        runner.runClass(Class.forName("ltd.idcu.est.observability.api.SimpleTraceContextTest"));
        System.out.println();

        System.out.println("Running gRPC tests...");
        runner.runClass(Class.forName("ltd.idcu.est.grpc.api.GrpcServiceTest"));
        runner.runClass(Class.forName("ltd.idcu.est.grpc.api.GrpcMethodTest"));
        runner.runClass(Class.forName("ltd.idcu.est.grpc.api.GrpcMethodTypeTest"));
        runner.runClass(Class.forName("ltd.idcu.est.grpc.core.GrpcServerBuilderTest"));
        runner.runClass(Class.forName("ltd.idcu.est.grpc.core.GrpcClientBuilderTest"));
        runner.runClass(Class.forName("ltd.idcu.est.grpc.core.GrpcServiceRegistryTest"));
        System.out.println();

        System.out.println("Running multitenancy tests...");
        runner.runClass(Class.forName("ltd.idcu.est.multitenancy.TenantContextTest"));
        runner.runClass(Class.forName("ltd.idcu.est.multitenancy.TenantContextHolderTest"));
        runner.runClass(Class.forName("ltd.idcu.est.multitenancy.TenantDataIsolationStrategyTest"));
        runner.runClass(Class.forName("ltd.idcu.est.multitenancy.TenantDataSecurityFilterTest"));
        runner.runClass(Class.forName("ltd.idcu.est.multitenancy.TenantAuditServiceTest"));
        runner.runClass(Class.forName("ltd.idcu.est.multitenancy.TenantInterceptorsTest"));
        System.out.println();

        System.out.println("Running serverless tests...");
        runner.runClass(Class.forName("ltd.idcu.est.serverless.api.ColdStartOptimizerTest"));
        runner.runClass(Class.forName("ltd.idcu.est.serverless.api.ServerlessLocalRunnerTest"));
        System.out.println();

        System.out.println("Running config tests...");
        runner.runClass(Class.forName("ltd.idcu.est.config.impl.AesConfigEncryptorTest"));
        runner.runClass(Class.forName("ltd.idcu.est.config.impl.DefaultConfigVersionManagerTest"));
        System.out.println();

        System.out.println("Running gateway tests...");
        runner.runClass(Class.forName("ltd.idcu.est.gateway.GatewayTest"));
        System.out.println();

        System.out.println("Running tracing tests...");
        runner.runClass(Class.forName("ltd.idcu.est.tracing.impl.TracingTest"));
        System.out.println();

        System.out.println("Running lifecycle tests...");
        runner.runClass(Class.forName("ltd.idcu.est.core.lifecycle.impl.LifecycleManagerTest"));
        runner.runClass(Class.forName("ltd.idcu.est.core.lifecycle.impl.DefaultLifecycleTest"));
        System.out.println();

        System.out.println("Running AOP tests...");
        runner.runClass(Class.forName("ltd.idcu.est.core.aop.impl.DefaultJoinPointTest"));
        runner.runClass(Class.forName("ltd.idcu.est.core.aop.impl.PointcutExpressionTest"));
        System.out.println();

        System.out.println("Running module tests...");
        runner.runClass(Class.forName("ltd.idcu.est.core.module.impl.AbstractModuleTest"));
        System.out.println();

        System.out.println("========================================");
        System.out.println("All tests completed!");
        System.out.println("========================================");
    }
}
