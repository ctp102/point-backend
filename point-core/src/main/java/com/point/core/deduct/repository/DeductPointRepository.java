package com.point.core.deduct.repository;

import com.point.core.deduct.domain.DeductPoint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeductPointRepository extends JpaRepository<DeductPoint, Long> {
}
