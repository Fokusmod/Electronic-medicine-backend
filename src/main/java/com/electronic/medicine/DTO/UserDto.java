package com.electronic.medicine.DTO;

import com.electronic.medicine.entity.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class UserDto {

    private Long id;

    private String email;

    private String firstName;

    private String lastName;

    private Integer age;

    private Date experience;

    private String photoUrl;

    private String status;

    private String position;

    private Set<SpecialityDto> specialities;

    private Set<EducationDto> educations;

    private Set<ReviewDto> reviews;

    private Set<RoleDto> roles;

    public UserDto(Long id, String email, String firstName, String lastName, Integer age, Date experience, String photoUrl, String status, String position, Set<Speciality> specialities, Set<Education> educations, Set<Review> reviews, Set<Role> roles) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.experience = experience;
        this.photoUrl = photoUrl;
        this.status = status;
        this.position = position;
        this.specialities = specialities.stream().map(speciality -> new SpecialityDto(speciality.getTitle())).collect(Collectors.toSet());
        this.educations = educations.stream().map(education -> new EducationDto(education.getTitle())).collect(Collectors.toSet());
        this.reviews = reviews.stream().map(review -> new ReviewDto(review.getAuthor(), review.getMessage())).collect(Collectors.toSet());
        this.roles = roles.stream().map(role -> new RoleDto(role.getTitle())).collect(Collectors.toSet());
    }

    public UserDto(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.age = user.getAge();
        this.experience = user.getExperience();
        this.photoUrl = user.getPhotoUrl() != null ? user.getPhotoUrl().getTitle() : null;
        this.status = user.getStatus() != null ? user.getStatus().getTitle() : null;
        this.position = user.getPosition() != null ? user.getPosition().getTitle() : null;
        this.specialities = user.getSpecialities().stream().map(speciality -> new SpecialityDto(speciality.getTitle())).collect(Collectors.toSet());
        this.educations = user.getEducations().stream().map(education -> new EducationDto(education.getTitle())).collect(Collectors.toSet());
        this.reviews = user.getReviews().stream().map(review -> new ReviewDto(review.getAuthor(), review.getMessage())).collect(Collectors.toSet());
        this.roles = user.getRoles().stream().map(role -> new RoleDto(role.getTitle())).collect(Collectors.toSet());
    }
}
