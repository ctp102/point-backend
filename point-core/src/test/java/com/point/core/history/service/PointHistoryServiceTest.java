//package com.point.core.history.service;
//
//import com.point.core.history.domain.PointHistory;
//import com.point.core.history.form.PointHistoryForm;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//
//import java.util.List;
//
//@SpringBootTest
//public class PointHistoryServiceTest {
//
//    @Autowired
//    private PointHistoryService pointHistoryService;
//
//    @Test
//    @DisplayName("회원별 포인트 적립/차감/취소 내역 조회")
//    void getPointHistoryListTest() {
//        // given
//        PointHistoryForm pointHistoryForm = new PointHistoryForm();
//        Pageable pageable = PageRequest.of(0, 10);
//
//        // when
//        Page<PointHistory> result = pointHistoryService.findPointHistoryList(globalUserId, pointHistoryForm, pageable);
//        List<PointHistory> pointHistoryList = result.getContent();
//
//        // then
//        Assertions.assertThat(pointHistoryList.size()).isEqualTo(0); // setUp 메서드 호출 시 회원 가입만 하고 포인트는 지급하지 않으므로 size는 0이 맞음
//    }
//
//}
