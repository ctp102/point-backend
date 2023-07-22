package com.point.api.controller;

import com.point.core.common.facade.PointFacade;
import com.point.core.common.response.CustomResponse;
import com.point.core.earn.dto.EarnPointRequest;
import com.point.core.user.domain.User;
import com.point.core.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserRestController {

    private final UserRepository userRepository;
    private final PointFacade pointFacade;

    @PostMapping("/api/v1/users")
    public CustomResponse saveUser(@RequestBody EarnPointRequest request) {
        User user = User.builder()
                .remainPoint(0L).build();

        // 1. 가입을 먼저 시킨다.
        User savedUser = userRepository.save(user);

        // 2. 포인트 지급
        pointFacade.earnPoint(savedUser.getUserId(), request);

        return new CustomResponse.Builder().build();
    }

}
