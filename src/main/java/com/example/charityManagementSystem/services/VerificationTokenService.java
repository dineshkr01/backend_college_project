package com.example.charityManagementSystem.services;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.charityManagementSystem.models.Admin;
import com.example.charityManagementSystem.models.Donor;
import com.example.charityManagementSystem.models.Needy;
import com.example.charityManagementSystem.repository.AdminRepo;
import com.example.charityManagementSystem.repository.DonorRepo;
import com.example.charityManagementSystem.repository.NeedyRepo;

@Service
public class VerificationTokenService {

    @Autowired
    private AdminRepo adminRepo;

    @Autowired
    private DonorRepo donorRepo;

    @Autowired
    private NeedyRepo needyRepo;

    

    public String generateToken(Admin admin, Donor donor, Needy needy) {
        String token = UUID.randomUUID().toString();

        if (donor != null) {
            Donor db_donor = donorRepo.findById(donor.getId()).orElse(null);
            if (db_donor == null && donor.getUsername() != null) {
                db_donor = donorRepo.findByUsername(donor.getUsername());
            }

            if (db_donor != null) {
                db_donor.setEmailVerificationToken(token);
                db_donor.setExpriyDateTime(LocalDateTime.now().plusHours(1));
                donorRepo.save(db_donor);
                System.out.println("Donor email token set successfully !!");
            } else {
                throw new IllegalArgumentException("Donor not found.");
            }

        }

        else if (admin != null) {
            Admin db_admin = adminRepo.findById(admin.getId()).orElse(null);
            if (db_admin == null && admin.getUsername() != null) {
                db_admin = adminRepo.findByUsername(admin.getUsername());
            }

            if (db_admin != null) {
                db_admin.setEmailVerificationToken(token);
                db_admin.setExpriyDateTime(LocalDateTime.now().plusHours(1));
                adminRepo.save(db_admin);
            } else {
                throw new IllegalArgumentException("Admin not found.");
            }
        }

        else if (needy != null) {
            Needy db_needy = needyRepo.findById(needy.getId()).orElse(null);
            if (db_needy == null && needy.getUsername() != null) {
                db_needy = needyRepo.findByUsername(needy.getUsername());
            }

            if (db_needy != null) {
                db_needy.setEmailVerificationToken(token);
                db_needy.setExpriyDateTime(LocalDateTime.now().plusHours(1));
                needyRepo.save(db_needy);
            } else {
                throw new IllegalArgumentException("Needy user not found.");
            }
        }

        return token;
    }

   


    public boolean validateToken(String token, char userType, String username) {
        if (userType == '1') {  
            Admin admin = adminRepo.findByUsername(username);
            if (admin == null) {
                return false;
            }
            if (token.equals(admin.getEmailVerificationToken())) {
                if (admin.getExpriyDateTime().isBefore(LocalDateTime.now())) {
                    return false;
                }
            } else {
                return false;
            }
    
        } else if (userType == '2') {  
            Donor donor = donorRepo.findByUsername(username);
            System.out.println("Donor from db is : " + donor);
            if (donor == null) {
                return false;
            }
    
            if (token.equals(donor.getEmailVerificationToken())) {
                if (donor.getExpriyDateTime().isBefore(LocalDateTime.now())) {
                    return false;
                }
            } else {
                return false;
            }
    
        } else if (userType == '3') {  
            Needy needy = needyRepo.findByUsername(username);
            if (needy == null) {
                return false;
            }
    
            if (token.equals(needy.getEmailVerificationToken())) {
                if (needy.getExpriyDateTime().isBefore(LocalDateTime.now())) {
                    return false;
                }
            } else {
                return false;
            }
    
        } else {
            return false;
        }
    
        return true;
    }
    

}
