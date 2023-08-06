package com.point.core.earn.domain;

import com.point.core.common.domain.BaseTimeEntity;
import com.point.core.common.enums.DomainCodes;
import com.point.core.user.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EarnPoint extends BaseTimeEntity {

    @Comment("획득 포인트 번호")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EARN_POINT_ID")
    private Long earnPointId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @Comment("획득 포인트")
    @Column(name = "SAVE_POINT", nullable = false)
    private Long savePoint;

    @Comment("차감 후 잔여 포인트")
    @Column(name = "REMAIN_POINT", nullable = false)
    private Long remainPoint;

    @Comment("포인트 차감 상태")
    @Enumerated(EnumType.STRING)
    @Column(name = "POINT_DEDUCT_STATUS", nullable = false)
    private DomainCodes pointDeductStatus;

    @Comment("만료 여부")
    @ColumnDefault("'N'")
    @Column(name = "EXPIRATION_YN", nullable = false)
    private String expirationYn;

    @Comment("포인트 유효 기간(1년)")
    @Column(name = "EXPIRED_DATE", nullable = false)
    private LocalDateTime expiredDate;

    @Builder
    private EarnPoint(User user, Long savePoint, Long remainPoint, DomainCodes pointDeductStatus, String expirationYn, LocalDateTime expiredDate) {
        this.user = user;
        this.savePoint = savePoint;
        this.remainPoint = remainPoint;
        this.pointDeductStatus = pointDeductStatus;
        this.expirationYn = expirationYn;
        this.expiredDate = expiredDate;
    }

    public static EarnPoint of(User user, Long savePoint) {
        return EarnPoint.builder()
                .user(user)
                .savePoint(savePoint)
                .remainPoint(savePoint) // 처음에는 획득 포인트와 잔여 포인트가 동일하다.
                .pointDeductStatus(DomainCodes.POINT_DEDUCT_ST_AVAILABLE)
                .expirationYn("N")
                .expiredDate(LocalDateTime.now().plusYears(1))
                .build();
    }

    public void addUser(User user) {
        this.user = user;
    }

}
