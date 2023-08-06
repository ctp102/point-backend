package com.point.api.controller;

import com.point.core.common.facade.PointFacade;
import com.point.core.common.response.CustomResponse;
import com.point.core.deduct.dto.CancelDeductPointRequest;
import com.point.core.deduct.dto.DeductPointRequest;
import com.point.core.earn.domain.EarnPoint;
import com.point.core.earn.dto.EarnPointRequest;
import com.point.core.history.dto.PointHistoryDto;
import com.point.core.history.form.PointHistoryForm;
import com.point.core.user.dto.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "UserPointRestController")
@Slf4j
@RestController
@RequestMapping("/api/v1/users/{userId}")
@RequiredArgsConstructor
public class UserPointRestController {

    private final PointFacade pointFacade;
//    private final PointRedissonFacade pointRedissonFacade;

    @Operation(summary = "회원별 잔여 포인트 조회")
    @ApiResponse(responseCode = "200", description = "API 요청 성공", content = {
            @Content(
                    schema = @Schema(implementation = CustomResponse.class),
                    examples = {
                            @ExampleObject(value = "{\"status\":{\"number\":200,\"code\":\"OK\",\"message\":\"OK\"},\"data\":{\"items\":[{\"userId\":1,\"remainPoint\":3000}]}}")
                    }
            )
    })
    @GetMapping("/points")
    public CustomResponse getUserPoint(@PathVariable("userId") final Long userId) {
        UserDto item = pointFacade.findUser(userId);
        return new CustomResponse.Builder().addItems(item).build();
    }

    @Operation(summary = "회원별 포인트 적립/차감/취소 내역 조회")
    @ApiResponse(responseCode = "200", description = "API 요청 성공", content = {
            @Content(
                    schema = @Schema(implementation = CustomResponse.class),
                    examples = {
                            @ExampleObject(value = "{\"status\":{\"number\":200,\"code\":\"OK\",\"message\":\"OK\"},\"data\":{\"items\":[{\"userId\":1,\"pointHistoryId\":1,\"point\":10000,\"pointActionType\":\"POINT_EARN\",\"createdDate\":\"2023-07-24T21:21:41.022511\"}],\"paging\":{\"pageNo\":1,\"pageSize\":10,\"totalElements\":5,\"totalPage\":1,\"sort\":{\"empty\":true,\"unsorted\":true,\"sorted\":false}}}}")
                    }
            )
    })
    @GetMapping("/points/history")
    public CustomResponse getPointHistoryList(@PathVariable("userId") final Long userId, final PointHistoryForm pointHistoryForm, @PageableDefault Pageable pageable) {
        Page<PointHistoryDto> items = pointFacade.findPointHistoryList(userId, pointHistoryForm, pageable);
        return new CustomResponse.Builder().addItems(items).build();
    }

    @Operation(summary = "회원별 포인트 적립")
    @ApiResponse(responseCode = "200", description = "API 요청 성공", content = {
            @Content(
                    schema = @Schema(implementation = CustomResponse.class),
                    examples = {
                            @ExampleObject(value = "{\"status\":{\"number\":200,\"code\":\"OK\",\"message\":\"OK\"},\"data\":{}}")
                    }
            )
    })
    @PostMapping("/points/earn")
    public CustomResponse earnPoint(@PathVariable("userId") final Long userId, @RequestBody @Valid final EarnPointRequest request) {
        EarnPoint earnPoint = pointFacade.earnPoint(userId, request);
        return new CustomResponse.Builder().addData("earnPointId", earnPoint.getEarnPointId()).build();
    }

    @Operation(summary = "회원별 포인트 차감")
    @ApiResponse(responseCode = "200", description = "API 요청 성공", content = {
            @Content(
                    schema = @Schema(implementation = CustomResponse.class),
                    examples = {
                            @ExampleObject(value = "{\"status\":{\"number\":200,\"code\":\"OK\",\"message\":\"OK\"},\"data\":{}}")
                    }
            )
    })
    @PostMapping("/points/deduct")
    public CustomResponse deductPoint(@PathVariable("userId") final Long userId, @RequestBody @Valid final DeductPointRequest request) {
        pointFacade.deductPoint(userId, request);
        return new CustomResponse.Builder().build();
    }

    @Operation(summary = "회원별 포인트 차감 취소(사용한 포인트 복구)")
    @ApiResponse(responseCode = "200", description = "API 요청 성공", content = {
            @Content(
                    schema = @Schema(implementation = CustomResponse.class),
                    examples = {
                            @ExampleObject(value = "{\"status\":{\"number\":200,\"code\":\"OK\",\"message\":\"OK\"},\"data\":{}}")
                    }
            )
    })
    @PostMapping("/points/cancel-deduct")
    public CustomResponse cancelDeductPoint(@PathVariable("userId") final Long userId, @RequestBody @Valid final CancelDeductPointRequest request) {
        pointFacade.cancelDeductPoint(userId, request);
        return new CustomResponse.Builder().build();
    }

}


