package com.example.charityManagementSystem.services;

import java.time.LocalDateTime;
import java.util.Optional;
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
public class PasswordVerificationTokenService {

    @Autowired
    private AdminRepo adminRepo;

    @Autowired
    private DonorRepo donorRepo;

    @Autowired
    private NeedyRepo needyRepo;



    public String generatePasswordResetToken(Admin admin, Donor donor, Needy needy) {
        String token = UUID.randomUUID().toString();

        // For Donor role
        if (donor != null) {
            Optional<Donor> optional_db_donor = donorRepo.findById(donor.getId());
            if (optional_db_donor.isPresent()) {
                Donor db_donor = optional_db_donor.get();
                db_donor.setPasswordResetToken(token); // Set the token directly
                db_donor.setPassexpriyDateTime(LocalDateTime.now().plusHours(1)); // Set expiry date
                donorRepo.save(db_donor);
                System.out.println("Donor password requested !! with passtoken: " + token);
            }

        }
        // For Admin role
        else if (admin != null) {
            Optional<Admin> optional_db_admin = adminRepo.findById(admin.getId());
            if (optional_db_admin.isPresent()) {
                Admin db_admin = optional_db_admin.get();
                db_admin.setPasswordResetToken(token); // Set the token directly
                db_admin.setPassexpriyDateTime(LocalDateTime.now().plusHours(1)); // Set expiry date
                adminRepo.save(db_admin);
            }
        }
        // For Needy role
        else if (needy != null) {
            Optional<Needy> optional_db_needy = needyRepo.findById(needy.getId());
            if (optional_db_needy.isPresent()) {
                Needy db_needy = optional_db_needy.get();
                db_needy.setPasswordResetToken(token); // Set the token directly
                db_needy.setPassexpriyDateTime(LocalDateTime.now().plusHours(1)); // Set expiry date
                needyRepo.save(db_needy);
            }
        }

        return token; // Return the generated token directly
    }






    public boolean validatePasswordResetToken(String urltoken, String email, char role, String new_password) {

        if ("1".equals(String.valueOf(role))) {
            // Admin Role Validation
            Optional<Admin> optional_admin = adminRepo.findByEmail(email);
            if (optional_admin.isPresent()) {
                Admin admin = optional_admin.get();

                if (urltoken.equals(admin.getPasswordResetToken())) {
                    if (admin.getPassexpriyDateTime().isBefore(LocalDateTime.now())) {
                        throw new IllegalArgumentException("Token has expired for admin");
                    }
                    else{
                        admin.setPassword(new_password);
                        adminRepo.save(admin);
                    }
                } else {
                    throw new IllegalArgumentException("Token is invalid for admin");
                }

            } else {
                throw new IllegalArgumentException("Admin not found");
            }

        } else if ("2".equals(String.valueOf(role))) {
            // Donor Role Validation
            Optional<Donor> optional_donor = donorRepo.findByEmail(email);
            if (optional_donor.isPresent()) {
                Donor donor = optional_donor.get();

                if (urltoken.equals(donor.getPasswordResetToken())) {
                    if (donor.getPassexpriyDateTime().isBefore(LocalDateTime.now())) {
                        throw new IllegalArgumentException("Token has expired for donor");
                    }
                    else{
                        donor.setPassword(new_password);
                        donorRepo.save(donor);
                    }
                } else {
                    throw new IllegalArgumentException("Token is invalid for donor");
                }

            } else {
                throw new IllegalArgumentException("Donor not found");
            }

        } else if ("3".equals(String.valueOf(role))) {
            // Needy Role Validation
            Optional<Needy> optional_needy = needyRepo.findByEmail(email);
            if (optional_needy.isPresent()) {
                Needy needy = optional_needy.get();

                if (urltoken.equals(needy.getPasswordResetToken())) {
                    if (needy.getPassexpriyDateTime().isBefore(LocalDateTime.now())) {
                        throw new IllegalArgumentException("Token has expired for needy user");
                    }
                    else{
                        needy.setPassword(new_password);
                        needyRepo.save(needy);
                    }
                } else {
                    throw new IllegalArgumentException("Token is invalid for needy user");
                }

            } else {
                throw new IllegalArgumentException("Needy user not found");
            }

        } else {
            throw new IllegalArgumentException("Invalid user type");
        }

        return true;
    }

}
