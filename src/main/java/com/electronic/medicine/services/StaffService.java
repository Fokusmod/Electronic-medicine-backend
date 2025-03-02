package com.electronic.medicine.services;

import com.electronic.medicine.DTO.ReceptionDto;
import com.electronic.medicine.DTO.ReceptionRequest;
import com.electronic.medicine.DTO.SpecialistReception;
import com.electronic.medicine.entity.Reception;
import com.electronic.medicine.entity.Role;
import com.electronic.medicine.entity.Speciality;
import com.electronic.medicine.entity.User;
import com.electronic.medicine.exception.MedicineServerErrorException;
import com.electronic.medicine.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class StaffService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final UserService userService;
    private final ReceptionService receptionService;


    public List<User> getUsersByRole(String role) {
        Role currentRole = roleService.getByTitle(role);
        return userRepository.findAllByRoles(currentRole);
    }

    public List<User> findStaffUsers(String param, String text) {
        List<User> all = getUsersByRole(param);
        return sortedByText(all, text);
    }

    public List<User> getAllStaff() {
        Role adminRole = roleService.getByTitle("ADMIN");
        Role specialistRole = roleService.getByTitle("SPECIALIST");
        List<User> admin = getUsersByRole(adminRole.getTitle());
        List<User> spec = getUsersByRole(specialistRole.getTitle());
        Set<User> result = new HashSet<>();
        result.addAll(admin);
        result.addAll(spec);
        return result.stream().toList();
    }

    public List<User> getAllSpecialistByParam(String param) {
        List<User> all = userRepository.findAllByRoles(roleService.getByTitle("SPECIALIST"));
        List<User> result = new ArrayList<>();
        for (User user : all) {
            Set<Speciality> specList = user.getSpecialities();
            for (Speciality speciality : specList) {
                if (speciality.getTitle().equals(param)) {
                    result.add(user);
                }
            }
        }
        return result;
    }

    public void setSpecialistReception(ReceptionRequest reception) {
        Reception rec = new Reception();
        rec.setReceptionTime(parserRequestReceptionToDate(reception.getDate(),reception.getTime()));
        receptionService.saveReception(rec);
        User user = userService.findById(reception.getId());
        user.getReceptions().add(rec);
        userService.saveUser(user);
    }

    private Date parserRequestReceptionToDate(String date, String time) {
        ZoneId zoneId = ZoneId.of("Europe/Moscow");
        String[] dateArray = date.split("-");
        String[] timeArray = time.split(":");

        LocalDateTime localDateTime = LocalDateTime.of(
                Integer.parseInt(dateArray[0]),
                Integer.parseInt(dateArray[1]),
                Integer.parseInt(dateArray[2]),
                Integer.parseInt(timeArray[0]),
                Integer.parseInt(timeArray[1]),
                0);

        ZonedDateTime zonedDateTime = localDateTime.atZone(zoneId);
        return Date.from(Instant.from(zonedDateTime));
    }

    public SpecialistReception getSpecialistReceptionByDate(String specialisation, String fullName, Date date) {
        Date moscowDate = getMoscowDate(date);
        User user;
        List<ReceptionDto> result;
        if (fullName.equals("false")) {
            user = getRandomSpecialistBySpecialisation(specialisation);
        } else {
            user = getCurrentSpecialistByFullName(fullName);
        }
        if (user != null) {
            result = getReceptionByDate(user, moscowDate);
            log.debug("Получен список записей на приём ко врачу {} на {}", user.getId(), date);
        } else {
            throw new MedicineServerErrorException("Серверная ошибка. Указанный специалист не найден на сервере.");
        }
        return new SpecialistReception(user.getId(), result);
    }

    public SpecialistReception getSpecialistReceptionByDate(Long id, Date date) {
        Date moscowDate = getMoscowDate(date);
        User user = userService.findById(id);
        List<ReceptionDto> result = getReceptionByDate(user,moscowDate);
        result.sort(new Comparator<ReceptionDto>() {
            @Override
            public int compare(ReceptionDto o1, ReceptionDto o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        });
        log.debug("Получен список записей на приём ко врачу {} на {}", user.getId(), date);
        return new SpecialistReception(user.getId(), result);
    }

    public List<ReceptionDto> getReceptionByDate(User user, Date date) {
        Set<Reception> receptions = user.getReceptions();
        List<ReceptionDto> result = new ArrayList<>();
        ZoneId zoneId = ZoneId.of("Europe/Moscow");
        for (Reception reception : receptions) {
            LocalDate dateReception = LocalDate.ofInstant(reception.getReceptionTime().toInstant(), zoneId);
            LocalDate currentDate = LocalDate.ofInstant(date.toInstant(), zoneId);
            if (dateReception.isEqual(currentDate)) {
                LocalTime localTime =LocalTime.ofInstant(reception.getReceptionTime().toInstant(), zoneId);
                result.add(new ReceptionDto(localTime.toString()));
            }
        }
        return result;
    }

    public void setAdminRole(Long id) {
        Role adminRole = roleService.getByTitle("ADMIN");
        User currentUser = userService.findById(id);
        checkUserRole(currentUser);
        currentUser.getRoles().add(adminRole);
        userService.saveUser(currentUser);
    }

    private void checkUserRole(User user) {
        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
            if (role.getTitle().equals("USER")) {
                roles.remove(role);
                break;
            }
        }
    }

    public void deleteAdminRole(Long id) {
        Role adminRole = roleService.getByTitle("ADMIN");
        User currentUser = userService.findById(id);
        if (currentUser.getRoles().size() == 1) {
            Role userRole = roleService.getByTitle("USER");
            currentUser.getRoles().add(userRole);
        }
        currentUser.getRoles().remove(adminRole);
        userService.saveUser(currentUser);
    }

    public void downgradeSpecialistToUser(Long id) {
        Role userRole = roleService.getByTitle("USER");
        User currentUser = userService.findById(id);
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        currentUser.setRoles(roles);
        userService.saveUser(currentUser);
    }


    private User getCurrentSpecialistByFullName(String fullName) {
        List<User> specByName = findStaffUsers("SPECIALIST", fullName);
        if (specByName.isEmpty()) {
            log.debug("Нет специалистов c именем {} в базе данных ", fullName);
            return null;
        }
        return specByName.get(0);
    }

    private User getRandomSpecialistBySpecialisation(String specialisation){
        List<User> specBySpecialisation = findStaffUsers("SPECIALIST",specialisation);
        if (specBySpecialisation.isEmpty()) {
            log.debug("Нет специалистов co специализацией {} в базе данных ", specialisation);
            return null;
        }
        int random = (int )(Math.random() * specBySpecialisation.size() + 1) - 1;
        return specBySpecialisation.get(random);
    }

    private Date getMoscowDate(Date date) {
        ZoneId zoneId = ZoneId.of("Europe/Moscow");
        LocalDate localDate = LocalDate.ofInstant(date.toInstant(), zoneId);
        return Date.from(Instant.from(localDate.atStartOfDay(zoneId)));
    }


    private List<User> sortedByText(List<User> users, String text) {
        List<User> result = new ArrayList<>();
        for (User user : users) {
            String username = (user.getFirstName() + " " + user.getLastName()).toLowerCase();
            String start = text.toLowerCase();
            if (username.startsWith(start)) {
                result.add(user);
            }
        }
        for (User user : users) {
            Set<Speciality> specs = user.getSpecialities();
            for (Speciality spec : specs) {
                String specialisation = spec.getTitle().toLowerCase();
                String start = text.toLowerCase();
                if (specialisation.startsWith(start)) {
                    result.add(user);
                }
            }
        }
        return result;
    }



}
