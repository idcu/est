package ltd.idcu.est.features.data.api;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Column {
    
    String name() default "";
    
    boolean nullable() default true;
    
    boolean unique() default false;
    
    int length() default 255;
    
    int precision() default 0;
    
    int scale() default 0;
    
    String columnDefinition() default "";
}
