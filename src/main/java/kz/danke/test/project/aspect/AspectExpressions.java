package kz.danke.test.project.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class AspectExpressions {

    @Pointcut("execution(public * kz.danke.test.project.service.*.*(..))")
    public void anySearchingOrAnalyticsApplied() {}

    @Pointcut("execution(public * kz.danke.test.project.service.*.get*(..))")
    public void anyGetter() {}

    @Pointcut("execution(public * kz.danke.test.project.service.*.set*(..))")
    public void anySetter() {}

    @Pointcut("anySearchingOrAnalyticsApplied() && !(anyGetter() || anySetter())")
    public void anySearchingOrAnalyticsAppliedWithoutGetterAndSetter() {}
}
