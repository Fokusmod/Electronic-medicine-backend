package com.electronic.medicine.services;

import com.electronic.medicine.DTO.RegistrationUserDto;
import com.electronic.medicine.entity.Role;
import com.electronic.medicine.entity.User;
import com.electronic.medicine.exception.MedicineBadCredential;
import com.electronic.medicine.exception.MedicineNotFound;
import com.electronic.medicine.exception.MedicineServerErrorException;
import com.electronic.medicine.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleService roleService;
    @Mock
    private MailSender mailSender;

    @Test
    void loadUserServiceContext() {
        assertNotNull(userRepository);
        assertNotNull(roleService);
        assertNotNull(mailSender);
        assertNotNull(userService);
    }


    @Test
    @DisplayName("getAllUsers успешное выполнение")
    void getAllUsers_Success() {
        String title = "USER";
        Role role = new Role(1L, title);
        List<User> testList = List.of(
                mock(User.class),
                mock(User.class),
                mock(User.class));
        when(roleService.getByTitle(title)).thenReturn(role);
        when(userRepository.findAllByRoles(role)).thenReturn(testList);
        var result = userService.getAllUsers();
        assertEquals(testList, result);
        verify(userRepository).findAllByRoles(role);
        verify(roleService).getByTitle(title);
    }

    @Test
    @DisplayName("getByEmail успешное выполнение")
    void getByEmail_Success() {
        String email = "email@mail.ru";
        Optional<User> testUser = Optional.of(mock(User.class));
        when(userRepository.getByEmail(email)).thenReturn(testUser);
        var result = userRepository.getByEmail(email);
        assertEquals(testUser,result);
        verify(userRepository).getByEmail(email);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    @DisplayName("getByActivationCode успешное выполнение")
    void getByActivationCode_Success() {
        String code = "1234-1234-1234-1234";
        Optional<User> testUser = Optional.of(mock(User.class));
        when(userRepository.getByActivationCode(code)).thenReturn(testUser);
        var result = userRepository.getByActivationCode(code);
        assertEquals(testUser,result);
        verify(userRepository).getByActivationCode(code);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    @DisplayName("findById успешное выполнение")
    void findById_Success() {
        Long id = 1L;
        Optional<User> testUser = Optional.of(mock(User.class));
        when(userRepository.findById(id)).thenReturn(testUser);
        var result = userRepository.findById(id);
        assertEquals(testUser,result);
        verify(userRepository).findById(id);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    @DisplayName("loadUserByUsername успешное выполнение")
    void loadUserByUsername_Success() {
        String username = "admin";
        User user = new User();
        user.setEmail(username);
        user.setPassword("password123");
        Role role = new Role();
        role.setTitle("ROLE_USER");
        user.setRoles(Set.of(role));
        when(userRepository.getByEmail(username)).thenReturn(Optional.of(user));
        var result = userService.loadUserByUsername(username);
        assertEquals(user.getEmail(), result.getUsername());
        assertEquals(user.getPassword(), result.getPassword());
        assertEquals(1, result.getAuthorities().size());
        assertEquals("ROLE_USER", result.getAuthorities().iterator().next().getAuthority());
    }

    @Test
    @DisplayName("saveUser успешное выполнение")
    void saveUser_Success() {
        User user = any(User.class);
        userService.saveUser(user);
        verify(userRepository).save(user);
    }

    @Test
    @DisplayName("deleteUser успешное выполнение")
    void deleteUser_Success() {
        User user = any(User.class);
        userService.deleteUser(user);
        verify(userRepository).delete(user);
    }

    @Test
    @DisplayName("deleteUserById успешное выполнение")
    void deleteUserById_Success() {
        Long id = 1L;
        userService.deleteUserById(id);
        verify(userRepository).deleteById(id);
    }
}