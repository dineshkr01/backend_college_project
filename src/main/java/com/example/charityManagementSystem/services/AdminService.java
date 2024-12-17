package com.example.charityManagementSystem.services;



import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import com.example.charityManagementSystem.repository.AdminRepo;
import com.example.charityManagementSystem.models.Admin;

@Service
public class AdminService {

    @Autowired
    private AdminRepo adminRepo;

    @Autowired
    AuthenticationManager authManager;

    @Autowired
    JWTService jwtservice;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public ResponseEntity<String> addAdmin(Admin admin) {
        System.out.println(admin.toString());
        admin.setPassword(encoder.encode(admin.getPassword()));
        adminRepo.save(admin);
        return ResponseEntity.ok("Admin added successfully");
    }

    public String getAdminByUsername(Admin admin, char role_number) {
        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(
                role_number+admin.getUsername(),
                admin.getPassword()));

        if (authentication.isAuthenticated())
            return jwtservice.generateToken(role_number + admin.getUsername());
        else
            return null;
    }

    public boolean checkEmailVerifyAdmin(Admin admin) {
        Optional<Admin> db_admin = adminRepo.findById(admin.getId());
        if (db_admin.isPresent()) {
            Admin exist_db_admin = db_admin.get();
            if(exist_db_admin.getEmailVerificationStatus())
                return true;
            else 
                return false;
        }
        return false;
    }
    
    
   
}
