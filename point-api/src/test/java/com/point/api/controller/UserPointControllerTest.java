package com.point.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.point.core.deduct.dto.CancelDeductPointRequest;
import com.point.core.deduct.dto.DeductPointRequest;
import com.point.core.earn.dto.EarnPointRequest;
import com.point.core.user.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserPointControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Test
    @Order(1)
    @DisplayName("[1] 회원 가입 및 초기 포인트 적립")
    void saveUserTest() throws Exception {
        Long savePoint = 10_000L;

        EarnPointRequest earnPointRequest = EarnPointRequest.of(savePoint);

        String uri = "/api/v1/users";

        mockMvc.perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(earnPointRequest)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        userRepository.findById(1L)
                .ifPresent(user -> assertThat(user.getRemainPoint()).isEqualTo(savePoint));
    }

    @Test
    @Order(2)
    @DisplayName("[2] 포인트 적립")
    void earnPointTest() throws Exception {
        Long userId = 1L;
        Long earnPoint = 5_000L;
        EarnPointRequest request = EarnPointRequest.of(earnPoint);

        String uri = "/api/v1/users/{userId}/points/earn".replace("{userId}", String.valueOf(userId));

        mockMvc.perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        userRepository.findById(1L)
                .ifPresent(user -> assertThat(user.getRemainPoint()).isEqualTo(15_000L));
    }

    @Test
    @Order(3)
    @DisplayName("[3] 포인트 차감")
    void deductPointTest() throws Exception {
        Long userId = 1L;
        Long deductPoint = 5_000L;
        DeductPointRequest request = DeductPointRequest.of(deductPoint);

        String uri = "/api/v1/users/{userId}/points/deduct".replace("{userId}", String.valueOf(userId));

        mockMvc.perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        userRepository.findById(1L)
                .ifPresent(user -> assertThat(user.getRemainPoint()).isEqualTo(10_000L));
    }

    @Test
    @Order(4)
    @DisplayName("[4] 포인트 차감 취소")
    void cancelDeductPointTest() throws Exception {
        Long userId = 1L;
        Long cancelPoint = 5_000L;
        CancelDeductPointRequest request = new CancelDeductPointRequest(cancelPoint);

        String uri = "/api/v1/users/{userId}/points/cancel-deduct".replace("{userId}", String.valueOf(userId));

        mockMvc.perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        userRepository.findById(1L)
                .ifPresent(user -> assertThat(user.getRemainPoint()).isEqualTo(15_000L));
    }

    @Test
    @Order(5)
    @DisplayName("[5] 회원별 잔여 포인트 조회")
    void getUserPointTest() throws Exception {
        Long userId = 1L;

        String uri = "/api/v1/users/{userId}/points".replace("{userId}", String.valueOf(userId));

        mockMvc.perform(get(uri))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        userRepository.findById(1L)
                .ifPresent(user -> assertThat(user.getRemainPoint()).isEqualTo(15_000L));
    }

    @Test
    @Order(6)
    @DisplayName("[6] 회원별 포인트 적립/차감/취소 내역 조회")
    void getPointHistoryListTest() throws Exception {
        Long userId = 1L;

        String uri = "/api/v1/users/{userId}/points/history".replace("{userId}", String.valueOf(userId));

        mockMvc.perform(get(uri))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        userRepository.findById(1L)
                .ifPresent(user -> assertThat(user.getRemainPoint()).isEqualTo(15_000L));
    }

}
