package ltd.idcu.est.features.data.api;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Entity {
    
    String tableName() default "";
    
    String schema() default "";
}
