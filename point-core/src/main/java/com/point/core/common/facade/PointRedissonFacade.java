//package com.point.core.common.facade;
//
//import com.point.core.common.config.properties.RedissonProperties;
//import com.point.core.common.enums.DomainCodes;
//import com.point.core.common.exception.CustomConflictException;
//import com.point.core.deduct.domain.DeductPoint;
//import com.point.core.deduct.dto.CancelDeductPointRequest;
//import com.point.core.deduct.dto.DeductPointRequest;
//import com.point.core.earn.domain.EarnPoint;
//import com.point.core.earn.dto.EarnPointRequest;
//import lombok.extern.slf4j.Slf4j;
//import org.redisson.api.RLock;
//import org.redisson.api.RedissonClient;
//import org.springframework.stereotype.Component;
//
//import java.util.concurrent.TimeUnit;
//
//import static com.point.core.common.enums.ErrorResponseCodes.LOCK_TRY_TIMEOUT;
//
//@Component
//@Slf4j
//@Deprecated
//public class PointRedissonFacade {
//
//    private final RedissonClient redissonClient;
//    private final Long lockWaitTime;
//    private final Long lockLeaseTime;
//    private final PointFacade pointFacade;
//
//    public PointRedissonFacade(RedissonClient redissonClient, RedissonProperties redissonProperties, PointFacade pointFacade) {
//        this.redissonClient = redissonClient;
//        this.lockWaitTime = redissonProperties.getWaitTime();
//        this.lockLeaseTime = redissonProperties.getLeaseTime();
//        this.pointFacade = pointFacade;
//    }
//
//    public EarnPoint earnPoint(Long userId, EarnPointRequest request) {
//        RLock lock = getLock(userId, DomainCodes.POINT_EARN);
//
//        try {
//            boolean available = lock.tryLock(lockWaitTime, lockLeaseTime, TimeUnit.MILLISECONDS);
//            if (!available) {
//                log.error("[earnPoint] lock 획득 실패, wait time = {} milliseconds", lockWaitTime);
//                throw new CustomConflictException(LOCK_TRY_TIMEOUT.getNumber(), LOCK_TRY_TIMEOUT.getMessage());
//            }
//            return pointFacade.earnPoint(userId, request);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        } finally {
//            lock.unlock();
//        }
//
//    }
//
//    public DeductPoint deductPoint(Long userId, DeductPointRequest request) {
//        RLock lock = getLock(userId, DomainCodes.POINT_DEDUCT);
//
//        try {
//            boolean available = lock.tryLock(lockWaitTime, lockLeaseTime, TimeUnit.MILLISECONDS);
//            if (!available) {
//                log.error("[deductPoint] lock 획득 실패, wait time = {} milliseconds", lockWaitTime);
//                throw new CustomConflictException(LOCK_TRY_TIMEOUT.getNumber(), LOCK_TRY_TIMEOUT.getMessage());
//            }
//            return pointFacade.deductPoint(userId, request);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        } finally {
//            lock.unlock();
//        }
//    }
//
//    public void cancelDeductPoint(Long userId, CancelDeductPointRequest request) {
//        RLock lock = getLock(userId, DomainCodes.POINT_DEDUCT_CANCEL);
//
//        try {
//            boolean available = lock.tryLock(lockWaitTime, lockLeaseTime, TimeUnit.MILLISECONDS);
//            if (!available) {
//                log.error("[cancelDeductPoint] lock 획득 실패, wait time = {} milliseconds", lockWaitTime);
//                throw new CustomConflictException(LOCK_TRY_TIMEOUT.getNumber(), LOCK_TRY_TIMEOUT.getMessage());
//            }
//            pointFacade.cancelDeductPoint(userId, request);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        } finally {
//            lock.unlock();
//        }
//    }
//
//    private RLock getLock(Long userId, DomainCodes pointActionType) {
//        String lockName = String.format("%s%s%d", pointActionType.name(), ":", userId);
//        return redissonClient.getLock(lockName);
//    }
//
//}
