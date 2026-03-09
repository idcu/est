package ltd.idcu.est.grpc.api;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GrpcService {
    
    String name() default "";
    
    String version() default "1.0.0";
    
    String description() default "";
    
}
