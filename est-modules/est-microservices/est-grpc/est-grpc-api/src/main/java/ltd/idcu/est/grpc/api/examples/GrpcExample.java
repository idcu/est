package ltd.idcu.est.grpc.api.examples;

import ltd.idcu.est.grpc.api.*;

public class GrpcExample {
    
    public static void main(String[] args) throws Exception {
        basicExample();
        annotationExample();
    }
    
    public static void basicExample() {
        System.out.println("=== gRPC Basic Example ===");
        
        System.out.println("GrpcMethodType values:");
        for (GrpcMethodType type : GrpcMethodType.values()) {
            System.out.println("  - " + type);
        }
        System.out.println();
    }
    
    public static void annotationExample() {
        System.out.println("=== gRPC Annotation Example ===");
        
        System.out.println("@GrpcService - Marks a class as a gRPC service");
        System.out.println("@GrpcMethod - Marks a method as a gRPC method");
        System.out.println("@GrpcInterceptor - Interface for gRPC interceptors");
        System.out.println();
    }
    
    @GrpcService(name = "UserService", version = "1.0.0", description = "User management service")
    public static class UserService {
        
        @GrpcMethod(name = "getUser", type = GrpcMethodType.UNARY)
        public void getUser() {
        }
        
        @GrpcMethod(name = "streamUsers", type = GrpcMethodType.SERVER_STREAMING)
        public void streamUsers() {
        }
        
    }
    
}
