package com.point.api.controller;

import com.point.core.common.facade.PointFacade;
import com.point.core.common.response.CustomResponse;
import com.point.core.earn.dto.EarnPointRequest;
import com.point.core.user.domain.User;
import com.point.core.user.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "UserRestController")
@RestController
@RequiredArgsConstructor
public class UserRestController {

    private final UserRepository userRepository;
    private final PointFacade pointFacade;

    @Operation(summary = "회원 가입 및 초기 포인트 적립")
    @ApiResponse(responseCode = "200", description = "API 요청 성공", content = {
            @Content(
                    schema = @Schema(implementation = CustomResponse.class),
                    examples = {
                        @ExampleObject(value = "{\"status\":{\"number\":200,\"code\":\"OK\",\"message\":\"OK\"},\"data\":{}}")
                    }
            )
    })
    @PostMapping("/api/v1/users")
    public CustomResponse saveUser(@RequestBody EarnPointRequest request) {
        User user = User.builder()
                .remainPoint(0L).build();

        // 1. 가입을 먼저 시킨다.
        User savedUser = userRepository.save(user);

        // 2. 포인트 적립
        pointFacade.earnPoint(savedUser.getUserId(), request);

        return new CustomResponse.Builder().build();
    }

}
