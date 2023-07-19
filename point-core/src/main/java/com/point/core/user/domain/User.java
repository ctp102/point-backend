package com.point.core.user.domain;

import com.point.core.common.enums.ErrorResponseCodes;
import com.point.core.common.exception.CustomAccessDeniedException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Comment("잔여 포인트")
    @Column(name = "REMAINING_POINT", nullable = false)
    private Long remainingPoint;

    public void increasePoint(final Long point) {
        this.remainingPoint += point;
    }

    public void decreasePoint(final Long point) {
        if (this.remainingPoint < point) {
            throw new CustomAccessDeniedException(ErrorResponseCodes.NOT_ENOUGH_POINT.getNumber(), ErrorResponseCodes.NOT_ENOUGH_POINT.getMessage());
        }
        this.remainingPoint -= point;
    }

}
