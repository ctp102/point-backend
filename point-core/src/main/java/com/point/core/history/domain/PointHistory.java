package com.point.core.history.domain;

import com.point.core.common.domain.BaseTimeEntity;
import com.point.core.common.enums.DomainCodes;
import com.point.core.user.domain.User;
import lombok.*;
import org.hibernate.annotations.Comment;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PointHistory extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "POINT_HISTORY_ID")
    private Long pointHistoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @Comment("적립/차감/취소 포인트")
    @Column(name = "POINT", nullable = false)
    private Long point;

    @Comment("포인트 적립/차감/취소 행위")
    @Enumerated(EnumType.STRING)
    @Column(name = "POINT_ACTION_TYPE", nullable = false)
    private DomainCodes pointActionType;

    @Builder
    public PointHistory(User user, Long point, DomainCodes pointActionType) {
        this.user = user;
        this.point = point;
        this.pointActionType = pointActionType;
    }

    public static PointHistory of(User user, Long point, DomainCodes pointActionType) {
        return PointHistory.builder()
                .user(user)
                .point(point)
                .pointActionType(pointActionType)
                .build();
    }

    public void addUser(User user) {
        this.user = user;
    }

}
