package com.electronic.medicine.services;

import com.electronic.medicine.entity.Role;
import com.electronic.medicine.exception.MedicineNotFound;
import com.electronic.medicine.exception.MedicineServerErrorException;
import com.electronic.medicine.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public Role findById(Long id) {
        Optional<Role> role = roleRepository.findById(id);
        if (role.isEmpty()) {
            log.debug("Не существует роли c id: {} в базе данных", id);
            throw new MedicineNotFound("Ошибка сервера. Не удалось найти указанную роль.");
        } else {
            return role.get();
        }
    }

    public Role getByTitle(String title) {
        Optional<Role> existRole = roleRepository.getByTitle(title);
        if (existRole.isPresent()) {
            return existRole.get();
        } else {
            log.debug("Не существует роли: {} в базе данных", title);
           throw new MedicineServerErrorException("Ошибка сервера. Не удалось найти указанную роль.");
        }

    }
}
