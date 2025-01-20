package com.electronic.medicine.services;

import com.electronic.medicine.entity.Users;
import com.electronic.medicine.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {


    private final UserRepository userRepository;




    public List<Users> getAllUsers (){
        return userRepository.findAll();
    }
}
