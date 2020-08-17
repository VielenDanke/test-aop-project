package kz.danke.test.project.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(3)
@Slf4j
public class AnalyticAspect {

    @Before("kz.danke.test.project.aspect.AspectExpressions.anySearchingOrAnalyticsAppliedWithoutGetterAndSetter()")
    public void analyticsWithoutGetterAndSetter() {
        log.info("Analytics");


    }
}
