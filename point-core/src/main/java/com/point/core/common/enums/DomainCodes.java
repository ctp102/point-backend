package com.point.core.common.enums;

import lombok.Getter;

@Getter
public enum DomainCodes {

    POINT_ACTION_TYPE("1001000", "포인트"),
    POINT_EARN("1001001", "포인트 획득"),
    POINT_DEDUCT("1001002", "포인트 차감"),
    POINT_DEDUCT_CANCEL("1001003", "포인트 차감 취소")
    ;

    DomainCodes(String dcd, String message) {
        this.dcd = dcd;
        this.message = message;
    }

    private final String dcd;
    private final String message;

}
