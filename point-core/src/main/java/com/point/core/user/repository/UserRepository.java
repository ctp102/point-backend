package com.point.core.user.repository;

import com.point.core.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Modifying(clearAutomatically = true)
    @Query("UPDATE User u " +
            "SET u.remainPoint = (u.remainPoint - :earnRemainPoint) " +
            "WHERE u.userId = :userId")
    void updateRemainPointForExpiration(
            @Param("userId") Long userId,
            @Param("earnRemainPoint") Long earnRemainPoint
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<User> findByUserId(Long userId);
}
