package com.electronic.medicine.controllers;

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

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
class SpecialisationControllerIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JwtProvider jwtProvider;

    @Test
    void getAllSpeciality() throws Exception {
        var mockBuilder = MockMvcRequestBuilders
                .get("/spec/all")
                .contentType(MediaType.APPLICATION_JSON);
        this.mockMvc.perform(mockBuilder).andExpectAll(
                status().isOk(),
                content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                jsonPath("$.length()").value(12));
    }

    @Test
    void setSpecialisationUserIsForbidden() throws Exception {
        var mockBuilder = MockMvcRequestBuilders
                .put("/spec/setSpecialisationUser/3/Гинеколог")
                .contentType(MediaType.APPLICATION_JSON);
        this.mockMvc.perform(mockBuilder).andExpectAll(
                status().isForbidden());
    }

    @Test
    void setSpecialisationUserIsOk() throws Exception {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ADMIN"));
        String accessToken = jwtProvider.generateToken(new User("admin", "admin", authorities));
        var mockBuilder = MockMvcRequestBuilders
                .put("/spec/setSpecialisationUser/3/Гинеколог")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accessToken);
        this.mockMvc.perform(mockBuilder).andExpectAll(
                status().isOk());
    }
}