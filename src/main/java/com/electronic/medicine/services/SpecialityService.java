package com.electronic.medicine.services;

import com.electronic.medicine.entity.Role;
import com.electronic.medicine.entity.Speciality;
import com.electronic.medicine.entity.User;
import com.electronic.medicine.exception.MedicineNotFound;
import com.electronic.medicine.repository.RoleRepository;
import com.electronic.medicine.repository.SpecialityRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class SpecialityService {

    private final SpecialityRepository specialityRepository;

    private final UserService userService;

    private final RoleService roleService;

    @Transactional
    public void setUserSpeciality(Long id, String specialisation) {
        Set<Speciality> specialities = new HashSet<>();
        specialities.add(getByTitle(specialisation));
        User user = userService.findById(id);
        upgradeUserToSpecialist(user);
        user.setSpecialities(specialities);
        userService.saveUser(user);
    }
    private void upgradeUserToSpecialist(User user) {
        Set<Role> userRoles = user.getRoles();
        for (Role userRole : userRoles) {
            if (userRole.getTitle().equals("USER")) {
                Set<Role> specialistRole = new HashSet<>();
                Role specialist = roleService.getByTitle("SPECIALIST");
                specialistRole.add(specialist);
                user.setRoles(specialistRole);

            }
        }
    }

    public Speciality getByTitle(String title) {
        Optional<Speciality> exist = specialityRepository.getByTitle(title);
        if (exist.isEmpty()) {
            log.debug("Роль {} а базе данных не найдена", title);
            throw new MedicineNotFound("Ошибка сервера. Не удалось найти указанную роль.");
        } else {
            return exist.get();
        }
    }

    public List<Speciality> findAll() {
        return specialityRepository.findAll();
    }
}
