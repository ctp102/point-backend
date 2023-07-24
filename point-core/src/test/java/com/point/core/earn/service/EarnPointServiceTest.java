package com.point.core.earn.service;

import com.point.core.common.CommonTest;
import com.point.core.earn.domain.EarnPoint;
import com.point.core.user.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class EarnPointServiceTest extends CommonTest {

    @Autowired
    private EarnPointService earnPointService;

    @Test
    @DisplayName("회원별 포인트 적립")
    void earnPointTest() {
        // given
        Long savePoint = 5_000L;

        User foundUser = userService.findUser(globalUserId);
        EarnPoint earnPoint = EarnPoint.of(foundUser, savePoint);

        // when
        EarnPoint savedEarnPoint = earnPointService.earn(earnPoint);

        // then
        Assertions.assertThat(savedEarnPoint.getEarnPointId()).isNotNull();
        Assertions.assertThat(savedEarnPoint.getUser().getUserId()).isEqualTo(globalUserId);
        Assertions.assertThat(savedEarnPoint.getSavePoint()).isEqualTo(savePoint);
    }

}
