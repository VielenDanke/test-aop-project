package kz.danke.test.project.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(1)
@Slf4j
public class CloudAspect {

    @Before("kz.danke.test.project.aspect.AspectExpressions.anySearchingOrAnalyticsAppliedWithoutGetterAndSetter()")
    public void cloudLogsWithoutGetterAndSetter() {
        log.info("Cloud");
    }
}
