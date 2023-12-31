package com.point.core.earn.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EarnPointRequest {

    @Positive(message = "적립 포인트는 0보다 커야 합니다.")
    private Long savePoint; // 적립 포인트

    @Builder
    private EarnPointRequest(Long savePoint) {
        this.savePoint = savePoint;
    }

    public static EarnPointRequest of(Long savePoint) {
        return EarnPointRequest.builder()
            .savePoint(savePoint)
            .build();
    }

}
