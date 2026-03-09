package ltd.idcu.est.grpc.core;

import io.grpc.Channel;
import io.grpc.ClientInterceptor;
import io.grpc.ManagedChannel;
import io.grpc.stub.AbstractStub;
import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.Test;

public class GrpcClientBuilderTest {

    @Test
    public void testDefaultValues() {
        GrpcClientBuilder builder = new GrpcClientBuilder();
        Assertions.assertNotNull(builder);
    }

    @Test
    public void testSetHost() {
        GrpcClientBuilder builder = new GrpcClientBuilder();
        GrpcClientBuilder result = builder.host("example.com");
        
        Assertions.assertEquals(builder, result);
    }

    @Test
    public void testSetPort() {
        GrpcClientBuilder builder = new GrpcClientBuilder();
        GrpcClientBuilder result = builder.port(8080);
        
        Assertions.assertEquals(builder, result);
    }

    @Test
    public void testUsePlaintext() {
        GrpcClientBuilder builder = new GrpcClientBuilder();
        GrpcClientBuilder result = builder.usePlaintext(false);
        
        Assertions.assertEquals(builder, result);
    }

    @Test
    public void testSetDeadline() {
        GrpcClientBuilder builder = new GrpcClientBuilder();
        GrpcClientBuilder result = builder.deadline(60000);
        
        Assertions.assertEquals(builder, result);
    }

    @Test
    public void testAddInterceptor() {
        GrpcClientBuilder builder = new GrpcClientBuilder();
        TestInterceptor interceptor = new TestInterceptor();
        GrpcClientBuilder result = builder.addInterceptor(interceptor);
        
        Assertions.assertEquals(builder, result);
    }

    @Test
    public void testBuildChannel() {
        GrpcClientBuilder builder = new GrpcClientBuilder();
        
        builder.host("localhost")
               .port(9090)
               .usePlaintext(true);
        
        ManagedChannel channel = builder.buildChannel();
        
        Assertions.assertNotNull(channel);
        channel.shutdown();
    }

    @Test
    public void testNewStub() {
        GrpcClientBuilder builder = new GrpcClientBuilder();
        
        builder.host("localhost")
               .port(9090)
               .usePlaintext(true)
               .deadline(30000);
        
        TestStub stub = builder.newStub(TestStub::new);
        
        Assertions.assertNotNull(stub);
        Channel channel = stub.getChannel();
        if (channel instanceof ManagedChannel) {
            ((ManagedChannel) channel).shutdown();
        }
    }

    @Test
    public void testFluentApi() {
        GrpcClientBuilder builder = new GrpcClientBuilder();
        TestInterceptor interceptor1 = new TestInterceptor();
        TestInterceptor interceptor2 = new TestInterceptor();
        
        GrpcClientBuilder result = builder
            .host("api.example.com")
            .port(443)
            .usePlaintext(false)
            .deadline(10000)
            .addInterceptor(interceptor1)
            .addInterceptor(interceptor2);
        
        Assertions.assertEquals(builder, result);
    }

    private static class TestInterceptor implements ClientInterceptor {
        @Override
        public <ReqT, RespT> io.grpc.ClientCall<ReqT, RespT> interceptCall(
                io.grpc.MethodDescriptor<ReqT, RespT> method,
                io.grpc.CallOptions callOptions,
                Channel next) {
            return next.newCall(method, callOptions);
        }
    }

    private static class TestStub extends AbstractStub<TestStub> {
        TestStub(Channel channel) {
            super(channel);
        }

        private TestStub(Channel channel, io.grpc.CallOptions callOptions) {
            super(channel, callOptions);
        }

        @Override
        protected TestStub build(Channel channel, io.grpc.CallOptions callOptions) {
            return new TestStub(channel, callOptions);
        }
    }
}
