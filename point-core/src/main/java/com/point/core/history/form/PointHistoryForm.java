package com.point.core.history.form;

import com.point.core.common.enums.DomainCodes;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PointHistoryForm {

    private DomainCodes pointActionType;

    @Builder
    private PointHistoryForm(DomainCodes pointActionType) {
        this.pointActionType = pointActionType;
    }

    public static PointHistoryForm of(DomainCodes pointActionType) {
        return PointHistoryForm.builder()
                .pointActionType(pointActionType)
                .build();
    }

}
