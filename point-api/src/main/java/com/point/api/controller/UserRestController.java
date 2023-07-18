package com.point.api.controller;

import com.point.api.common.exception.CustomBadRequestException;
import com.point.core.common.response.CustomResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserRestController {

//    private final UserService userService;

    @GetMapping("/")
    public CustomResponse test() {
//        userService.invokeError();
        throw new CustomBadRequestException(1001, "CustomBadRequestException");

    }

}
