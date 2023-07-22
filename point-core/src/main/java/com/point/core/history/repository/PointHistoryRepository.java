package com.point.core.history.repository;

import com.point.core.common.enums.DomainCodes;
import com.point.core.history.domain.PointHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {

    @Query("SELECT ph FROM PointHistory ph " +
            "WHERE ph.user.userId = :userId " +
            "AND ph.pointActionType in :pointActionTypes " +
            "ORDER BY ph.pointHistoryId ASC")
    Page<PointHistory> findAll(
            @Param("userId") Long userId,
            @Param("pointActionTypes") List<DomainCodes> pointActionTypes,
            @Param("pageable") Pageable pageable
    );

}
