package com.point.core.history.dto;

import com.point.core.common.enums.DomainCodes;
import com.point.core.history.domain.PointHistory;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PointHistoryDto {

    private Long userId;
    private Long pointHistoryId;
    private Long point; // 적립/차감/취소 포인트
    private DomainCodes pointActionType;
    private LocalDateTime createdDate;

    @Builder
    public PointHistoryDto(Long userId, Long pointHistoryId, Long point, DomainCodes pointActionType, LocalDateTime createdDate) {
        this.userId = userId;
        this.pointHistoryId = pointHistoryId;
        this.point = point;
        this.pointActionType = pointActionType;
        this.createdDate = createdDate;
    }

    public static PointHistoryDto of(PointHistory pointHistory) {
        return PointHistoryDto.builder()
                .userId(pointHistory.getUser().getUserId())
                .pointHistoryId(pointHistory.getPointHistoryId())
                .point(pointHistory.getPoint())
                .pointActionType(pointHistory.getPointActionType())
                .createdDate(pointHistory.getCreatedDate())
                .build();
    }

}
