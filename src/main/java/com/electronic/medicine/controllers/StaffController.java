package com.electronic.medicine.controllers;

import com.electronic.medicine.DTO.*;
import com.electronic.medicine.services.StaffService;
import com.electronic.medicine.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/staff")
public class StaffController {

    private final StaffService staffService;

    private final UserService userService;


    @GetMapping("/getAllStaff")
    public List<SpecialistDto> getAllStaff() {
        return staffService.getAllStaff().stream().map(specialist -> new SpecialistDto(
                specialist.getId(),
                specialist.getFirstName(),
                specialist.getLastName(),
                specialist.getPhotoUrl(),
                specialist.getSpecialities(),
                specialist.getStatus(),
                specialist.getRoles())).toList();
    }

    @GetMapping("/getAllSpecialists/{role}")
    public List<SpecialistDto> getAllSpecialists() {
        return staffService.getAllStaff().stream().map(specialist -> new SpecialistDto(
                specialist.getId(),
                specialist.getFirstName(),
                specialist.getLastName(),
                specialist.getPhotoUrl(),
                specialist.getSpecialities(),
                specialist.getStatus(),
                specialist.getRoles())).toList();
    }


    @GetMapping("/getAllAdminStaff/{role}")
    public List<AdminStaffDto> getAllAdminStuff(@PathVariable String role) {
        return staffService.getUsersByRole(role).stream().map(admin -> new AdminStaffDto(
                        admin.getId(),
                        admin.getFirstName(),
                        admin.getLastName(),
                        admin.getPhotoUrl(),
                        admin.getSpecialities(),
                        admin.getPosition())).toList();
    }

    @GetMapping("/getAllSpecialist/{param}")
    public List<SpecialistDto> findSpecialistsByParam(@PathVariable String param) {
        return staffService.getAllSpecialistByParam(param).stream().map(spec -> new SpecialistDto(
                spec.getId(),
                spec.getFirstName(),
                spec.getLastName(),
                spec.getPhotoUrl(),
                spec.getSpecialities(),
                spec.getStatus(),
                spec.getRoles())).toList();
    }

    @GetMapping("/getSpecialistReception/{specialisation}/{fullName}/{date}")
    public SpecialistReception getSpecialistReception(@PathVariable String specialisation, @PathVariable String fullName, @PathVariable Date date) {
        return staffService.getSpecialistReceptionByDate(specialisation, fullName, date);

    }

    @GetMapping("/getSpecialistReceptionByDate/{id}/{date}")
    public SpecialistReception getSpecialistReception(@PathVariable Long id, @PathVariable Date date) {
        return staffService.getSpecialistReceptionByDate(id, date);

    }

    @GetMapping("/getSpecialistFinderResult/{param}/{text}")
    public List<SpecialistDto> findUsersByParams(@PathVariable String param, @PathVariable String text) {
        return staffService.findStaffUsers(param, text).stream()
                .filter(item -> !item.getEmail().equals("admin"))
                .map(admin -> new SpecialistDto(
                        admin.getId(),
                        admin.getFirstName(),
                        admin.getLastName(),
                        admin.getPhotoUrl(),
                        admin.getSpecialities(),
                        admin.getStatus(),
                        admin.getRoles())).toList();
    }

    @PostMapping("/setSpecialistReception")
    public void setSpecialistReception(@RequestBody ReceptionRequest reception) {
        staffService.setSpecialistReception(reception);
    }

    @PutMapping("/setAdminRole/{id}")
    public void setAdminRole(@PathVariable Long id) {
        staffService.setAdminRole(id);
    }

    @DeleteMapping("/deleteAdminRole/{id}")
    public void deleteAdminRole(@PathVariable Long id) {
        staffService.deleteAdminRole(id);
    }

    @PutMapping("/downgradeSpecialistToUser/{id}")
    public void downgradeSpecialistToUser(@PathVariable Long id) {
        staffService.downgradeSpecialistToUser(id);
    }

    @GetMapping("/getSpecialistInfo/{id}")
    public UserDto findSpecById(@PathVariable Long id) {
        return new UserDto(userService.findById(id));
    }

    @GetMapping("/getSpecialist/{username}")
    public UserDto findSpecByUsername(@PathVariable String username) {
        return new UserDto(userService.getByEmail(username));
    }
}

