package com.point.core.common.facade;

import com.point.core.common.enums.DomainCodes;
import com.point.core.deduct.domain.DeductPoint;
import com.point.core.deduct.dto.CancelDeductPointRequest;
import com.point.core.deduct.dto.DeductPointRequest;
import com.point.core.deduct.service.DeductPointService;
import com.point.core.deduct.validator.DeductPointValidator;
import com.point.core.earn.domain.EarnPoint;
import com.point.core.earn.dto.EarnPointRequest;
import com.point.core.earn.service.EarnPointService;
import com.point.core.history.domain.PointHistory;
import com.point.core.history.dto.PointHistoryDto;
import com.point.core.history.form.PointHistoryForm;
import com.point.core.history.service.PointHistoryService;
import com.point.core.user.domain.User;
import com.point.core.user.dto.UserDto;
import com.point.core.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PointFacade {

    private final UserService userService;
    private final EarnPointService earnPointService;
    private final DeductPointService deductPointService;
    private final PointHistoryService pointHistoryService;
    private final DeductPointValidator deductPointValidator;

    @Transactional(readOnly = true)
    public UserDto findUser(Long userId) {
        User foundUser = userService.findUser(userId);
        return UserDto.of(userId, foundUser.getRemainPoint());
    }

    @Transactional(readOnly = true)
    public Page<PointHistoryDto> findPointHistoryList(Long userId, PointHistoryForm pointHistoryForm, Pageable pageable) {
        Page<PointHistory> result = pointHistoryService.findPointHistoryList(userId, pointHistoryForm, pageable);

        List<PointHistoryDto> pointHistoryDtoList = result.getContent().stream()
                .map(PointHistoryDto::of)
                .toList();

        return new PageImpl<>(pointHistoryDtoList, pageable, result.getTotalElements());
    }

    @Transactional
    public EarnPoint earnPoint(Long userId, EarnPointRequest request) {

        // 1. 포인트 적립
        User foundUser = userService.findUser(userId);

        EarnPoint earnPoint = EarnPoint.of(foundUser, request.getSavePoint());
        foundUser.addEarnPoint(earnPoint);
        EarnPoint savedEarnPoint = earnPointService.earn(earnPoint);

        // 2. 포인트 적립 내역 저장
        PointHistory pointHistory = PointHistory.of(foundUser, request.getSavePoint(), DomainCodes.POINT_EARN);
        foundUser.addPointHistory(pointHistory);
        pointHistoryService.save(pointHistory);

        // 3. 사용자 보유 포인트 갱신
        foundUser.increase(request.getSavePoint());

        return savedEarnPoint;
    }

    @Transactional
    public DeductPoint deductPoint(Long userId, DeductPointRequest request) {

        // 1. 차감 포인트 유효성 검증
        User foundUser = userService.findUser(userId);
        deductPointValidator.validate(foundUser.getRemainPoint(), request.getDeductPoint());

        // 2. 포인트 차감 및 내역 저장
        DeductPoint deductPoint = DeductPoint.of(foundUser, request.getDeductPoint());
        foundUser.addDeductPoint(deductPoint);

        DeductPoint savedDeductPoint = deductPointService.deduct(deductPoint);

        // 3. 사용자 보유 포인트 갱신
        foundUser.decrease(request.getDeductPoint());

        return savedDeductPoint;
    }

    @Transactional
    public void cancelDeductPoint(Long userId, CancelDeductPointRequest request) {

        // 1. 포인트 차감 취소 및 내역 저장
        deductPointService.cancelDeduct(userId, request.getCancelPoint());

        // 2. 사용자 보유 포인트 갱신
        User foundUser = userService.findUser(userId);
        foundUser.increase(request.getCancelPoint());
    }
}
