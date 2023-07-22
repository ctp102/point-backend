package com.point.core.deduct.service;

import com.point.core.common.enums.DomainCodes;
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

@Service
@RequiredArgsConstructor
public class DeductPointServiceImpl implements DeductPointService {

    private final UserRepository userRepository;
    private final EarnPointRepository earnPointRepository;
    private final DeductPointRepository deductPointRepository;
    private final PointHistoryRepository pointHistoryRepository;
    private final DeductPointValidator deductPointValidator;

    /**
     * 포인트 차감
     */
    @Transactional
    @Override
    public DeductPoint deduct(DeductPoint deductPoint) {
        List<EarnPoint> availableEarnPoints = earnPointRepository.findAvailableEarnPoints(deductPoint.getUser().getUserId());

        Long tempDeductPoint = deductPoint.getDeductPoint();
        for (EarnPoint earnPoint : availableEarnPoints) {
            if (tempDeductPoint == 0) {
                break;
            }

            // 포인트 만료일 한번 더 체크
            if (deductPointValidator.isExpired(earnPoint.getExpiredDate())) {
                handleExpiredEarnPoint(earnPoint);
                continue;
            }
            tempDeductPoint = handleAvailableEarnPoint(earnPoint, tempDeductPoint);
        }

        return deductPointRepository.save(deductPoint);
    }

    /**
     * 포인트 차감 취소
     */
    @Transactional
    @Override
    public void cancelDeduct(Long userId, Long cancelPoint) {

        // 포인트 복구 가능한 earnPointList 조회
        List<EarnPoint> restorableEarnPointList = earnPointRepository.findRestorableEarnPointList(userId);

        Long tempRestorePoint = cancelPoint;
        for (EarnPoint earnPoint : restorableEarnPointList) {
            if (tempRestorePoint == 0) {
                break;
            }

            tempRestorePoint = handleRestorableEarnPoint(earnPoint, tempRestorePoint);
        }
    }

    private void handleExpiredEarnPoint(EarnPoint earnPoint) {
        userRepository.updateRemainPoint(earnPoint.getUser().getUserId(), earnPoint.getRemainPoint());
        earnPointRepository.updateExpirationYn(earnPoint.getEarnPointId());
    }

    private Long handleAvailableEarnPoint(EarnPoint earnPoint, Long tempDeductPoint) {

        if (tempDeductPoint >= earnPoint.getRemainPoint()) {
            PointHistory pointHistory = PointHistory.of(earnPoint.getUser(), earnPoint.getRemainPoint(), DomainCodes.POINT_DEDUCT);
            earnPoint.getUser().addPointHistory(pointHistory);

            pointHistoryRepository.save(pointHistory);

            return tempDeductPoint - earnPoint.getRemainPoint();
        } else {
            PointHistory pointHistory = PointHistory.of(earnPoint.getUser(), tempDeductPoint, DomainCodes.POINT_DEDUCT);
            earnPoint.getUser().addPointHistory(pointHistory);

            pointHistoryRepository.save(pointHistory);
            earnPointRepository.updateRemainPointForDeduct(earnPoint.getEarnPointId(), tempDeductPoint);
            return 0L;
        }
    }

    private Long handleRestorableEarnPoint(EarnPoint earnPoint, Long tempRestorePoint) {

        Long restorePoint = earnPoint.getSavePoint();

        if (tempRestorePoint >= restorePoint) {
            PointHistory pointHistory = PointHistory.of(earnPoint.getUser(), restorePoint, DomainCodes.POINT_DEDUCT_CANCEL);
            earnPoint.getUser().addPointHistory(pointHistory);

            pointHistoryRepository.save(pointHistory);
            earnPointRepository.updateRemainPointForRestoration(earnPoint.getEarnPointId(), restorePoint);

            return tempRestorePoint - restorePoint;
        } else {
            PointHistory pointHistory = PointHistory.of(earnPoint.getUser(), tempRestorePoint, DomainCodes.POINT_DEDUCT_CANCEL);
            earnPoint.getUser().addPointHistory(pointHistory);

            pointHistoryRepository.save(pointHistory);
            earnPointRepository.updateRemainPointForRestoration(earnPoint.getEarnPointId(), restorePoint - tempRestorePoint);

            return 0L;
        }
    }

}
