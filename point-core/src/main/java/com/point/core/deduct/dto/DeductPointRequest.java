package com.point.core.deduct.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeductPointRequest {

    @Positive(message = "차감 포인트는 0보다 커야 합니다.")
    private Long deductPoint;

    @Builder
    private DeductPointRequest(Long deductPoint) {
        this.deductPoint = deductPoint;
    }

    public static DeductPointRequest of(Long deductPoint) {
        return DeductPointRequest.builder()
            .deductPoint(deductPoint)
            .build();
    }

}
