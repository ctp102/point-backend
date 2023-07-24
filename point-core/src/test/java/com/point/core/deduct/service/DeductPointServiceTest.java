package com.point.core.deduct.service;

import com.point.core.common.CommonTest;
import com.point.core.common.exception.CustomAccessDeniedException;
import com.point.core.deduct.domain.DeductPoint;
import com.point.core.user.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class DeductPointServiceTest extends CommonTest {

    @Autowired
    private DeductPointService deductPointService;

    @Test
    @DisplayName("회원별 포인트 차감")
    void deductPointTest() {
        // given
        Long _deductPoint = 5_000L;
        User foundUser = userService.findUser(globalUserId);
        DeductPoint deductPoint = DeductPoint.of(foundUser, _deductPoint);

        // when
        DeductPoint savedDeductPoint = deductPointService.deduct(deductPoint);

        // then
        Assertions.assertThat(savedDeductPoint.getDeductPointId()).isNotNull();
        Assertions.assertThat(savedDeductPoint.getUser().getUserId()).isEqualTo(globalUserId);
        Assertions.assertThat(savedDeductPoint.getDeductPoint()).isEqualTo(_deductPoint);
    }

    @Test
    @DisplayName("회원별 포인트 차감 취소")
    void cancelDeductPointTest() {
        // given
        Long cancelPoint = 5_000L;
        Long _deductPoint = 5_000L;
        User foundUser = userService.findUser(globalUserId);
        DeductPoint deductPoint = DeductPoint.of(foundUser, _deductPoint);
        deductPointService.deduct(deductPoint);

        // when & then
        Assertions.assertThatThrownBy(() -> deductPointService.cancelDeduct(globalUserId, cancelPoint))
                .isInstanceOf(CustomAccessDeniedException.class)
                .hasMessageContaining("차감 취소할 포인트가 부족합니다.");
    }

}
