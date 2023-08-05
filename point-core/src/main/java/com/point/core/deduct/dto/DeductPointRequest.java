package com.point.core.deduct.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeductPointRequest {

    @Positive(message = "차감 포인트는 0보다 커야 합니다.")
    private Long deductPoint;

    public DeductPointRequest(Long deductPoint) {
        this.deductPoint = deductPoint;
    }

}
