package com.electronic.medicine.controllers;

import com.electronic.medicine.DTO.RegistrationUserDto;
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
class UserControllerIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JwtProvider jwtProvider;

    @Test
    void registrationUsersIsBadRequest() throws Exception {
        RegistrationUserDto dto = new RegistrationUserDto();
        dto.setEmail("newuser");
        dto.setPassword("newpassword");
        dto.setConfirmPassword("newpassword");
        String json = new ObjectMapper().writeValueAsString(dto);
        var mockBuilder = MockMvcRequestBuilders
                .post("/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);
        this.mockMvc.perform(mockBuilder).andExpectAll(
                status().isBadRequest());
    }

    @Test
    void registrationUsersIsOk() throws Exception {
        RegistrationUserDto dto = new RegistrationUserDto();
        dto.setEmail("newuser@mail.ru");
        dto.setPassword("newpassword");
        dto.setConfirmPassword("newpassword");
        String json = new ObjectMapper().writeValueAsString(dto);
        var mockBuilder = MockMvcRequestBuilders
                .post("/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);
        this.mockMvc.perform(mockBuilder).andExpectAll(
                status().isOk());
    }

    @Test
    void getAllUserIsForbidden() throws Exception {
        var mockBuilder = MockMvcRequestBuilders
                .get("/getAllUsers")
                .contentType(MediaType.APPLICATION_JSON);
        this.mockMvc.perform(mockBuilder).andExpectAll(
                status().isForbidden());
    }

    @Test
    void getAllUserIsOk() throws Exception {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ADMIN"));
        String accessToken = jwtProvider.generateToken(new User("admin", "admin", authorities));
        var mockBuilder = MockMvcRequestBuilders
                .get("/getAllUsers")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accessToken);
        this.mockMvc.perform(mockBuilder).andExpectAll(
                status().isOk(),
                content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                jsonPath("$.length()").value(1));
    }
}