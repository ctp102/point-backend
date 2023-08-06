package com.point.core.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserDto {

    private final Long userId;
    private final Long remainPoint;

    @Builder
    private UserDto(Long userId, Long remainPoint) {
        this.userId = userId;
        this.remainPoint = remainPoint;
    }

    public static UserDto of(Long userId, Long remainPoint) {
        return UserDto.builder()
                .userId(userId)
                .remainPoint(remainPoint)
                .build();
    }

}
