package com.electronic.medicine.DTO;

import com.electronic.medicine.entity.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class SpecialistDto {

    private Long id;

    private String firstName;

    private String lastName;

    private String photoUrl;

    private List<SpecialityDto> specialities;

    private String status;

    private List<RoleDto> roles;

    public SpecialistDto(Long id, String firstName, String lastName, Photo photoUrl, Set<Speciality> specialities, Status status, Set<Role> roles) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.photoUrl = photoUrl != null ?  photoUrl.getTitle(): null;
        this.specialities = specialities.stream().map(speciality -> new SpecialityDto(speciality.getTitle())).collect(Collectors.toList());
        this.status = status != null ? status.getTitle(): null;
        this.roles = roles.stream().map(role -> new RoleDto(role.getTitle())).toList();
    }
}
