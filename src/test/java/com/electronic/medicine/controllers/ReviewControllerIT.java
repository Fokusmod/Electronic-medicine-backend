package com.electronic.medicine.controllers;

import com.electronic.medicine.DTO.ReviewRequest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
class ReviewControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void addReviewIsOk() throws Exception {
        ReviewRequest reviewRequest = new ReviewRequest();
        reviewRequest.setAuthor("Пользователь");
        reviewRequest.setSpecId(3L);
        reviewRequest.setMessage("Классный врач");
        String json = new ObjectMapper().writeValueAsString(reviewRequest);

        var mockBuilder = MockMvcRequestBuilders
                .post("/review/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);
        this.mockMvc.perform(mockBuilder).andExpectAll(
                status().isOk());
    }

    @Test
    void addReviewIsNotFound() throws Exception {
        ReviewRequest reviewRequest = new ReviewRequest();
        reviewRequest.setAuthor("Пользователь");
        reviewRequest.setSpecId(1111L);
        reviewRequest.setMessage("Классный врач");
        String json = new ObjectMapper().writeValueAsString(reviewRequest);

        var mockBuilder = MockMvcRequestBuilders
                .post("/review/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);
        this.mockMvc.perform(mockBuilder).andExpectAll(
                status().isNotFound());
    }
}