package com.point.core.deduct.service;

import com.point.core.common.enums.DomainCodes;
import com.point.core.common.exception.CustomAccessDeniedException;
import com.point.core.deduct.domain.DeductPoint;
import com.point.core.deduct.repository.DeductPointRepository;
import com.point.core.deduct.validator.DeductPointValidator;
import com.point.core.earn.domain.EarnPoint;
import com.point.core.earn.repository.EarnPointRepository;
import com.point.core.history.domain.PointHistory;
import com.point.core.history.repository.PointHistoryRepository;
import com.point.core.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.point.core.common.enums.ErrorResponseCodes.NOT_ENOUGH_CANCEL_DEDUCT_POINT;

@Service
@RequiredArgsConstructor
public class DeductPointServiceImpl implements DeductPointService {

    private final UserRepository userRepository;
    private final EarnPointRepository earnPointRepository;
    private final DeductPointRepository deductPointRepository;
    private final PointHistoryRepository pointHistoryRepository;
    private final DeductPointValidator deductPointValidator;

    private final List<DomainCodes> availableEarnPointTypes = List.of(DomainCodes.POINT_DEDUCT_ST_PART, DomainCodes.POINT_DEDUCT_ST_AVAILABLE);
    private final List<DomainCodes> restorableEarnPointTypes = List.of(DomainCodes.POINT_DEDUCT_ST_ALL, DomainCodes.POINT_DEDUCT_ST_PART);

    /**
     * 포인트 차감
     */
    @Transactional
    @Override
    public DeductPoint deduct(DeductPoint deductPoint) {
        List<EarnPoint> availableEarnPoints = earnPointRepository.findAvailableEarnPoints(deductPoint.getUser().getUserId(), availableEarnPointTypes);
        Long remainingDeductPoint = deductPoint.getDeductPoint();

        for (EarnPoint earnPoint : availableEarnPoints) {
            if (remainingDeductPoint == 0) {
                break;
            }

            // 포인트 만료일 한번 더 체크
            if (deductPointValidator.isExpired(earnPoint.getExpiredDate())) {
                handleExpiredEarnPoint(earnPoint);
            } else {
                remainingDeductPoint = handleAvailableEarnPoint(earnPoint, remainingDeductPoint);
            }
        }

        return deductPointRepository.save(deductPoint);
    }

    /**
     * 포인트 차감 취소
     */
    @Transactional
    @Override
    public void cancelDeduct(Long userId, Long cancelPoint) {

        List<EarnPoint> restorableEarnPointList = earnPointRepository.findRestorableEarnPointList(userId, restorableEarnPointTypes);

        if (restorableEarnPointList.isEmpty()) {
            throw new CustomAccessDeniedException(NOT_ENOUGH_CANCEL_DEDUCT_POINT.getNumber(), NOT_ENOUGH_CANCEL_DEDUCT_POINT.getMessage());
        }

        Long remainingCancelPoint = cancelPoint; // 차감 취소시킬 포인트
        for (EarnPoint earnPoint : restorableEarnPointList) {
            if (remainingCancelPoint == 0) {
                break;
            }
            remainingCancelPoint = handleRestorableEarnPoint(earnPoint, remainingCancelPoint);
        }
    }

    private void handleExpiredEarnPoint(EarnPoint earnPoint) {
        userRepository.updateRemainPointForExpiration(earnPoint.getUser().getUserId(), earnPoint.getRemainPoint());
        earnPointRepository.updateExpirationYn(earnPoint.getEarnPointId());
    }

    private Long handleAvailableEarnPoint(EarnPoint earnPoint, Long remainingDeductPoint) {

        if (earnPoint.getRemainPoint() > remainingDeductPoint) {
            PointHistory pointHistory = PointHistory.of(earnPoint.getUser(), remainingDeductPoint, DomainCodes.POINT_DEDUCT);
            earnPoint.getUser().addPointHistory(pointHistory);

            pointHistoryRepository.save(pointHistory);
            earnPointRepository.updateRemainPointForDeductPart(earnPoint.getEarnPointId(), remainingDeductPoint, DomainCodes.POINT_DEDUCT_ST_PART);
            return 0L;
        } else {
            PointHistory pointHistory = PointHistory.of(earnPoint.getUser(), earnPoint.getRemainPoint(), DomainCodes.POINT_DEDUCT);
            earnPoint.getUser().addPointHistory(pointHistory);

            pointHistoryRepository.save(pointHistory);
            earnPointRepository.updateRemainPointForDeductAll(earnPoint.getEarnPointId(), DomainCodes.POINT_DEDUCT_ST_ALL);

            return remainingDeductPoint - earnPoint.getRemainPoint();
        }
    }

    private Long handleRestorableEarnPoint(EarnPoint earnPoint, Long remainingCancelPoint) {

        Long currCancelPoint = getCurrentCancelPoint(earnPoint, remainingCancelPoint);

        if (remainingCancelPoint >= currCancelPoint) {
            PointHistory pointHistory = PointHistory.of(earnPoint.getUser(), currCancelPoint, DomainCodes.POINT_DEDUCT_CANCEL);
            earnPoint.getUser().addPointHistory(pointHistory);

            pointHistoryRepository.save(pointHistory);
            earnPointRepository.updateRemainPointAndDeductStatusForRestoration(earnPoint.getEarnPointId(), currCancelPoint, DomainCodes.POINT_DEDUCT_ST_AVAILABLE);

            return remainingCancelPoint - currCancelPoint;
        } else {
            PointHistory pointHistory = PointHistory.of(earnPoint.getUser(), remainingCancelPoint, DomainCodes.POINT_DEDUCT_CANCEL);
            earnPoint.getUser().addPointHistory(pointHistory);

            pointHistoryRepository.save(pointHistory);

            earnPointRepository.updateRemainPointAndDeductStatusForRestoration(earnPoint.getEarnPointId(), remainingCancelPoint, DomainCodes.POINT_DEDUCT_ST_PART);

            return 0L;
        }
    }

    private Long getCurrentCancelPoint(EarnPoint earnPoint, Long remainingCancelPoint) {
        return switch (earnPoint.getPointDeductStatus()) {
            case POINT_DEDUCT_ST_ALL -> earnPoint.getSavePoint();
            case POINT_DEDUCT_ST_PART -> earnPoint.getRemainPoint() + remainingCancelPoint == earnPoint.getSavePoint() ? remainingCancelPoint : 0L;
            default -> 0L;
        };
    }

}
