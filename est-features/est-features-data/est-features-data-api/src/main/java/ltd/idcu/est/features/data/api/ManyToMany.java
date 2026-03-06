package ltd.idcu.est.features.data.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ManyToMany {
    String joinTable() default "";
    
    String joinColumn() default "";
    
    String inverseJoinColumn() default "";
    
    String mappedBy() default "";
    
    boolean lazy() default true;
}
