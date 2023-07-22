package com.point.core.earn.repository;

import com.point.core.earn.domain.EarnPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EarnPointRepository extends JpaRepository<EarnPoint, Long> {

    @Query("SELECT ep FROM EarnPoint ep " +
            "WHERE ep.user.userId = :userId " +
            "AND ep.expirationYn = 'N' " +
            "AND ep.remainPoint > 0 " +
            "ORDER BY ep.earnPointId ASC")
    List<EarnPoint> findAvailableEarnPoints(
            @Param("userId") Long userId
    );

    @Modifying(clearAutomatically = true)
    @Query("UPDATE EarnPoint ep " +
            "SET ep.expirationYn = 'Y' " +
            "WHERE ep.earnPointId = :earnPointId")
    void updateExpirationYn(
            @Param("earnPointId") Long earnPointId
    );

//    @Transactional
    @Modifying
    @Query("UPDATE EarnPoint ep " +
            "SET ep.remainPoint = (ep.remainPoint - :tempDeductPoint) " +
            "WHERE ep.earnPointId = :earnPointId")
    void updateRemainPoint(
            @Param("earnPointId") Long earnPointId,
            @Param("tempDeductPoint") long tempDeductPoint
    );

}
