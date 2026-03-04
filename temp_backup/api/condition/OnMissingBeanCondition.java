package ltd.idcu.est.core.api.condition;

import ltd.idcu.est.core.api.Config;
import ltd.idcu.est.core.api.annotation.ConditionalOnMissingBean;

public class OnMissingBeanCondition implements Condition {

    @Override
    public boolean matches(Config config, Class<?> beanClass) {
        ConditionalOnMissingBean annotation = beanClass.getAnnotation(ConditionalOnMissingBean.class);
        if (annotation == null) {
            return true;
        }

        return true;
    }
}
