package com.electronic.medicine.controllers;

import com.electronic.medicine.DTO.RegistrationUserDto;
import com.electronic.medicine.DTO.UserDto;
import com.electronic.medicine.entity.User;
import com.electronic.medicine.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/registration")
    public void registrationUsers(@RequestBody RegistrationUserDto registrationUserDto) {
        userService.createNewUsers(registrationUserDto);
    }

    @GetMapping("/getAllUsers")
    public List<UserDto> getAllUser() {
        return userService.getAllUsers().stream().map(UserDto::new).toList();
    }
}
