package com.electronic.medicine.repository;

import com.electronic.medicine.entity.Role;
import com.electronic.medicine.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> getByEmail(String email);

    Optional<User> getByActivationCode(String code);

    List<User> findAllByRoles(Role role);

}
