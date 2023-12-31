package com.point.core.deduct.validator;

import com.point.core.common.exception.CustomAccessDeniedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static com.point.core.common.enums.ErrorResponseCodes.NOT_ENOUGH_POINT;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeductPointValidator {

    public void validate(Long remainPoint, Long deductPoint) {
        if (remainPoint < deductPoint) {
            log.error("[deductPoint] 잔여 포인트가 부족합니다. 잔여 포인트 = {}, 차감 시도 포인트 = {}", remainPoint, deductPoint);
            throw new CustomAccessDeniedException(NOT_ENOUGH_POINT.getNumber(), NOT_ENOUGH_POINT.getMessage());
        }
    }

    public boolean isExpired(LocalDateTime expiredDate) {
        return LocalDateTime.now().isAfter(expiredDate);
    }

}
