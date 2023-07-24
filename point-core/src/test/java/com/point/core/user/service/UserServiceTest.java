package com.point.core.user.service;

import com.point.core.common.CommonTest;
import com.point.core.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserServiceTest extends CommonTest {

    @Test
    @DisplayName("사용자 보유 포인트 조회")
    void findUserTest() {

        // when
        User foundUser = userService.findUser(globalUserId);

        // then
        assertThat(foundUser.getUserId()).isEqualTo(globalUserId);
        assertThat(foundUser.getRemainPoint()).isEqualTo(globalPoint);
    }

}
