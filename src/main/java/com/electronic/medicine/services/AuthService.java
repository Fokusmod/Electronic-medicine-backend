package com.electronic.medicine.services;

import com.electronic.medicine.DTO.*;
import com.electronic.medicine.entity.User;
import com.electronic.medicine.exception.MedicineAuthException;
import com.electronic.medicine.exception.MedicineBadCredential;
import com.electronic.medicine.exception.MedicineNotFound;
import com.electronic.medicine.exception.MedicineServerErrorException;
import com.electronic.medicine.utils.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtProvider jwtProvider;


    public JwtResponse createAuthToken(JwtRequest request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        } catch (Exception bad) {
            log.debug("При авторизации неверно введён логин или пароль.");
            throw new MedicineAuthException("Неверно введён логин или пароль. Попробуйте ввести данные заново.");
        }
        checkActivationCode(userService.getByEmail(request.getEmail()));
        UserDetails userDetails = userService.loadUserByUsername(request.getEmail());
        String token = jwtProvider.generateToken(userDetails);
        String refreshToken = jwtProvider.generateRefreshToken(userDetails);
        log.debug("Успешная авторизация пользователя {}", request.getEmail());
        return new JwtResponse(token, refreshToken);
    }

    private void checkActivationCode(User user) {
        if (user.getActivationCode() != null) {
            throw new MedicineNotFound("Вход невозможен. Пользователь находится на этапе активации учётной записи.");
        }
    }

    public ActivateResponse confirmActivateCode(ActivateRequest activateRequest) {
        User existUser = userService.getByActivationCode(activateRequest.getCode());
        existUser.setActivationCode(null);
        userService.saveUser(existUser);
        userService.sendSuccessActivation(existUser);
        return new ActivateResponse("Учётная запись подтверждена, вы можете войти в систему");
    }

    public JwtResponse refreshToken(JwtRefreshRequest jwtRefreshRequest) {
        UserDetails userDetails = userService.loadUserByUsername(jwtRefreshRequest.getEmail());
        boolean result = jwtProvider.checkTokens(jwtRefreshRequest.getRefreshToken());
        if (result) {
            String token = jwtProvider.generateToken(userDetails);
            String refreshToken = jwtProvider.generateRefreshToken(userDetails);
            return new JwtResponse(token, refreshToken);
        } else {
            log.debug("При обновлении токенов данные были некорректны");
            throw new MedicineServerErrorException("Данные рефрешь токена были подменены или не корректны");
        }
    }
}
