package ltd.idcu.est.data.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ManyToMany {
    String mappedBy() default "";
    FetchType fetch() default FetchType.LAZY;
    CascadeType[] cascade() default {};
    String joinTable() default "";
    String joinColumn() default "";
    String inverseJoinColumn() default "";
    boolean lazy() default true;

    enum FetchType {
        EAGER,
        LAZY
    }

    enum CascadeType {
        ALL,
        PERSIST,
        MERGE,
        REMOVE,
        REFRESH,
        DETACH
    }
}
