package com.electronic.medicine.repository;

import com.electronic.medicine.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface UserRepository extends JpaRepository<Users,Long> {
}
