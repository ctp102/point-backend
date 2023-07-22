package com.point.core.deduct.repository;

import com.point.core.deduct.domain.DeductPoint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeductPointRepository extends JpaRepository<DeductPoint, Long> {

//    @Query("SELECT dp FROM DeductPoint dp " +
//            "WHERE dp.user.userId = :userId " +
//            "ORDER BY dp.deductPointId DESC")
//    List<DeductPoint> findDeductPointList(
//            @Param("userId") Long userId
//    );

}
