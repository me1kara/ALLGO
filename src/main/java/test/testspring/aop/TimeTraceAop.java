package test.testspring.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
@Slf4j
public class TimeTraceAop {
    @Around("execution(* test.testspring..*(..))")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable{

        StopWatch sw = new StopWatch();
        sw.start();
        try{

        return joinPoint.proceed();
        }finally{
            sw.stop();
            long executionTime = sw.getTotalTimeMillis();
            String className = joinPoint.getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            String task = className + ". "+methodName;
            log.debug("[Execution time]" + task + "-->" + executionTime + "(ms)");
        }

    }
}
