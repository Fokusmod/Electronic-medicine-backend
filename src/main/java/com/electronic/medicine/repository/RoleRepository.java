package com.electronic.medicine.repository;

import com.electronic.medicine.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {

    Optional<Role> getByTitle(String title);

    void deleteByTitle(String title);

}
