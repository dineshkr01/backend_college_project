package com.example.charityManagementSystem.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.charityManagementSystem.repository.NeedyRepo;
import com.example.charityManagementSystem.models.Needy;

@Service
public class NeedyService {

    @Autowired
    private NeedyRepo needyRepo;

    @Autowired
    AuthenticationManager authManager;

    @Autowired
    JWTService jwtservice;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public ResponseEntity<String> addNeedy(@RequestBody Needy needy) {
        System.out.println(needy.toString());
        needy.setPassword(encoder.encode(needy.getPassword()));
        needyRepo.save(needy);
        return ResponseEntity.ok("Needy person added successfully");
    }

    public String getNeedyByUsername(Needy needy, char role_number) {
        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(
                role_number + needy.getUsername(),
                needy.getPassword()));

        if (authentication.isAuthenticated()) {
            return jwtservice.generateToken(role_number + needy.getUsername());
        } else {
            return null;
        }
    }

    public boolean checkEmailVerifyNeedy(Needy needy) {
        Optional<Needy> db_needy = needyRepo.findById(needy.getId());
        if (db_needy.isPresent()) {
            Needy exist_db_needy = db_needy.get();
            if (exist_db_needy.getEmailVerificationStatus())
                return true;
            else
                return false;
        }
        return false;
    }

}
