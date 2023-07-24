package com.point.core.earn.repository;

import com.point.core.common.enums.DomainCodes;
import com.point.core.earn.domain.EarnPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EarnPointRepository extends JpaRepository<EarnPoint, Long> {

    @Query("SELECT ep FROM EarnPoint ep " +
            "WHERE ep.user.userId = :userId " +
            "AND ep.pointDeductStatus in :availableEarnPointTypes " +
            "AND ep.expirationYn = 'N' " +
            "ORDER BY ep.earnPointId ASC")
    List<EarnPoint> findAvailableEarnPoints(
            @Param("userId") Long userId,
            @Param("availableEarnPointTypes") List<DomainCodes> availableEarnPointTypes
    );

    @Query("SELECT ep FROM EarnPoint ep " +
            "WHERE ep.user.userId = :userId " +
            "AND ep.pointDeductStatus in :restorableEarnPointTypes " +
            "AND ep.expirationYn = 'N' " +
            "ORDER BY ep.earnPointId DESC")
    List<EarnPoint> findRestorableEarnPointList(
            @Param("userId") Long userId,
            @Param("restorableEarnPointTypes") List<DomainCodes> restorableEarnPointTypes
    );

    @Modifying(clearAutomatically = true)
    @Query("UPDATE EarnPoint ep " +
            "SET ep.expirationYn = 'Y' " +
            "WHERE ep.earnPointId = :earnPointId")
    void updateExpirationYn(
            @Param("earnPointId") Long earnPointId
    );

    @Modifying
    @Query("UPDATE EarnPoint ep " +
            "SET ep.remainPoint = 0, ep.pointDeductStatus = :pointDeductStatus " +
            "WHERE ep.earnPointId = :earnPointId")
    void updateRemainPointForDeductAll(
            @Param("earnPointId") Long earnPointId,
            @Param("pointDeductStatus") DomainCodes pointDeductStatus
    );

    @Modifying
    @Query("UPDATE EarnPoint ep " +
            "SET ep.remainPoint = (ep.remainPoint - :tempDeductPoint), " +
            "    ep.pointDeductStatus = :pointDeductStatus " +
            "WHERE ep.earnPointId = :earnPointId")
    void updateRemainPointForDeductPart(
            @Param("earnPointId") Long earnPointId,
            @Param("tempDeductPoint") long tempDeductPoint,
            @Param("pointDeductStatus") DomainCodes pointDeductStatus
    );

    @Modifying
    @Query("UPDATE EarnPoint ep " +
            "SET ep.remainPoint = (ep.remainPoint + :restorePoint)," +
            "    ep.pointDeductStatus = :pointDeductStatus " +
            "WHERE ep.earnPointId = :earnPointId")
    void updateRemainPointAndDeductStatusForRestoration(
            @Param("earnPointId") Long earnPointId,
            @Param("restorePoint") Long restorePoint,
            @Param("pointDeductStatus") DomainCodes pointDeductStatus
    );

}
