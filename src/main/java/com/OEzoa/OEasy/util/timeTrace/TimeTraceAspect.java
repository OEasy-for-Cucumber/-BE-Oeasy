package com.OEzoa.OEasy.util.timeTrace;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.lang.reflect.Method;

@Slf4j
@Component
@Aspect
public class TimeTraceAspect {
    @Pointcut("@annotation(com.OEzoa.OEasy.util.timeTrace.TimeTrace)") // 메서드 정의
    public void timeTraceMethod() {}

    @Pointcut("@within(com.OEzoa.OEasy.util.timeTrace.TimeTrace)") // 클래스 정의
    public void timeTraceClass() {}

    @Around("timeTraceMethod() || timeTraceClass()")
    public Object traceTime(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        // 메서드 또는 클래스 레벨의 @TimeTrace 애노테이션 가져오기
        TimeTrace timeTrace = method.getAnnotation(TimeTrace.class);
        if (timeTrace == null) {
            timeTrace = joinPoint.getTarget().getClass().getAnnotation(TimeTrace.class);
        }
        try {
            stopWatch.start();
            log.info("{} : {} ",timeTrace.startMessage(), joinPoint.getSignature().toShortString());
            return joinPoint.proceed(); // 실제 타겟 호출
        } finally {
            stopWatch.stop();
            log.info("{} : {} = {}s",
                    timeTrace.endMessage(),
                    joinPoint.getSignature().toShortString(),
                    stopWatch.getTotalTimeSeconds());
        }
    }
}
