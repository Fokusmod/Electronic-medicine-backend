package com.electronic.medicine.controllers;
import com.electronic.medicine.DTO.*;
import com.electronic.medicine.services.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth")
    public JwtResponse createAuthToken(@RequestBody JwtRequest request) {
       return authService.createAuthToken(request);
    }

    @PostMapping("/refreshToken")
    public JwtResponse refreshAuthToken(@RequestBody JwtRefreshRequest jwtRefreshRequest) {
        return authService.refreshToken(jwtRefreshRequest);
    }

    @PostMapping("/activate")
    public ActivateResponse activateAccount(@RequestBody ActivateRequest activateRequest) {
        return authService.confirmActivateCode(activateRequest);
    }
}
