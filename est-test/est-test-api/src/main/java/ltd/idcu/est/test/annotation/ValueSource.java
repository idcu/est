package ltd.idcu.est.test.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ValueSource {
    String[] strings() default {};
    int[] ints() default {};
    long[] longs() default {};
    double[] doubles() default {};
    boolean[] booleans() default {};
}
