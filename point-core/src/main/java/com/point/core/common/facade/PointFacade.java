package com.point.core.common.facade;

import com.point.core.common.enums.DomainCodes;
import com.point.core.deduct.domain.DeductPoint;
import com.point.core.deduct.dto.DeductPointRequest;
import com.point.core.deduct.service.DeductPointService;
import com.point.core.earn.domain.EarnPoint;
import com.point.core.earn.dto.EarnPointRequest;
import com.point.core.earn.service.EarnPointService;
import com.point.core.history.domain.PointHistory;
import com.point.core.history.service.PointHistoryService;
import com.point.core.user.domain.User;
import com.point.core.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class PointFacade {

    private final UserService userService;
    private final EarnPointService earnPointService;
    private final DeductPointService deductPointService;
    private final PointHistoryService pointHistoryService;

    @Transactional
    public EarnPoint earnPoint(Long userId, EarnPointRequest request) {

        // 1. 포인트 적립
        User foundUser = userService.findUser(userId);
        EarnPoint earnPoint = EarnPoint.of(foundUser, request.getSavePoint());
        EarnPoint savedEarnPoint = earnPointService.earn(earnPoint);

        // 2. 포인트 적립 내역 저장
        PointHistory pointHistory = PointHistory.of(foundUser, request.getSavePoint(), DomainCodes.POINT_EARN);
        pointHistoryService.save(pointHistory);

        // 3. 포인트 합산 갱신
        foundUser.increase(request.getSavePoint());

        return savedEarnPoint;
    }

    @Transactional
    public DeductPoint deductPoint(Long userId, DeductPointRequest deductPointRequest) {
        // 1. 보유 포인트 검증

        // 2. 포인트 차감

        // 3. 포인트 차감 내역 저장

        // 4. 포인트 합산 갱신

        return null;
    }

}
