package com.electronic.medicine.DTO;

import com.electronic.medicine.entity.Photo;
import com.electronic.medicine.entity.Position;
import com.electronic.medicine.entity.Speciality;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class AdminStaffDto {

    private Long id;

    private String firstName;

    private String lastName;

    private String photoUrl;

    private List<SpecialityDto> specialities;

    private String position;

    public AdminStaffDto(Long id, String firstName, String lastName, Photo photoUrl, Set<Speciality> specialities, Position position) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.photoUrl = photoUrl != null ? photoUrl.getTitle(): null;
        this.specialities = specialities.stream().map(speciality -> new SpecialityDto(speciality.getTitle())).collect(Collectors.toList());
        this.position = position != null ? position.getTitle(): null;
    }
}
