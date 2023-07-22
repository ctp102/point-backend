package com.point.core.history.service;

import com.point.core.history.domain.PointHistory;
import com.point.core.history.form.PointHistoryForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PointHistoryService {

    Page<PointHistory> findPointHistoryList(Long userId, PointHistoryForm pointHistoryForm, Pageable pageable);

    PointHistory save(PointHistory pointHistory);

}
