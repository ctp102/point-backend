package com.point.api.controller;

import com.point.core.common.enums.ErrorResponseCodes;
import com.point.core.common.exception.CustomBadRequestException;
import com.point.core.common.response.CustomResponse;
import com.point.core.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;

    @GetMapping("/test")
    public CustomResponse test() {
        throw new CustomBadRequestException(ErrorResponseCodes.NOT_ENOUGH_POINT.getNumber(), ErrorResponseCodes.NOT_ENOUGH_POINT.getMessage());
    }

    @GetMapping("/test2")
    public CustomResponse test2() {
        userService.invokeError();
        return new CustomResponse.Builder().build();
    }

}
