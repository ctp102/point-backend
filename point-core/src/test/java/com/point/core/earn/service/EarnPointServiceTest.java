//package com.point.core.earn.service;
//
//import com.point.core.earn.domain.EarnPoint;
//import com.point.core.earn.dto.EarnPointRequest;
//import com.point.core.user.domain.User;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//@SpringBootTest
//public class EarnPointServiceTest {
//
//    @Autowired
//    private EarnPointService earnPointService;
//
//    @Test
//    @DisplayName("회원별 포인트 적립")
//    void earnPointTest() {
//        // given
//        User user = User.builder().remainPoint(0L).build();
//        User savedUser = userRepository.save(user);
//
//        EarnPointRequest earnPointRequest = EarnPointRequest.builder().savePoint(10_000L).build();
//        pointFacade.earnPoint(savedUser.getUserId(), earnPointRequest);
//
//        Long savePoint = 5_000L;
//
//        User foundUser = userService.findUser(globalUserId);
//        EarnPoint earnPoint = EarnPoint.of(foundUser, savePoint);
//
//        // when
//        EarnPoint savedEarnPoint = earnPointService.earn(earnPoint);
//
//        // then
//        Assertions.assertThat(savedEarnPoint.getEarnPointId()).isNotNull();
//        Assertions.assertThat(savedEarnPoint.getUser().getUserId()).isEqualTo(globalUserId);
//        Assertions.assertThat(savedEarnPoint.getSavePoint()).isEqualTo(savePoint);
//    }
//
//}
