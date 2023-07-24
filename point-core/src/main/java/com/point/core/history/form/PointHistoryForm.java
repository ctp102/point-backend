package com.point.core.history.form;

import com.point.core.common.enums.DomainCodes;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PointHistoryForm {

    private DomainCodes pointActionType;

    public PointHistoryForm(DomainCodes pointActionType) {
        this.pointActionType = pointActionType;
    }
}
