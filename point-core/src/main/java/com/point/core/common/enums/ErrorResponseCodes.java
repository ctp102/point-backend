package com.point.core.common.enums;

import com.point.core.common.response.CustomResponseCodes;
import lombok.Getter;

import java.util.Arrays;

public enum ErrorResponseCodes implements CustomResponseCodes {

    NOT_ENOUGH_POINT(403, "잔여 포인트가 부족합니다.")
    ;

    @Getter
    private final int number;

    @Getter
    private final String message;

    ErrorResponseCodes(int number, String message) {
        this.number = number;
        this.message = message;
    }

    @Override
    public String getCode() {
        return this.name();
    }

    public static ErrorResponseCodes findByMessage(String message) {
        return Arrays.stream(ErrorResponseCodes.values())
                .filter(e -> e.getMessage().equals(message))
                .findFirst()
                .orElse(null);
    }

}
