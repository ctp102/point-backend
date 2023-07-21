package com.point.core.earn.service;

import com.point.core.earn.domain.EarnPoint;
import com.point.core.earn.repository.EarnPointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EarnPointServiceImpl implements EarnPointService {

    private final EarnPointRepository earnPointRepository;

    @Transactional
    @Override
    public EarnPoint earn(EarnPoint earnPoint) {
        return earnPointRepository.save(earnPoint);
    }

}
