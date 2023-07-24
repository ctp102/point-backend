package com.point.core.user.domain;

import com.point.core.common.domain.BaseTimeEntity;
import com.point.core.common.exception.CustomAccessDeniedException;
import com.point.core.deduct.domain.DeductPoint;
import com.point.core.earn.domain.EarnPoint;
import com.point.core.history.domain.PointHistory;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static com.point.core.common.enums.ErrorResponseCodes.NOT_ENOUGH_POINT;

@Entity
@Table(name = "USERS")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

    @Comment("회원 번호")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Long userId;

    @Comment("잔여 포인트")
    @Column(name = "REMAIN_POINT", nullable = false)
    private Long remainPoint;

    @Comment("획득 포인트 목록")
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EarnPoint> earnPoints = new ArrayList<>();

    @Comment("차감 포인트 목록")
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DeductPoint> deductPoints = new ArrayList<>();

    @Comment("포인트 획득/차감 내역")
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PointHistory> pointHistories = new ArrayList<>();

    public void increase(final Long point) {
        this.remainPoint += point;
    }

    public void decrease(final Long point) {
        if (this.remainPoint < point) {
            throw new CustomAccessDeniedException(NOT_ENOUGH_POINT.getNumber(), NOT_ENOUGH_POINT.getMessage());
        }
        this.remainPoint -= point;
    }

    public void addEarnPoint(EarnPoint earnPoint) {
        this.earnPoints.add(earnPoint);
        earnPoint.addUser(this);
    }

    public void addDeductPoint(DeductPoint deductPoint) {
        this.deductPoints.add(deductPoint);
        deductPoint.addUser(this);
    }

    public void addPointHistory(PointHistory pointHistory) {
        this.pointHistories.add(pointHistory);
        pointHistory.addUser(this);
    }

    @Builder
    public User(Long userId, Long remainPoint) {
        this.userId = userId;
        this.remainPoint = remainPoint;
    }

}