package com.electronic.medicine.services;

import com.electronic.medicine.entity.Role;
import com.electronic.medicine.exception.MedicineNotFound;
import com.electronic.medicine.repository.RoleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    @InjectMocks
    private RoleService roleService;
    @Mock
    private RoleRepository roleRepository;

    @Test
    void loadRoleServiceContext() {
        assertNotNull(roleRepository);
        assertNotNull(roleService);
    }

    @Test
    @DisplayName("findById успешное выполнение")
    void findById_Success() {
        Long id = 1L;
        Role testRole = new Role(id, "USER");
        Optional<Role> role = Optional.of(testRole);
        when(roleRepository.findById(id)).thenReturn(role);
        var result = roleService.findById(id);
        assertEquals(testRole, result);
        verify(roleRepository).findById(id);
        verifyNoMoreInteractions(roleRepository);
    }

    @Test
    @DisplayName("findById роль не найдена")
    void findById_NotFound() {
        Long id = 100000L;
        MedicineNotFound ex = new MedicineNotFound("error");
        when(roleRepository.findById(id)).thenThrow(ex);
        var result = assertThrows(MedicineNotFound.class, () -> roleService.findById(id));
        assertEquals(ex, result);
        verify(roleRepository).findById(id);
        verifyNoMoreInteractions(roleRepository);
    }

    @Test
    @DisplayName("getByTitle успешное выполнение")
    void getByTitle_Success() {
        String title = "USER";
        Role testRole = new Role(1L, title);
        Optional<Role> role = Optional.of(testRole);
        when(roleRepository.getByTitle(title)).thenReturn(role);
        var result = roleService.getByTitle(title);
        assertEquals(testRole, result);
        verify(roleRepository).getByTitle(title);
        verifyNoMoreInteractions(roleRepository);
    }

    @Test
    @DisplayName("getByTitle роль не найдена")
    void getByTitle_NotFound() {
        String title = "ANOTHER";
        MedicineNotFound ex = new MedicineNotFound("error");
        when(roleRepository.getByTitle(title)).thenThrow(ex);
        var result = assertThrows(MedicineNotFound.class, () -> roleService.getByTitle(title));
        assertEquals(ex, result);
        verify(roleRepository).getByTitle(title);
        verifyNoMoreInteractions(roleRepository);
    }
}