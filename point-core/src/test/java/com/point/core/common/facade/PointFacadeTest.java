package com.point.core.common.facade;

import com.point.core.common.enums.DomainCodes;
import com.point.core.deduct.dto.CancelDeductPointRequest;
import com.point.core.deduct.dto.DeductPointRequest;
import com.point.core.deduct.repository.DeductPointRepository;
import com.point.core.earn.dto.EarnPointRequest;
import com.point.core.earn.repository.EarnPointRepository;
import com.point.core.history.dto.PointHistoryDto;
import com.point.core.history.form.PointHistoryForm;
import com.point.core.history.repository.PointHistoryRepository;
import com.point.core.user.domain.User;
import com.point.core.user.dto.UserDto;
import com.point.core.user.repository.UserRepository;
import com.point.core.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
public class PointFacadeTest {

    @Autowired
    private PointFacade pointFacade;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EarnPointRepository earnPointRepository;

    @Autowired
    private DeductPointRepository deductPointRepository;

    @Autowired
    private PointHistoryRepository pointHistoryRepository;

    @AfterEach
    void tearDown() {
        pointHistoryRepository.deleteAllInBatch();
        earnPointRepository.deleteAllInBatch();
        deductPointRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("회원의 보유 포인트를 조회한다.")
    void findUser() {
        // given
        User user = User.builder().remainPoint(0L).build();
        User savedUser = userRepository.save(user);

        EarnPointRequest earnPointRequest = EarnPointRequest.builder().savePoint(10_000L).build();
        pointFacade.earnPoint(savedUser.getUserId(), earnPointRequest);

        // when
        UserDto userDto = pointFacade.findUser(savedUser.getUserId());

        // then
        assertThat(userDto.getRemainPoint()).isEqualTo(10_000L);
    }

    @Test
    @DisplayName("회원별 포인트 적립/차감/취소 내역을 조회한다.")
    void findPointHistoryList() {
        // given
        User user = User.builder().remainPoint(0L).build();
        User savedUser = userRepository.save(user);

        EarnPointRequest earnPointRequest = EarnPointRequest.builder().savePoint(10_000L).build();
        pointFacade.earnPoint(savedUser.getUserId(), earnPointRequest);

        DeductPointRequest deductPointRequest = DeductPointRequest.of(5_000L);
        pointFacade.deductPoint(savedUser.getUserId(), deductPointRequest);

        CancelDeductPointRequest cancelDeductPointRequest = CancelDeductPointRequest.of(5_000L);
        pointFacade.cancelDeductPoint(savedUser.getUserId(), cancelDeductPointRequest);

        PointHistoryForm pointHistoryForm1 = PointHistoryForm.of(DomainCodes.POINT_EARN);
        PointHistoryForm pointHistoryForm2 = PointHistoryForm.of(DomainCodes.POINT_DEDUCT);
        PointHistoryForm pointHistoryForm3 = PointHistoryForm.of(DomainCodes.POINT_DEDUCT_CANCEL);

        // when
        Page<PointHistoryDto> pointHistoryList1 = pointFacade.findPointHistoryList(savedUser.getUserId(), pointHistoryForm1, PageRequest.of(0, 10));
        Page<PointHistoryDto> pointHistoryList2 = pointFacade.findPointHistoryList(savedUser.getUserId(), pointHistoryForm2, PageRequest.of(0, 10));
        Page<PointHistoryDto> pointHistoryList3 = pointFacade.findPointHistoryList(savedUser.getUserId(), pointHistoryForm3, PageRequest.of(0, 10));

        // then
        assertThat(pointHistoryList1.getTotalElements()).isEqualTo(1);
        assertThat(pointHistoryList1.getContent().get(0).getPoint()).isEqualTo(10_000L);
        assertThat(pointHistoryList1.getContent().get(0).getPointActionType()).isEqualTo(DomainCodes.POINT_EARN);

        assertThat(pointHistoryList2.getTotalElements()).isEqualTo(1);
        assertThat(pointHistoryList2.getContent().get(0).getPoint()).isEqualTo(5_000L);
        assertThat(pointHistoryList2.getContent().get(0).getPointActionType()).isEqualTo(DomainCodes.POINT_DEDUCT);

        assertThat(pointHistoryList3.getTotalElements()).isEqualTo(1);
        assertThat(pointHistoryList3.getContent().get(0).getPoint()).isEqualTo(5_000L);
        assertThat(pointHistoryList3.getContent().get(0).getPointActionType()).isEqualTo(DomainCodes.POINT_DEDUCT_CANCEL);
    }

    @Test
    @DisplayName("회원에게 포인트를 적립한다.")
    void earnPoint() {
        // given
        User user = User.builder().remainPoint(0L).build();
        User savedUser = userRepository.save(user);

        EarnPointRequest earnPointRequest = EarnPointRequest.builder().savePoint(10_000L).build();

        // when
        pointFacade.earnPoint(savedUser.getUserId(), earnPointRequest);

        // then
        User userAfterDeduct = userService.findUser(savedUser.getUserId());

        assertThat(userAfterDeduct.getRemainPoint()).isEqualTo(10_000L);
    }

    @Test
    @DisplayName("회원의 포인트를 차감한다")
    void deductPoint() {
        // given
        User user = User.builder().remainPoint(0L).build();
        User savedUser = userRepository.save(user);

        EarnPointRequest earnPointRequest = EarnPointRequest.builder().savePoint(10_000L).build();
        pointFacade.earnPoint(savedUser.getUserId(), earnPointRequest);

        DeductPointRequest deductPointRequest = DeductPointRequest.of(5_000L);

        // when
        pointFacade.deductPoint(savedUser.getUserId(), deductPointRequest);

        // then
        User userAfterDeduct = userService.findUser(savedUser.getUserId());
        assertThat(userAfterDeduct.getRemainPoint()).isEqualTo(5_000L);
    }

    @Test
    @DisplayName("동시에 100번 100 포인트씩 차감한다. 초기에는 10_000 포인트가 지급된다.")
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
        User userAfterDeduct = userService.findUser(savedUser.getUserId());

        assertThat(userAfterDeduct.getRemainPoint()).isEqualTo(0L);
    }

    @Test
    @DisplayName("차감한 포인트를 취소한다.")
    void cancelDeductPoint() {
        // given
        User user = User.builder().remainPoint(0L).build();
        User savedUser = userRepository.save(user);

        EarnPointRequest earnPointRequest = EarnPointRequest.builder().savePoint(10_000L).build();
        pointFacade.earnPoint(savedUser.getUserId(), earnPointRequest);

        DeductPointRequest deductPointRequest = DeductPointRequest.of(5_000L);
        pointFacade.deductPoint(savedUser.getUserId(), deductPointRequest);

        // when
        CancelDeductPointRequest cancelDeductPointRequest = CancelDeductPointRequest.of(5_000L);
        pointFacade.cancelDeductPoint(savedUser.getUserId(), cancelDeductPointRequest);

        // then
        User userAfterDeduct = userService.findUser(savedUser.getUserId());
        assertThat(userAfterDeduct.getRemainPoint()).isEqualTo(10_000L);
    }

}
