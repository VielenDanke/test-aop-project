package kz.danke.test.project.aspect;

import kz.danke.test.project.model.Student;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Stream;

@Aspect
@Component
@Order(2)
@Slf4j
public class LoggingAspect {

    /*
    Modifiers are optional

    Params:
    () - no args
    * - one any type
    .. - 0 or more any type

    @Pointcut for reusability of expression

    @After working always

    @AfterReturning only on success

    @AfterThrowing only on exception

    @Around working before and after method
     */

    @After("execution(* kz.danke.test.project.service.CrudOperations.findAll())")
    public void afterFinallyFindAllStudents(JoinPoint joinPoint) {
        log.info("Using @After advice: " + joinPoint.getSignature().toShortString());
    }

    @AfterReturning(
            pointcut = "execution(* kz.danke.test.project.service.CrudOperations.findAll())",
            returning = "students"
    )
    public void afterReturningFindAllStudentsAdvice(JoinPoint joinPoint, List<Student> students) {
        log.info("After returning method: " + joinPoint.getSignature().toShortString());

        students
                .forEach(std -> log.info(std.toString()));
    }

    @AfterThrowing(
            pointcut = "execution(* kz.danke.test.project.service.CrudOperations.findById(..))",
            throwing = "ex"
    )
    public void afterThrowingExceptionWithFindById(JoinPoint joinPoint, Throwable ex) {
        log.info("After throwing exception: " + joinPoint.getSignature().toShortString());
        log.info("Exception: " + ex.getClass());
    }

    @Around("execution(* kz.danke.test.project.service.CrudOperations.save(*))")
    public Object afterSaveMethodCheckingAmountOfTime(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        Object resultObject = null;

        /*
        Be aware to use handling exceptions in AOP. Be sure it is not tremendous exception.
         */

        try {
            resultObject = proceedingJoinPoint.proceed();
        } catch (Exception e) {
            log.error(e.toString());
            /*
            Could be thrown custom exception, or re-thrown existin one
             */
        }

        long end = System.currentTimeMillis();

        long duration = end - start;

        log.info("Duration of proceeding method is: " + duration);

        return resultObject;
    }

    @Before("kz.danke.test.project.aspect.AspectExpressions.anySearchingOrAnalyticsAppliedWithoutGetterAndSetter()")
    public void loggingWithoutGetterAndSetter(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

        log.info("Logging " + methodSignature);

        Object[] args = joinPoint.getArgs();

        Stream.of(args)
                .filter(obj -> obj.getClass().isAssignableFrom(Student.class))
                .findAny()
                .ifPresent(std -> {
                    Student student = (Student) std;

                    log.info("Name is: " + student.getUsername());
                });
    }
}
