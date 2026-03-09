package ltd.idcu.est.grpc.core;

import io.grpc.*;
import io.grpc.stub.AbstractStub;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class GrpcClientBuilder {
    
    private String host = "localhost";
    private int port = 9090;
    private boolean usePlaintext = true;
    private long deadlineMs = 30000;
    private final List<ClientInterceptor> interceptors = new ArrayList<>();
    
    public GrpcClientBuilder host(String host) {
        this.host = host;
        return this;
    }
    
    public GrpcClientBuilder port(int port) {
        this.port = port;
        return this;
    }
    
    public GrpcClientBuilder usePlaintext(boolean usePlaintext) {
        this.usePlaintext = usePlaintext;
        return this;
    }
    
    public GrpcClientBuilder deadline(long deadlineMs) {
        this.deadlineMs = deadlineMs;
        return this;
    }
    
    public GrpcClientBuilder addInterceptor(ClientInterceptor interceptor) {
        interceptors.add(interceptor);
        return this;
    }
    
    public ManagedChannel buildChannel() {
        ManagedChannelBuilder<?> builder = ManagedChannelBuilder.forAddress(host, port);
        
        if (usePlaintext) {
            builder.usePlaintext();
        }
        
        for (ClientInterceptor interceptor : interceptors) {
            builder.intercept(interceptor);
        }
        
        return builder.build();
    }
    
    public <T extends AbstractStub<T>> T newStub(StubFactory<T> factory) {
        ManagedChannel channel = buildChannel();
        T stub = factory.create(channel);
        if (deadlineMs > 0) {
            stub = stub.withDeadlineAfter(deadlineMs, TimeUnit.MILLISECONDS);
        }
        return stub;
    }
    
    @FunctionalInterface
    public interface StubFactory<T extends AbstractStub<T>> {
        T create(Channel channel);
    }
    
}
