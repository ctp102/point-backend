package com.point.core.deduct.service;

import com.point.core.deduct.repository.DeductPointRepository;
import com.point.core.earn.repository.EarnPointRepository;
import com.point.core.history.repository.PointHistoryRepository;
import com.point.core.user.repository.UserRepository;
import com.point.core.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class DeductPointServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private DeductPointService deductPointService;

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

//    @Test
//    @DisplayName("회원별 포인트 차감를 차감한다")
//    void deductPointTest() {
//        // given
//        Long _deductPoint = 5_000L;
//        User foundUser = userService.findUser(1L);
//        DeductPoint deductPoint = DeductPoint.of(foundUser, _deductPoint);
//
//        // when
//        DeductPoint savedDeductPoint = deductPointService.deduct(deductPoint);
//
//        // then
//        User user = userService.findUser(1L);
//
//        assertThat(savedDeductPoint.getDeductPointId()).isNotNull();
//        assertThat(savedDeductPoint.getUser().getUserId()).isEqualTo(globalUserId);
//        assertThat(savedDeductPoint.getDeductPoint()).isEqualTo(_deductPoint);
//        assertThat(user.getRemainPoint()).isEqualTo(foundUser.getRemainPoint() - _deductPoint);
//    }



//    @Test
//    @DisplayName("회원별 포인트를 차감 취소한다")
//    void cancelDeductPointTest() {
//        // given
//        Long cancelPoint = 5_000L;
//        Long _deductPoint = 5_000L;
//        User foundUser = userService.findUser(globalUserId);
//        DeductPoint deductPoint = DeductPoint.of(foundUser, _deductPoint);
//        deductPointService.deduct(deductPoint);
//
//        // when & then
//        assertThatThrownBy(() -> deductPointService.cancelDeduct(globalUserId, cancelPoint))
//                .isInstanceOf(CustomAccessDeniedException.class)
//                .hasMessageContaining("차감 취소할 포인트가 부족합니다.");
//    }

}
