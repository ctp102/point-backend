//package com.point.core.common.facade;
//
//import com.point.core.earn.dto.EarnPointRequest;
//import com.point.core.user.domain.User;
//import com.point.core.user.service.UserService;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//@SpringBootTest
//@Transactional
//public class PointRedissonFacadeTest {
//
//    @Autowired
//    private PointRedissonFacade pointRedissonFacade;
//
//    @Autowired
//    private UserService userService;
//
//    @Test
//    void earnPointTest() {
//        Long userId = 1L;
//        Long defaultPoint = 10_000L;
//        Long savePoint = 5_000L; // 적립할 포인트
//
//        EarnPointRequest request = EarnPointRequest.of(savePoint);
//        pointRedissonFacade.earnPoint(userId, request);
//
//        User foundUser = userService.findUser(userId);
//
//        Assertions.assertThat(foundUser.getRemainPoint()).isEqualTo(defaultPoint + savePoint);
//    }
//
////    @Test
////    @DisplayName("포인트 적립 - 동시에 100 포인트 씩 100번 요청")
////    void earnPointWithConcurrencyTest() throws InterruptedException {
////        Long userId = 1L;
////        Long earnPoint = 100L; // 적립할 포인트
////
////        int threadCount = 100;
////        ExecutorService executor = Executors.newFixedThreadPool(32);
////        CountDownLatch latch = new CountDownLatch(threadCount);
////
////        EarnPointRequest request = new EarnPointRequest(earnPoint);
////        for (int i = 0; i < threadCount; i++) {
////            executor.execute(() -> {
////                try {
////                    pointRedissonFacade.earnPoint(userId, request);
////                } finally {
////                    latch.countDown();
////                }
////            });
////        }
////
////        latch.await();
////
////        User foundUser = userService.findUser(userId);
////
////        Assertions.assertThat(foundUser.getRemainPoint()).isEqualTo(10000L + (100 * 100L)); // 20000 포인트
////    }
//
//}
