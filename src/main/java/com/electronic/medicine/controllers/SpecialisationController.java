package com.electronic.medicine.controllers;

import com.electronic.medicine.DTO.SpecialityDto;
import com.electronic.medicine.services.SpecialityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/spec")
public class SpecialisationController {

    private final SpecialityService specialityService;


    @GetMapping("/all")
    public List<SpecialityDto> getAllSpeciality() {
        return specialityService.findAll().stream().filter(item -> !item.getTitle().equals("none")).map(spec -> new SpecialityDto(
                spec.getTitle())).toList();
    }

    @PutMapping("/setSpecialisationUser/{id}/{specialisation}")
    public void setSpecialisationUser(@PathVariable Long id, @PathVariable String specialisation) {
        specialityService.setUserSpeciality(id, specialisation);
    }
}
