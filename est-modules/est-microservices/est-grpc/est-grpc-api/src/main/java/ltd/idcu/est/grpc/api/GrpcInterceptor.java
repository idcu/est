package ltd.idcu.est.grpc.api;

import io.grpc.*;

public interface GrpcInterceptor {
    
    <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
        ServerCall<ReqT, RespT> call,
        Metadata headers,
        ServerCallHandler<ReqT, RespT> next
    );
    
    default int getOrder() {
        return 0;
    }
    
}
