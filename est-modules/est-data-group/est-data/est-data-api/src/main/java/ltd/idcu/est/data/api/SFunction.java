package ltd.idcu.est.data.api;

import java.io.Serializable;

@FunctionalInterface
public interface SFunction<T, R> extends Serializable {

    R apply(T t);

    default String getFieldName() {
        return LambdaUtils.getFieldName(this);
    }
}
