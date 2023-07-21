package com.point.core.earn.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EarnPointRequest {

    private Long savePoint; // 적립 포인트

    public EarnPointRequest(Long savePoint) {
        this.savePoint = savePoint;
    }

}
