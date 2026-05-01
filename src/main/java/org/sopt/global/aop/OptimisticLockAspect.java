package org.sopt.global.aop;

import jakarta.persistence.OptimisticLockException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.sopt.global.api.code.CommonErrorCode;
import org.sopt.global.exception.CustomException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Aspect
@Component
@Order(Ordered.LOWEST_PRECEDENCE - 1)
public class OptimisticLockAspect {

    private static final Logger log = Logger.getLogger(OptimisticLockAspect.class.getName());

    @Around("@annotation(optimisticLock)")
    public Object lock(final ProceedingJoinPoint joinPoint, OptimisticLock optimisticLock) throws Throwable {

        int retryCount = optimisticLock.retryCount();
        long waitTime = optimisticLock.waitTime();
        String methodName = joinPoint.getSignature().getName();

        for (int i = 0; i < retryCount; i++) {
            try {
                return joinPoint.proceed();
            } catch (OptimisticLockException | ObjectOptimisticLockingFailureException e) {
                log.warning(String.format("[OptimisticLock] 충돌 감지 - method: %s, 시도: %d/%d", methodName, i + 1, retryCount));
                Thread.sleep(waitTime);
            }
        }

        log.severe(String.format("[OptimisticLock] 최종 실패 - method: %s, 재시도 횟수: %d", methodName, retryCount));
        throw new CustomException(CommonErrorCode.OPTIMISTIC_LOCK_CONFLICT);
    }
}