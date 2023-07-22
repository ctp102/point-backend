package com.point.core.deduct.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeductPointRequest {

    private Long deductPoint;

    public DeductPointRequest(Long deductPoint) {
        this.deductPoint = deductPoint;
    }

}
