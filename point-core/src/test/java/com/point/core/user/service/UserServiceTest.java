package com.point.core.user.service;

import com.point.core.common.facade.PointFacade;
import com.point.core.earn.dto.EarnPointRequest;
import com.point.core.user.domain.User;
import com.point.core.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private PointFacade pointFacade;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("사용자 보유 포인트 조회")
    void findUserTest() {
        User user = User.builder().remainPoint(0L).build();
        User savedUser = userRepository.save(user);

        EarnPointRequest earnPointRequest = EarnPointRequest.builder().savePoint(10_000L).build();
        pointFacade.earnPoint(savedUser.getUserId(), earnPointRequest);

        // when
        User foundUser = userService.findUser(savedUser.getUserId());

        // then
        assertThat(foundUser.getUserId()).isEqualTo(savedUser.getUserId());
        assertThat(foundUser.getRemainPoint()).isEqualTo(10_000L);
    }

}
