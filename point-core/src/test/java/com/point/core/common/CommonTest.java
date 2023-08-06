package com.point.core.common;

import com.point.core.common.facade.PointFacade;
import com.point.core.user.repository.UserRepository;
import com.point.core.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class CommonTest {

    @Autowired
    private PointFacade pointFacade;

    @Autowired
    protected UserService userService;

    @Autowired
    private UserRepository userRepository;

    protected final Long globalUserId = 1L;
    protected final Long globalPoint = 10_000L;

//    @BeforeEach
//    @DisplayName("회원 가입 및 포인트 적립")ㄹ
//    void setUp() {
//        User user = User.builder()
//                .userId(globalUserId)
//                .remainPoint(globalPoint)
//                .build();
//
//        userService.save(user);
//    }
//
//    @BeforeEach
//    @DisplayName("회원 가입 및 포인트 적립")
//    void setUp2() {
//        User user = User.builder().remainPoint(0L).build();
//        User savedUser = userRepository.save(user);
//
//        EarnPointRequest request = EarnPointRequest.builder().savePoint(10_000L).build();
//        pointFacade.earnPoint(savedUser.getUserId(), request);
//    }
}
