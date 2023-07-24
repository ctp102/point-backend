package com.point.core.common;

import com.point.core.user.domain.User;
import com.point.core.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class CommonTest {

    @Autowired
    protected UserService userService;

    protected final Long globalUserId = 1L;
    protected final Long globalPoint = 10_000L;

    @BeforeEach
    @DisplayName("회원 가입 및 포인트 적립")
    void setUp() {
        User user = User.builder()
                .userId(globalUserId)
                .remainPoint(globalPoint)
                .build();

        userService.save(user);
    }

}
