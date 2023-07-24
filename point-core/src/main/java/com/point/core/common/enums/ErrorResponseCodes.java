package com.point.core.common.enums;

import com.point.core.common.response.CustomResponseCodes;
import lombok.Getter;

import java.util.Arrays;

public enum ErrorResponseCodes implements CustomResponseCodes {

    NOT_ENOUGH_POINT                    (403, "잔여 포인트가 부족합니다."),
    NOT_ENOUGH_CANCEL_DEDUCT_POINT      (403, "차감 취소할 포인트가 부족합니다."),

    USER_NOT_FOUND                      (404, "사용자를 찾을 수 없습니다."),

    LOCK_TRY_TIMEOUT                    (409, "Lock 획득에 실패했습니다."),

    INTERNAL_SERVER_ERROR               (500, "예상치 못한 에러가 발생했습니다.")
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
