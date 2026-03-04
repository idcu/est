package ltd.idcu.est.core.api.condition;

import ltd.idcu.est.core.api.Config;

public interface Condition {
    boolean matches(Config config, Class<?> beanClass);
}
