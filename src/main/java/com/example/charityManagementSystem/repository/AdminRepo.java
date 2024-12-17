package com.example.charityManagementSystem.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.charityManagementSystem.models.Admin;

@Repository
public interface AdminRepo extends JpaRepository<Admin, Long> {

    Admin findByUsername(String username);

    Optional<Admin> findByEmail(String email);
}
