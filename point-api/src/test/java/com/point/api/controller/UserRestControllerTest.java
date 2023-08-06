package com.point.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.point.core.earn.dto.EarnPointRequest;
import com.point.core.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
public class UserRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("회원 가입 및 초기 포인트 적립")
    void saveUserTest() throws Exception {
        Long savePoint = 10_000L;

        EarnPointRequest earnPointRequest = EarnPointRequest.of(savePoint);

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(earnPointRequest)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        userRepository.findById(1L).ifPresent(user -> {
            Assertions.assertThat(user.getRemainPoint()).isEqualTo(savePoint);
        });
    }

}
