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

import com.example.charityManagementSystem.repository.DonorRepo;
import com.example.charityManagementSystem.models.Donor;

@Service
public class DonorService {

    @Autowired
    private DonorRepo donorrepo;

    @Autowired
    AuthenticationManager authManager;

    @Autowired
    JWTService jwtservice;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public ResponseEntity<String> addDonor(@RequestBody Donor donor) {
        System.out.println(donor.toString());
        donor.setPassword(encoder.encode(donor.getPassword()));
        donorrepo.save(donor);
        return ResponseEntity.ok("Donor added successfully");
    }

    public String getDonorByUsername(Donor donor, char role_number) {
        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(
                role_number + donor.getUsername(),
                donor.getPassword()));

        if (authentication.isAuthenticated()) {
            return jwtservice.generateToken(role_number + donor.getUsername());
        } else {
            return null;
        }
    }

    public boolean checkEmailVerifyDonor(Donor donor) {
        Optional<Donor> db_donor = donorrepo.findById(donor.getId());
        if (db_donor.isPresent()) {
            Donor exist_db_user = db_donor.get();
            if(exist_db_user.getEmailVerificationStatus())
            return true;
            else return false;
        } 
        return false;
    }

}
