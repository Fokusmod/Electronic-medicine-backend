package com.electronic.medicine.services;

import com.electronic.medicine.DTO.*;
import com.electronic.medicine.entity.User;
import com.electronic.medicine.exception.MedicineAuthException;
import com.electronic.medicine.exception.MedicineBadCredential;
import com.electronic.medicine.exception.MedicineServerErrorException;
import com.electronic.medicine.utils.JwtProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private UserService userService;
    @Mock
    private JwtProvider jwtProvider;

    private final ActivateRequest activateRequest = new ActivateRequest("1234-1234-1234-1234");
    private final ActivateResponse activateResponse = new ActivateResponse("Учётная запись подтверждена, вы можете войти в систему");

    private final JwtRequest incorrectRequest = new JwtRequest("admEn", "admEnpassword");
    private final JwtRequest correctRequest = new JwtRequest("admin", "adminpassword");

    private final JwtRefreshRequest jwtRefreshBadRequest = new JwtRefreshRequest("admEn", "asadaghkjkajskdasd.akakgg.fdjgkdkjghx-===c");
    private final JwtRefreshRequest jwtRefreshValidRequest = new JwtRefreshRequest("admin", "asadaghkjkajskdasd.akakgg.fdjgkdkjghxc");
    private final JwtResponse jwtResponse = new JwtResponse
            ("asadaghkjkajskdasd.akakgg.fdjgkdkjghxc",
                    "asdasdkhkhlsdjvas.pqpweijqweml.akshjghaj");

    @Test
    void IsLoadAuthControllerContext() {
        assertNotNull(authenticationManager);
        assertNotNull(userService);
        assertNotNull(jwtProvider);
        assertNotNull(authService);
    }

    @Test
    @DisplayName("createAuthToken вернёт ошибку MedicineAuthException")
    void createAuthToken_ReturnMedicineAuthException() {
        MedicineAuthException authException =
                new MedicineAuthException("Неверно введён логин или пароль. Попробуйте ввести данные заново.");

        doThrow(authException)
                .when(this.authenticationManager)
                .authenticate(new UsernamePasswordAuthenticationToken(
                        incorrectRequest.getEmail(),
                        incorrectRequest.getPassword()));
        var result = assertThrowsExactly(MedicineAuthException.class, () -> this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                incorrectRequest.getEmail(),
                incorrectRequest.getPassword())));
        assertEquals(result, authException);
        Mockito.verify(this.authenticationManager).authenticate(new UsernamePasswordAuthenticationToken(
                incorrectRequest.getEmail(),
                incorrectRequest.getPassword()));
        Mockito.verifyNoMoreInteractions(this.authenticationManager);
    }


    @Test
    @DisplayName("createAuthToken вернёт ошибку UsernameNotFoundException")
    void createAuthToken_ReturnUsernameNotFoundException() {
        UsernameNotFoundException ex = new UsernameNotFoundException("123");
        when(userService.loadUserByUsername(correctRequest.getEmail())).thenThrow(ex);
        var result = assertThrowsExactly(UsernameNotFoundException.class, () -> this.userService.loadUserByUsername(correctRequest.getEmail()));
        assertEquals(ex, result);
        Mockito.verify(this.userService).loadUserByUsername(correctRequest.getEmail());
        Mockito.verifyNoMoreInteractions(this.userService);
    }

    @Test
    @DisplayName("createAuthToken вернёт токены доступа")
    void createAuthToken_Success() {
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                "admin",
                "admin",
                List.of(new SimpleGrantedAuthority("USER")));
        when(jwtProvider.generateToken(userDetails)).thenReturn(jwtResponse.getAccessToken());
        when(jwtProvider.generateRefreshToken(userDetails)).thenReturn(jwtResponse.getRefreshToken());

        var accessResult = this.jwtProvider.generateToken(userDetails);
        var refreshResult = this.jwtProvider.generateRefreshToken(userDetails);

        assertEquals(jwtResponse.getAccessToken(), accessResult);
        assertEquals(jwtResponse.getRefreshToken(), refreshResult);
        Mockito.verify(this.jwtProvider).generateToken(userDetails);
        Mockito.verify(this.jwtProvider).generateRefreshToken(userDetails);
        Mockito.verifyNoMoreInteractions(this.jwtProvider);
    }


    @Test
    @DisplayName("confirmActivateCode() вернёт MedicineBadCredentials")
    void confirmActivateCode_ReturnBadCredentials() {
        var ex = mock(MedicineBadCredential.class);
        User user = new User();
        user.setActivationCode(activateRequest.getCode());
        when(this.userService.getByActivationCode(activateRequest.getCode())).thenThrow(ex);
        var result = assertThrowsExactly(MedicineBadCredential.class, () -> this.authService.confirmActivateCode(activateRequest));
        assertEquals(result, ex);
        Mockito.verify(this.userService, times(1)).getByActivationCode(activateRequest.getCode());
    }

    @Test
    @DisplayName("confirmActivateCode() выполнится успешно")
    void confirmActivateCode_ReturnActivateResponse() {
        User user = new User();
        user.setActivationCode(activateRequest.getCode());
        when(this.userService.getByActivationCode(activateRequest.getCode())).thenReturn(user);
        var result = this.authService.confirmActivateCode(activateRequest);
        assertEquals(activateResponse, result);
        Mockito.verify(this.userService, times(1)).getByActivationCode(activateRequest.getCode());
        Mockito.verify(this.userService, times(1)).saveUser(user);
        Mockito.verify(this.userService, times(1)).sendSuccessActivation(user);
    }

    @Test
    @DisplayName("refreshToken вернёт ошибку UsernameNotFoundException")
    void refreshToken_UsernameNotFoundException() {
        UsernameNotFoundException ex = mock(UsernameNotFoundException.class);
        when(userService.loadUserByUsername(jwtRefreshBadRequest.getEmail())).thenThrow(ex);
        var result = assertThrowsExactly(UsernameNotFoundException.class, () -> this.userService.loadUserByUsername(correctRequest.getEmail()));
        assertEquals(ex, result);
        Mockito.verify(this.userService).loadUserByUsername(correctRequest.getEmail());
        Mockito.verifyNoMoreInteractions(this.userService);
    }

    @Test
    @DisplayName("refreshToken() вернёт ошибку MedicineServerErrorException")
    void refreshToken_ReturnServerError() {
        MedicineServerErrorException ex = new MedicineServerErrorException("123");
        when(this.jwtProvider.checkTokens(jwtRefreshBadRequest.getRefreshToken())).thenThrow(ex);

        var result = assertThrowsExactly(MedicineServerErrorException.class, ()-> this.authService.refreshToken(jwtRefreshBadRequest));
        assertEquals(result,ex);
        Mockito.verify(this.jwtProvider).checkTokens(jwtRefreshBadRequest.getRefreshToken());
        Mockito.verifyNoMoreInteractions(this.jwtProvider);
    }

    @Test
    @DisplayName("refreshToken() вернёт JwtResponse")
    void refreshToken_ReturnJwtResponse() {
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                "admin",
                "admin",
                List.of(new SimpleGrantedAuthority("USER")));
        when(this.userService.loadUserByUsername(jwtRefreshValidRequest.getEmail())).thenReturn(userDetails);
        when(this.jwtProvider.checkTokens(jwtRefreshValidRequest.getRefreshToken())).thenReturn(true);
        when(this.jwtProvider.generateToken(userDetails)).thenReturn(jwtResponse.getAccessToken());
        when(this.jwtProvider.generateRefreshToken(userDetails)).thenReturn(jwtResponse.getRefreshToken());
        var result = this.authService.refreshToken(jwtRefreshValidRequest);
        assertEquals(result,jwtResponse);
        Mockito.verify(this.userService).loadUserByUsername(jwtRefreshValidRequest.getEmail());
        Mockito.verify(this.jwtProvider).checkTokens(jwtRefreshValidRequest.getRefreshToken());
        Mockito.verify(this.jwtProvider).generateToken(userDetails);
        Mockito.verify(this.jwtProvider).generateRefreshToken(userDetails);

    }
}