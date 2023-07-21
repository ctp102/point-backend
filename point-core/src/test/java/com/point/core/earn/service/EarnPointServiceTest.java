package com.point.core.earn.service;

import com.point.core.user.domain.User;
import com.point.core.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Slf4j
public class EarnPointServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private EarnPointService earnPointService;

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

}
