package com.point.core.common.facade;

import com.point.core.earn.dto.EarnPointRequest;
import com.point.core.user.domain.User;
import com.point.core.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class PointFacadeTest {

    @Autowired
    private PointFacade pointFacade;

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
    @DisplayName("포인트 적립")
    void earnPoint() {
        Long savePoint = 5_000L; // 적립할 포인트

        EarnPointRequest request = new EarnPointRequest(savePoint);
        pointFacade.earnPoint(globalUserId, request);

        User foundUser = userService.findUser(globalUserId);

        assertThat(foundUser.getRemainPoint()).isEqualTo(globalPoint + savePoint);
    }


}
