package com.point.api.controller;

import com.point.core.common.facade.PointFacade;
import com.point.core.common.response.CustomResponse;
import com.point.core.deduct.dto.CancelDeductPointRequest;
import com.point.core.deduct.dto.DeductPointRequest;
import com.point.core.earn.dto.EarnPointRequest;
import com.point.core.history.dto.PointHistoryDto;
import com.point.core.history.form.PointHistoryForm;
import com.point.core.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

/**
 * The type User point rest controller.
 */
@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/api/v1/users/{userId}")
public class UserPointRestController {

    private final PointFacade pointFacade;

    /**
     * 회원별 잔여 포인트 조회
     *
     * @param userId the user id
     * @return user point
     */
    @GetMapping("/points")
    public CustomResponse getUserPoint(@PathVariable("userId") final Long userId) {
        UserDto item = pointFacade.findUser(userId);
        return new CustomResponse.Builder().addItems(item).build();
    }

    /**
     * 회원별 포인트 적립/사용 내역 조회
     *
     * @param userId           the user id
     * @param pointHistoryForm the point history form
     * @param pageable         the pageable
     * @return user point history
     */
    @GetMapping("/points/history")
    public CustomResponse getPointHistoryList(@PathVariable("userId") final Long userId, final PointHistoryForm pointHistoryForm, @PageableDefault Pageable pageable) {
        Page<PointHistoryDto> items = pointFacade.findPointHistoryList(userId, pointHistoryForm, pageable);
        return new CustomResponse.Builder().addItems(items).build();
    }

    /**
     * 회원별 포인트 적립
     *
     * @param userId  the user id
     * @param request the request
     * @return custom response
     */
    @PostMapping("/points/earn")
    public CustomResponse earnPoint(@PathVariable("userId") final Long userId, @RequestBody final EarnPointRequest request) {
        pointFacade.earnPoint(userId, request);
        return new CustomResponse.Builder().build();
    }

    /**
     * 회원별 포인트 차감
     *
     * @param userId  the user id
     * @param request the deduct point request
     * @return custom response
     */
    @PostMapping("/points/deduct")
    public CustomResponse deductPoint(@PathVariable("userId") final Long userId, @RequestBody final DeductPointRequest request) {
        pointFacade.deductPoint(userId, request);
        return new CustomResponse.Builder().build();
    }

    /**
     * 회원별 포인트 차감 취소(사용한 포인트 복구)
     *
     * @param userId the user id
     * @return custom response
     */
    @PostMapping("/points/cancel-deduct")
    public CustomResponse cancelDeductPoint(@PathVariable("userId") final Long userId, @RequestBody final CancelDeductPointRequest request) {
        pointFacade.cancelDeductPoint(userId, request);
        return new CustomResponse.Builder().build();
    }

}


