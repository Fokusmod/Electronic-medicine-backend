package com.electronic.medicine.repository;

import com.electronic.medicine.entity.Speciality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpecialityRepository extends JpaRepository<Speciality, Long> {

    Optional<Speciality> getByTitle(String title);

    void deleteByTitle(String title);
}
