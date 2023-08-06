package com.point.core.deduct.domain;

import com.point.core.common.domain.BaseTimeEntity;
import com.point.core.user.domain.User;
import lombok.*;
import org.hibernate.annotations.Comment;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class DeductPoint extends BaseTimeEntity {

    @Comment("차감 포인트 번호")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DEDUCT_POINT_ID")
    private Long deductPointId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @Comment("차감 포인트")
    @Column(name = "DEDUCT_POINT", nullable = false)
    private Long deductPoint;

    @Builder
    private DeductPoint(User user, Long deductPoint) {
        this.user = user;
        this.deductPoint = deductPoint;
    }

    public static DeductPoint of(User user, Long deductPoint) {
        return DeductPoint.builder()
                .user(user)
                .deductPoint(deductPoint)
                .build();
    }

    public void addUser(User user) {
        this.user = user;
    }

}
