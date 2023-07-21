package com.point.api.controller;

import com.point.core.common.facade.PointFacade;
import com.point.core.common.response.CustomCommonResponseCodes;
import com.point.core.common.response.CustomResponse;
import com.point.core.deduct.domain.DeductPoint;
import com.point.core.deduct.dto.DeductPointRequest;
import com.point.core.earn.domain.EarnPoint;
import com.point.core.earn.dto.EarnPointRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/api/v1/users/{userId}")
public class UserPointRestController {

    private final PointFacade pointFacade;

    /**
     * 회원별 잔여 포인트 조회
     *
     * @param userId
     * @return
     */
    @GetMapping("/points")
    public CustomResponse getUser(@PathVariable("userId") final Long userId) {
        return new CustomResponse.Builder(CustomCommonResponseCodes.OK).build();
    }

    /**
     * 회원별 포인트 적립/사용 내역 조회
     *
     * @param userId
     * @return
     */
    @GetMapping("/points/history")
    public CustomResponse getUserHistory(@PathVariable("userId") final Long userId, @PageableDefault Pageable pageable) {
        return new CustomResponse.Builder(CustomCommonResponseCodes.OK).build();
    }

    /**
     * 회원별 포인트 적립
     *
     * @param userId
     * @return
     */
    @PostMapping("/points/earn")
    public CustomResponse earnPoint(@PathVariable("userId") final Long userId, @RequestBody final EarnPointRequest earnPointRequest) {
        EarnPoint earnPoint = pointFacade.earnPoint(userId, earnPointRequest);
        return new CustomResponse.Builder().addItems(earnPoint).build();
    }

    /**
     * 회원별 포인트 차감
     *
     * @param userId
     * @return
     */
    @PostMapping("/points/deduct")
    public CustomResponse deductPoint(@PathVariable("userId") final Long userId, @RequestBody final DeductPointRequest deductPointRequest) {
        DeductPoint deductPoint = pointFacade.deductPoint(userId, deductPointRequest);
        return new CustomResponse.Builder().addItems(deductPoint).build();
    }

    /**
     * 회원별 포인트 차감 취소
     *
     * @param userId
     * @return
     */
    @PostMapping("/points/cancel-deduct")
    public CustomResponse cancelDeductPoint(@PathVariable("userId") final Long userId) {
        return new CustomResponse.Builder(CustomCommonResponseCodes.OK).build();
    }

}


