package com.point.core.common.enums;

import lombok.Getter;

@Getter
public enum DomainCodes {

    POINT_ACTION_TYPE("1001000", "포인트 행위 타입"),
    POINT_EARN("1001001", "포인트 획득"),
    POINT_DEDUCT("1001002", "포인트 차감"),
    POINT_DEDUCT_CANCEL("1001003", "포인트 차감 취소"),

    POINT_DEDUCT_STATUS("1002000", "포인트 차감 상태"),
    POINT_DEDUCT_ST_ALL("1002001", "포인트 전체 차감"),
    POINT_DEDUCT_ST_PART("1002002", "포인트 부분 차감"),
    POINT_DEDUCT_ST_AVAILABLE("1002003", "포인트 미사용")
    ;

    DomainCodes(String dcd, String message) {
        this.dcd = dcd;
        this.message = message;
    }

    private final String dcd;
    private final String message;

}
