package com.point.core.user.service;

import com.point.core.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class UserServiceTest {

    @Autowired
    private UserService userService;

    private final Long globalUserId = 1L;
    private final Long globalPoint = 10_000L;

    @BeforeEach
    @DisplayName("사용자 및 포인트 지급")
    void setUp() {
        User user = User.builder()
                .userId(globalUserId)
                .remainPoint(globalPoint)
                .build();

        userService.save(user);
    }

    @Test
    @DisplayName("사용자 보유 포인트 조회")
    void findUser() {
        User foundUser = userService.findUser(globalUserId);

        assertThat(foundUser.getUserId()).isEqualTo(globalUserId);
        assertThat(foundUser.getRemainPoint()).isEqualTo(globalPoint);
    }

}
