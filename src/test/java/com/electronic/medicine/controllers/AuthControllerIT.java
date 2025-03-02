package com.electronic.medicine.controllers;

import com.electronic.medicine.DTO.JwtRefreshRequest;
import com.electronic.medicine.utils.JwtProvider;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
class AuthControllerIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JwtProvider jwtProvider;

    @Test
    void createAuthToken_ReturnTokens() throws Exception {
        var mockBuilder = MockMvcRequestBuilders
                .post("/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"email":"admin", "password": "admin"}
                        """);
        this.mockMvc.perform(mockBuilder).andExpectAll(
                status().isOk(),
                content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                jsonPath("$.length()").value(2));
    }

    @Test
    void refreshAuthTokenTest() throws Exception{
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ADMIN"));
        String accessToken = jwtProvider.generateToken(new User("admin", "admin", authorities));
        String refreshToken = jwtProvider.generateRefreshToken(new User("admin", "admin", authorities));

        JwtRefreshRequest jwtRefreshRequest = new JwtRefreshRequest();
        jwtRefreshRequest.setEmail("admin");
        jwtRefreshRequest.setRefreshToken(refreshToken);
        String json = new ObjectMapper().writeValueAsString(jwtRefreshRequest);
        var mockBuilder = MockMvcRequestBuilders
                .post("/refreshToken")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accessToken)
                .content(json);
        this.mockMvc.perform(mockBuilder).andExpectAll(
                status().isOk(),
                content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                jsonPath("$.length()").value(2));
    }

    @Test
    void activateAccountTest() throws Exception{
       var mockBuilder = MockMvcRequestBuilders
                .post("/activate")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"code":"1234-1234-1234-1234"}
                        """);
        this.mockMvc.perform(mockBuilder).andExpectAll(
                status().isBadRequest());

    }
}