package com.electronic.medicine.services;

import com.electronic.medicine.entity.Speciality;
import com.electronic.medicine.entity.User;
import com.electronic.medicine.exception.MedicineNotFound;
import com.electronic.medicine.repository.SpecialityRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SpecialityServiceTest {

    @InjectMocks
    private SpecialityService specialityService;
    @Mock
    private SpecialityRepository specialityRepository;
    @Mock
    private UserService userService;
    @Mock
    private RoleService roleService;

    @Test
    void loadSpecialityServiceContext() {
        assertNotNull(specialityService);
        assertNotNull(specialityRepository);
        assertNotNull(userService);
        assertNotNull(roleService);
    }

    @Test
    @DisplayName("setUserSpeciality успешное выполнение")
    void setUserSpeciality_Success() {
        Speciality speciality = mock(Speciality.class);
        when(specialityRepository.getByTitle(speciality.getTitle())).thenReturn(Optional.of(speciality));
        when(userService.findById(1L)).thenReturn(mock(User.class));
        specialityService.setUserSpeciality(1L,speciality.getTitle());
        verify(userService).findById(1L);
        verify(specialityRepository).getByTitle(speciality.getTitle());

    }

    @Test
    @DisplayName("getByTitle успешное выполнение")
    void getByTitle_Success() {
        String title = "Specialisation";
        Speciality testSpeciality = new Speciality(1L,title);
        Optional<Speciality> speciality = Optional.of(testSpeciality);
        when(specialityRepository.getByTitle(title)).thenReturn(speciality);
        var result = specialityService.getByTitle(title);
        assertEquals(testSpeciality, result);
        verify(specialityRepository).getByTitle(title);
        verifyNoMoreInteractions(specialityRepository);
    }

    @Test
    @DisplayName("getByTitle успешное выполнение")
    void getByTitle_NotFound() {
        String title = "Specialisation";
        MedicineNotFound ex = new MedicineNotFound("error");
        when(specialityRepository.getByTitle(title)).thenThrow(ex);
        var result = assertThrows(MedicineNotFound.class,()->specialityService.getByTitle(title));
        assertEquals(ex,result);
        verify(specialityRepository).getByTitle(title);
        verifyNoMoreInteractions(specialityRepository);
    }

    @Test
    @DisplayName("findAll успещное выполнение")
    void findAll_Success() {
        List<Speciality> testList = List.of(
                new Speciality(1L,"One"),
                new Speciality(2L,"Two"),
                new Speciality(3L,"Three"));
        when(specialityRepository.findAll()).thenReturn(testList);
        var result = specialityService.findAll();
        assertEquals(testList,result);
        verify(specialityRepository).findAll();
        verifyNoMoreInteractions(specialityRepository);
    }
}