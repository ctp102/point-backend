package com.point.core.deduct.domain;

import com.point.core.common.domain.BaseTimeEntity;
import com.point.core.deduct.dto.DeductPointRequest;
import com.point.core.earn.domain.EarnPoint;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EARN_POINT_ID", nullable = false)
    private EarnPoint earnPoint;

    @Comment("차감 포인트")
    @Column(name = "DEDUCT_POINT", nullable = false)
    private Long deductPoint;

    @Builder
    public DeductPoint(Long deductPoint) {
        this.deductPoint = deductPoint;
    }

    public static DeductPoint of(Long userId, DeductPointRequest request) {
        return DeductPoint.builder()
//                .user(user.of(userId))
                .deductPoint(request.getDeductPoint())
                .build();
    }

}
