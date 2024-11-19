package com.OEzoa.OEasy.api.member;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
public class SimpleTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("서버 상태 확인")
    public void testServerIsRunning() throws Exception {
        // 왜 안되냐고 너 진짜 싸울래?
        mockMvc.perform(get("/"))
                .andExpect(status().isOk()); // 응답 상태 200 OK
    }
}