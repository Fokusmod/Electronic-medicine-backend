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
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@Sql("classpath:sql/specialist.sql")
class StaffControllerIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JwtProvider jwtProvider;



    @Test
    void getAllStaff() throws Exception {
        var mockBuilder = MockMvcRequestBuilders
                .get("/staff/getAllStaff")
                .contentType(MediaType.APPLICATION_JSON);
        this.mockMvc.perform(mockBuilder).andExpectAll(
                status().isOk(),
                content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                jsonPath("$.length()").value(14));
    }

    @Test
    void getAllSpecialists() throws Exception {
        var mockBuilder = MockMvcRequestBuilders
                .get("/staff/getAllSpecialists/SPECIALIST")
                .contentType(MediaType.APPLICATION_JSON);
        this.mockMvc.perform(mockBuilder).andExpectAll(
                status().isOk(),
                content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                jsonPath("$.length()").value(14));
    }

    @Test
    void getAllAdminStuff() throws Exception {
        var mockBuilder = MockMvcRequestBuilders
                .get("/staff/getAllAdminStaff/ADMIN")
                .contentType(MediaType.APPLICATION_JSON);
        this.mockMvc.perform(mockBuilder).andExpectAll(
                status().isOk(),
                content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                jsonPath("$.length()").value(7));
    }

    @Test
    void findSpecialistsBySpecialisation() throws Exception {
        var mockBuilder = MockMvcRequestBuilders
                .get("/staff/getAllSpecialist/Кардиолог")
                .contentType(MediaType.APPLICATION_JSON);
        this.mockMvc.perform(mockBuilder).andExpectAll(
                status().isOk(),
                content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                content().json("""
                        [{"id":3, "firstName": "Анна", "lastName":"Викторовна"}]
                        """),
                jsonPath("$.length()").value(1));
    }

    @Test
    void getSpecialistReceptionByFullName() throws Exception {
        var mockBuilder = MockMvcRequestBuilders
                .get("/staff/getSpecialistReception/Кардиолог/Анна Викторовна/Wed Feb 26 2025 03:00:00 GMT+0300 (Москва, стандартное время)")
                .contentType(MediaType.APPLICATION_JSON);
        this.mockMvc.perform(mockBuilder).andExpectAll(
                status().isOk(),
                content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                jsonPath("list.length()").value(0));
    }

    @Test
    void getSpecialistReceptionById() throws Exception {
        var mockBuilder = MockMvcRequestBuilders
                .get("/staff/getSpecialistReceptionByDate/3/Wed Feb 26 2025 03:00:00 GMT+0300 (Москва, стандартное время)")
                .contentType(MediaType.APPLICATION_JSON);
        this.mockMvc.perform(mockBuilder).andExpectAll(
                status().isOk(),
                content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                jsonPath("list.length()").value(0));
    }

    @Test
    void findUsersByParams() throws Exception {
        var mockBuilder = MockMvcRequestBuilders
                .get("/staff/getSpecialistFinderResult/SPECIALIST/Стом")
                .contentType(MediaType.APPLICATION_JSON);
        this.mockMvc.perform(mockBuilder).andExpectAll(
                status().isOk(),
                content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                jsonPath("$.length()").value(1));
    }

    @Test
    void setSpecialistReception() throws Exception {
        var mockBuilder = MockMvcRequestBuilders
                .post("/staff/setSpecialistReception")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"id":3, "date": "2025-02-26", "time":"16:00"}
                        """);
        this.mockMvc.perform(mockBuilder).andExpect(
                status().isOk());
    }


    @Test
    void setAdminRoleIsForbidden() throws Exception {
        var mockBuilder = MockMvcRequestBuilders
                .put("/staff/setAdminRole/3")
                .contentType(MediaType.APPLICATION_JSON);
        this.mockMvc.perform(mockBuilder).andExpect(
                status().isForbidden());
    }

    @Test
    void setAdminRoleIsOk() throws Exception {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ADMIN"));
        String accessToken = jwtProvider.generateToken(new User("admin", "admin", authorities));
        var mockBuilder = MockMvcRequestBuilders
                .put("/staff/setAdminRole/3")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accessToken);
        this.mockMvc.perform(mockBuilder).andExpect(
                status().isOk());
    }


    @Test
    void deleteAdminRoleIsForbidden() throws Exception {
        var mockBuilder = MockMvcRequestBuilders
                .delete("/staff/deleteAdminRole/3")
                .contentType(MediaType.APPLICATION_JSON);
        this.mockMvc.perform(mockBuilder).andExpect(
                status().isForbidden());
    }

    @Test
    void deleteAdminRole() throws Exception {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ADMIN"));
        String accessToken = jwtProvider.generateToken(new User("admin", "admin", authorities));
        var mockBuilder = MockMvcRequestBuilders
                .delete("/staff/deleteAdminRole/3")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accessToken);
        this.mockMvc.perform(mockBuilder).andExpect(
                status().isOk());
    }

    @Test
    void downgradeSpecialistToUserIsForbidden() throws Exception {
        var mockBuilder = MockMvcRequestBuilders
                .put("/staff/downgradeSpecialistToUser/3")
                .contentType(MediaType.APPLICATION_JSON);
        this.mockMvc.perform(mockBuilder).andExpect(
                status().isForbidden());
    }

    @Test
    void downgradeSpecialistToUserIsOk() throws Exception {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ADMIN"));
        String accessToken = jwtProvider.generateToken(new User("admin", "admin", authorities));
        var mockBuilder = MockMvcRequestBuilders
                .put("/staff/downgradeSpecialistToUser/3")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accessToken);
        this.mockMvc.perform(mockBuilder).andExpect(
                status().isOk());
    }

    @Test
    void findSpecById() throws Exception {
        var mockBuilder = MockMvcRequestBuilders
                .get("/staff/getSpecialistInfo/100")
                .contentType(MediaType.APPLICATION_JSON);
        this.mockMvc.perform(mockBuilder).andExpectAll(
                status().isOk(),
                content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                content().json("""
                            {"id":100,"email":"user100"}
                        """)
        );
    }

    @Test
    void findSpecByUsername() throws Exception {
        var mockBuilder = MockMvcRequestBuilders
                .get("/staff/getSpecialist/user100")
                .contentType(MediaType.APPLICATION_JSON);
        this.mockMvc.perform(mockBuilder).andExpectAll(
                status().isOk(),
                content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                content().json("""
                            {"id":100,"email":"user100"}
                        """)
        );

    }
}