package com.point.core.deduct.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Positive;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CancelDeductPointRequest {

    @Positive(message = "차감 취소 포인트는 0보다 커야 합니다.")
    private Long cancelPoint;

    public CancelDeductPointRequest(Long cancelPoint) {
        this.cancelPoint = cancelPoint;
    }

}
