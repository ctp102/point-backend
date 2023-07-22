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

//    private final EarnPointService earnPointService;
    private final UserRepository userRepository;
    private final EarnPointRepository earnPointRepository;
    private final DeductPointRepository deductPointRepository;
    private final PointHistoryRepository pointHistoryRepository;
    private final DeductPointValidator deductPointValidator;

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
            tempDeductPoint = handleAvailablePoint(earnPoint, tempDeductPoint);
        }

        return deductPointRepository.save(deductPoint);
    }

    private void handleExpiredEarnPoint(EarnPoint earnPoint) {
        userRepository.updateRemainPoint(earnPoint.getUser().getUserId(), earnPoint.getRemainPoint());
        earnPointRepository.updateExpirationYn(earnPoint.getEarnPointId());
    }

    private Long handleAvailablePoint(EarnPoint earnPoint, Long tempDeductPoint) {
        if (tempDeductPoint >= earnPoint.getRemainPoint()) {
            PointHistory pointHistory = PointHistory.of(earnPoint.getUser(), earnPoint.getRemainPoint(), DomainCodes.POINT_DEDUCT);
            pointHistoryRepository.save(pointHistory);
            return tempDeductPoint - earnPoint.getRemainPoint();
        } else {
            PointHistory pointHistory = PointHistory.of(earnPoint.getUser(), tempDeductPoint, DomainCodes.POINT_DEDUCT);
            pointHistoryRepository.save(pointHistory);
            earnPointRepository.updateRemainPoint(earnPoint.getEarnPointId(), tempDeductPoint);
            return 0L;
        }
    }

}
