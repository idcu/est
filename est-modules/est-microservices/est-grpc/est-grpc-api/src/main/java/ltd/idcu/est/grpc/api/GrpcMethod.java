package ltd.idcu.est.grpc.api;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GrpcMethod {
    
    String name() default "";
    
    GrpcMethodType type() default GrpcMethodType.UNARY;
    
}
