package com.point.core.deduct.dto;

import lombok.*;

import javax.validation.constraints.Positive;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CancelDeductPointRequest {

    @Positive(message = "차감 취소 포인트는 0보다 커야 합니다.")
    private Long cancelPoint;

    @Builder
    private CancelDeductPointRequest(Long cancelPoint) {
        this.cancelPoint = cancelPoint;
    }

    public static CancelDeductPointRequest of(Long cancelPoint) {
        return CancelDeductPointRequest.builder()
                .cancelPoint(cancelPoint)
                .build();
    }

}
