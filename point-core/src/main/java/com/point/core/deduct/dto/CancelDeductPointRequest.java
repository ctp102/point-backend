package com.point.core.deduct.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CancelDeductPointRequest {

    private Long cancelPoint;

    public CancelDeductPointRequest(Long cancelPoint) {
        this.cancelPoint = cancelPoint;
    }

}
