package ltd.idcu.est.grpc.core;

import io.grpc.*;
import ltd.idcu.est.grpc.api.GrpcInterceptor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GrpcServerBuilder {
    
    private int port = 9090;
    private final List<io.grpc.BindableService> services = new ArrayList<>();
    private final List<GrpcInterceptor> interceptors = new ArrayList<>();
    
    public GrpcServerBuilder port(int port) {
        this.port = port;
        return this;
    }
    
    public GrpcServerBuilder addService(io.grpc.BindableService service) {
        services.add(service);
        return this;
    }
    
    public GrpcServerBuilder addInterceptor(GrpcInterceptor interceptor) {
        interceptors.add(interceptor);
        return this;
    }
    
    public Server build() {
        ServerBuilder<?> builder = ServerBuilder.forPort(port);
        
        interceptors.sort(Comparator.comparingInt(GrpcInterceptor::getOrder));
        for (GrpcInterceptor interceptor : interceptors) {
            builder.intercept(new ServerInterceptorAdapter(interceptor));
        }
        
        for (io.grpc.BindableService service : services) {
            builder.addService(service);
        }
        
        return builder.build();
    }
    
    public Server start() throws IOException {
        Server server = build();
        server.start();
        return server;
    }
    
    private static class ServerInterceptorAdapter implements ServerInterceptor {
        private final GrpcInterceptor interceptor;
        
        ServerInterceptorAdapter(GrpcInterceptor interceptor) {
            this.interceptor = interceptor;
        }
        
        @Override
        public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            Metadata headers,
            ServerCallHandler<ReqT, RespT> next
        ) {
            return interceptor.interceptCall(call, headers, next);
        }
    }
    
}
