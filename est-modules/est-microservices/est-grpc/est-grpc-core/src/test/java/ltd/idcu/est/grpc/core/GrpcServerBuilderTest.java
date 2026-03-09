package ltd.idcu.est.grpc.core;

import io.grpc.BindableService;
import io.grpc.ServerServiceDefinition;
import ltd.idcu.est.grpc.api.GrpcInterceptor;
import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.Test;

public class GrpcServerBuilderTest {

    @Test
    public void testDefaultPort() {
        GrpcServerBuilder builder = new GrpcServerBuilder();
        Assertions.assertNotNull(builder);
    }

    @Test
    public void testSetPort() {
        GrpcServerBuilder builder = new GrpcServerBuilder();
        GrpcServerBuilder result = builder.port(8080);
        
        Assertions.assertEquals(builder, result);
    }

    @Test
    public void testAddService() {
        GrpcServerBuilder builder = new GrpcServerBuilder();
        TestService service = new TestService();
        GrpcServerBuilder result = builder.addService(service);
        
        Assertions.assertEquals(builder, result);
    }

    @Test
    public void testAddInterceptor() {
        GrpcServerBuilder builder = new GrpcServerBuilder();
        TestInterceptor interceptor = new TestInterceptor();
        GrpcServerBuilder result = builder.addInterceptor(interceptor);
        
        Assertions.assertEquals(builder, result);
    }

    @Test
    public void testBuild() {
        GrpcServerBuilder builder = new GrpcServerBuilder();
        TestService service = new TestService();
        TestInterceptor interceptor = new TestInterceptor();
        
        builder.port(8080)
               .addService(service)
               .addInterceptor(interceptor);
        
        io.grpc.Server server = builder.build();
        
        Assertions.assertNotNull(server);
    }

    @Test
    public void testFluentApi() {
        GrpcServerBuilder builder = new GrpcServerBuilder();
        TestService service1 = new TestService();
        TestService service2 = new TestService();
        TestInterceptor interceptor1 = new TestInterceptor(1);
        TestInterceptor interceptor2 = new TestInterceptor(2);
        
        GrpcServerBuilder result = builder
            .port(9091)
            .addService(service1)
            .addService(service2)
            .addInterceptor(interceptor1)
            .addInterceptor(interceptor2);
        
        Assertions.assertEquals(builder, result);
    }

    private static class TestService implements BindableService {
        @Override
        public ServerServiceDefinition bindService() {
            return ServerServiceDefinition.builder("TestService").build();
        }
    }

    private static class TestInterceptor implements GrpcInterceptor {
        private final int order;

        TestInterceptor() {
            this(0);
        }

        TestInterceptor(int order) {
            this.order = order;
        }

        @Override
        public int getOrder() {
            return order;
        }

        @Override
        public <ReqT, RespT> io.grpc.ServerCall.Listener<ReqT> interceptCall(
                io.grpc.ServerCall<ReqT, RespT> call,
                io.grpc.Metadata headers,
                io.grpc.ServerCallHandler<ReqT, RespT> next) {
            return next.startCall(call, headers);
        }
    }
}
