package com.point.core.history.service;

import com.point.core.common.enums.DomainCodes;
import com.point.core.history.domain.PointHistory;
import com.point.core.history.form.PointHistoryForm;
import com.point.core.history.repository.PointHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PointHistoryServiceImpl implements PointHistoryService {

    private final PointHistoryRepository pointHistoryRepository;

    @Transactional(readOnly = true)
    @Override
    public Page<PointHistory> findPointHistoryList(Long userId, PointHistoryForm pointHistoryForm, Pageable pageable) {
        List<DomainCodes> pointActionTypes = getPointActionTypes(pointHistoryForm.getPointActionType());
        return pointHistoryRepository.findAll(userId, pointActionTypes, pageable);
    }

    @Transactional
    @Override
    public PointHistory save(PointHistory pointHistory) {
        return pointHistoryRepository.save(pointHistory);
    }

    private List<DomainCodes> getPointActionTypes(DomainCodes pointActionType) {
        if (Objects.isNull(pointActionType)) {
            return List.of(DomainCodes.POINT_EARN, DomainCodes.POINT_DEDUCT, DomainCodes.POINT_DEDUCT_CANCEL);
        }
        return List.of(pointActionType);
    }

}
