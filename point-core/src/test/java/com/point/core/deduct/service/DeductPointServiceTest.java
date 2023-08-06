package com.point.core.deduct.service;

import com.point.core.common.CommonTest;
import com.point.core.common.exception.CustomAccessDeniedException;
import com.point.core.common.facade.PointFacade;
import com.point.core.deduct.domain.DeductPoint;
import com.point.core.deduct.dto.DeductPointRequest;
import com.point.core.earn.dto.EarnPointRequest;
import com.point.core.user.domain.User;
import com.point.core.user.repository.UserRepository;
import com.point.core.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
public class DeductPointServiceTest extends CommonTest {

    @Autowired
    private PointFacade pointFacade;

    @Autowired
    private UserService userService;

    @Autowired
    private DeductPointService deductPointService;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("회원별 포인트 차감를 차감한다")
    void deductPointTest() {
        // given
        Long _deductPoint = 5_000L;
        User foundUser = userService.findUser(globalUserId);
        DeductPoint deductPoint = DeductPoint.of(foundUser, _deductPoint);

        // when
        DeductPoint savedDeductPoint = deductPointService.deduct(deductPoint);

        // then
        User user = userService.findUser(1L);

        assertThat(savedDeductPoint.getDeductPointId()).isNotNull();
        assertThat(savedDeductPoint.getUser().getUserId()).isEqualTo(globalUserId);
        assertThat(savedDeductPoint.getDeductPoint()).isEqualTo(_deductPoint);
        assertThat(user.getRemainPoint()).isEqualTo(foundUser.getRemainPoint() - _deductPoint);
    }

    @Test
    @DisplayName("100 포인트씩 동시에 100번을 차감 시도한다. 초기에 10_000 포인트가 지급된다.")
    void deductPointConcurrency() throws InterruptedException {
        // given
        User user = User.builder().remainPoint(0L).build();
        User savedUser = userRepository.save(user);

        EarnPointRequest earnPointRequest = EarnPointRequest.builder().savePoint(10_000L).build();
        pointFacade.earnPoint(savedUser.getUserId(), earnPointRequest);

        DeductPointRequest deductPointRequest = DeductPointRequest.of(100L);

        int threadCount = 100;
        ExecutorService executor = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        // when
        for (int i = 0; i < threadCount; i++) {
            executor.execute(() -> {
                try {
                    pointFacade.deductPoint(savedUser.getUserId(), deductPointRequest);
                } catch (Exception e) {
                    log.error("error", e);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        // then
        User userAfterDeduct = userService.findUser(1L);

        assertThat(userAfterDeduct.getRemainPoint()).isEqualTo(0L);
    }

    @Test
    @DisplayName("회원별 포인트를 차감 취소한다")
    void cancelDeductPointTest() {
        // given
        Long cancelPoint = 5_000L;
        Long _deductPoint = 5_000L;
        User foundUser = userService.findUser(globalUserId);
        DeductPoint deductPoint = DeductPoint.of(foundUser, _deductPoint);
        deductPointService.deduct(deductPoint);

        // when & then
        assertThatThrownBy(() -> deductPointService.cancelDeduct(globalUserId, cancelPoint))
                .isInstanceOf(CustomAccessDeniedException.class)
                .hasMessageContaining("차감 취소할 포인트가 부족합니다.");
    }

}
