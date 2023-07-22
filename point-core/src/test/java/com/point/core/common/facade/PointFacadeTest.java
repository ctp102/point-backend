package com.point.core.common.facade;

import com.point.core.deduct.dto.DeductPointRequest;
import com.point.core.earn.dto.EarnPointRequest;
import com.point.core.user.domain.User;
import com.point.core.user.dto.UserDto;
import com.point.core.user.service.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@SqlGroup({
        @Sql(value = "/sql/default-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
public class PointFacadeTest {

    @Autowired
    private PointFacade pointFacade;

    @Autowired
    private UserService userService;

    @Test
    @DisplayName("사용자 포인트 합계 조회")
    void getUserPointTest() {
        Long userId = 1L;

        UserDto userDto = pointFacade.findUser(userId);

        assertThat(userDto.getUserId()).isEqualTo(userId);
        assertThat(userDto.getRemainPoint()).isEqualTo(10_000L);
    }

    @Test
    @DisplayName("사용자 포인트 사용 내역 조회 - 적립/획득/차감/만료")
    void getPointHistoryListTest() {

    }

    @Test
    @DisplayName("포인트 적립")
    void earnPointTest() {
        Long userId = 1L;
        Long defaultPoint = 10_000L;
        Long savePoint = 5_000L; // 적립할 포인트

        EarnPointRequest request = new EarnPointRequest(savePoint);
        pointFacade.earnPoint(userId, request);

        User foundUser = userService.findUser(userId);

        assertThat(foundUser.getRemainPoint()).isEqualTo(defaultPoint + savePoint);
    }

    @Test
    @DisplayName("포인트 차감")
    void deductPointTest() {
        Long deductPoint = 5_000L; // 차감할 포인트

    }

    @Test
    @DisplayName("포인트 차감 취소")
    void cancelDeductPointTest() {
        Long cancelPoint = 5_000L; // 차감 취소할 포인트
    }

    @Test
    @DisplayName("포인트 차감 - 동시에 100개의 요청(100 포인트씩 100번 차감하므로 0이 되어야 함)")
    void deductPointWithConcurrencyTest() throws InterruptedException {
        Long userId = 1L;
        Long deductPoint = 100L; // 차감할 포인트

        int threadCount = 100;
        ExecutorService executor = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        DeductPointRequest request = new DeductPointRequest(deductPoint);
        for (int i = 0; i < threadCount; i++) {
            executor.execute(() -> {
                try {
                    pointFacade.deductPoint(userId, request);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        User foundUser = userService.findUser(userId);

        Assertions.assertThat(foundUser.getRemainPoint()).isEqualTo(0L);
    }

}
