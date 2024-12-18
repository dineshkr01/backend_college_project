package com.example.charityManagementSystem.controllers;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.charityManagementSystem.services.AdminService;
import com.example.charityManagementSystem.services.DonorService;
import com.example.charityManagementSystem.services.EmailService;
import com.example.charityManagementSystem.services.NeedyService;
import com.example.charityManagementSystem.services.PasswordVerificationTokenService;
import com.example.charityManagementSystem.services.VerificationTokenService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.mail.MessagingException;

import com.example.charityManagementSystem.models.Admin;
import com.example.charityManagementSystem.models.Donation;
import com.example.charityManagementSystem.models.Donor;
import com.example.charityManagementSystem.models.Needy;
import com.example.charityManagementSystem.repository.AdminRepo;
import com.example.charityManagementSystem.repository.DonationRepo;
import com.example.charityManagementSystem.repository.DonorRepo;
import com.example.charityManagementSystem.repository.NeedyRepo;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    DonorService donorservice;

    @Autowired
    AdminService adminservice;

    @Autowired
    NeedyService needyservice;

    @Autowired
    AdminRepo adminRepo;

    @Autowired
    DonorRepo donorRepo;

    @Autowired
    NeedyRepo needyRepo;

    @Autowired
    DonationRepo donationRepo;

    @Autowired
    VerificationTokenService verifyEmailToken;

    @Autowired
    PasswordVerificationTokenService passwordService;

    @Autowired
    EmailService mailservice;

    @Autowired
    private ObjectMapper objectMapper;

    private Admin convertToAdmin(Map<String, Object> userData) {
        return objectMapper.convertValue(userData, Admin.class);
    }

    private Donor convertToDonor(Map<String, Object> userData) {
        return objectMapper.convertValue(userData, Donor.class);
    }

    private Needy convertToNeedy(Map<String, Object> userData) {
        return objectMapper.convertValue(userData, Needy.class);
    }

    @PostMapping("/public/register")
    public ResponseEntity<String> registerUser(@RequestBody Map<String, Object> userData,
            @RequestParam("role") char role) {
        String successMessage = "";

        if (role == '1') {
            System.out.println("Admin is registering");
            Admin adminUser = convertToAdmin(userData);
            adminUser.setUsername('1' + adminUser.getUsername());
            adminservice.addAdmin(adminUser);
            successMessage = "Admin registered successfully";
        } else if (role == '2') {
            System.out.println("Donor is registering");
            Donor donorUser = convertToDonor(userData);
            donorUser.setUsername('2' + donorUser.getUsername());
            donorservice.addDonor(donorUser);
            successMessage = "Donor registered successfully";
        } else if (role == '3') {
            System.out.println("Needy is registering");
            Needy needyUser = convertToNeedy(userData);
            needyUser.setUsername('3' + needyUser.getUsername());
            needyservice.addNeedy(needyUser);
            successMessage = "Needy registered successfully";
        } else {
            System.out.println("Invalid is registering");
            return ResponseEntity.status(400).body("Invalid user role or user type");
        }

        return ResponseEntity.ok(successMessage);
    }

    @PostMapping("/public/login")
    public ResponseEntity<String> login(@RequestBody Map<String, Object> userRequest, @RequestParam("role") char role) {
        String jwtToken = null;
        System.out.println("Login from database");
        try {
            if (role == '1') {
                Admin adminUser = convertToAdmin(userRequest);
                jwtToken = adminservice.getAdminByUsername(adminUser, role);
            } else if (role == '2') {
                Donor donorUser = convertToDonor(userRequest);
                jwtToken = donorservice.getDonorByUsername(donorUser, role);
            } else if (role == '3') {
                Needy needyUser = convertToNeedy(userRequest);
                jwtToken = needyservice.getNeedyByUsername(needyUser, role);
            } else {
                return ResponseEntity.status(400).body("Invalid role");
            }

            if (jwtToken != null) {
                return ResponseEntity.ok(jwtToken);
            } else {
                return ResponseEntity.status(404).body("Invalid username or password");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error during login: " + e.getMessage());
        }
    }

    @PostMapping("/checkEmailVerifyDonor")
    public ResponseEntity<Boolean> checkEmailVerifyDonor(@RequestBody Map<String, Object> user,
            @RequestParam(name = "role") char role) {

        boolean is_checked = false;

        try {
            if (role == '1') {
                Admin adminUser = convertToAdmin(user);
                is_checked = adminservice.checkEmailVerifyAdmin(adminUser);
            } else if (role == '2') {
                Donor donorUser = convertToDonor(user);
                is_checked = donorservice.checkEmailVerifyDonor(donorUser);
            } else if (role == '3') {
                Needy needyUser = convertToNeedy(user);
                is_checked = needyservice.checkEmailVerifyNeedy(needyUser);
            } else {
                return ResponseEntity.ok(false);
            }

            return ResponseEntity.ok(is_checked);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(false);
        }
    }

    @PostMapping("/sendVerificationEmail")
    public ResponseEntity<String> verifyEmail(@RequestBody Map<String, Object> user, @RequestParam("role") char role)
            throws MessagingException {

        System.out.println("Sending mail for email verification !!");

        String email = null;
        String username = null;
        String token = null;

        try {
            if (role == '1') {
                Admin adminUser = convertToAdmin(user);
                email = adminUser.getEmail();
                token = verifyEmailToken.generateToken(adminUser, null, null);
                username = role + adminUser.getUsername();
            } else if (role == '2') {
                Donor donorUser = convertToDonor(user);
                email = donorUser.getEmail();
                token = verifyEmailToken.generateToken(null, donorUser, null);
                username = role + donorUser.getUsername();
            } else if (role == '3') {
                Needy needyUser = convertToNeedy(user);
                email = needyUser.getEmail();
                token = verifyEmailToken.generateToken(null, null, needyUser);
                username = role + needyUser.getUsername();
            } else {
                return ResponseEntity.status(400).body("Invalid user role or user type.");
            }

            if (email == null || email.isEmpty()) {
                return ResponseEntity.status(400).body("Email is required");
            }

            String url = "http://localhost:8085/api/public/verify-email?token=" + token + "&role=" + role
                    + "&username=" + username;
            String subject = "Email Verification Link";
            String htmlContent = "<html><body>"
                    + "<h3>Hello,</h3>"
                    + "<p>Please click the following link to verify your email:</p>"
                    + "<p><a href=\"" + url + "\">Verify Email</a></p>"
                    + "<p>If you did not request this, please ignore this email.</p>"
                    + "</body></html>";

            mailservice.sendEmail(email, subject, htmlContent);
            return ResponseEntity.ok("Verification email sent successfully!");

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error during sending verification email: " + e.getMessage());
        }
    }

    @GetMapping("/public/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestParam("token") String token, @RequestParam("role") char role,
            @RequestParam("username") String username) {

        System.out.println("Verify mail is called : " + token);
        System.out.println(role + " " + username);
        try {
            if (verifyEmailToken.validateToken(token, role, username)) {
                if (role == '1') {
                    Admin db_admin = adminRepo.findByUsername(username);
                    if (db_admin != null) {
                        db_admin.setEmailVerificationStatus(true);
                        adminRepo.save(db_admin);
                    } else {
                        throw new IllegalArgumentException("Admin not found");
                    }
                } else if (role == '2') {
                    Donor db_donor = donorRepo.findByUsername(username);
                    if (db_donor != null) {
                        db_donor.setEmailVerificationStatus(true);
                        donorRepo.save(db_donor);
                    } else {
                        throw new IllegalArgumentException("Donor not found");
                    }
                } else if (role == '3') {
                    Needy db_needy = needyRepo.findByUsername(username);
                    if (db_needy != null) {
                        db_needy.setEmailVerificationStatus(true);
                        needyRepo.save(db_needy);
                    } else {
                        throw new IllegalArgumentException("Needy not found");
                    }
                } else {
                    throw new IllegalArgumentException("Invalid role");
                }

                return ResponseEntity.ok("Email successfully verified!");
            } else
                return ResponseEntity.ok("Email not verified!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid or expired token: " + e.getMessage());
        }
    }

    @PostMapping("/public/sendResetPasswordMail")
    public ResponseEntity<String> ResetPassword(@RequestBody Map<String, Object> user,
            @RequestParam(name = "role") char role) throws UnsupportedEncodingException {

        String passToken = null;
        String user_email;

        try {
            if (role == '1') {
                Admin adminUser = convertToAdmin(user);
                Optional<Admin> db_admin = adminRepo.findByEmail(adminUser.getEmail());
                user_email = adminUser.getEmail();
                if (db_admin.isPresent()) {
                    Admin org_admin = db_admin.get();
                    passToken = passwordService.generatePasswordResetToken(org_admin, null, null);
                }

            } else if (role == '2') {
                Donor donorUser = convertToDonor(user);
                Optional<Donor> db_donor = donorRepo.findByEmail(donorUser.getEmail());
                user_email = donorUser.getEmail();
                if (db_donor.isPresent()) {
                    Donor org_donor = db_donor.get();
                    passToken = passwordService.generatePasswordResetToken(null, org_donor, null);
                }

            } else if (role == '3') {
                Needy needyUser = convertToNeedy(user);
                Optional<Needy> db_needy = needyRepo.findByEmail(needyUser.getEmail());
                user_email = needyUser.getEmail();
                if (db_needy.isPresent()) {
                    Needy org_needy = db_needy.get();
                    passToken = passwordService.generatePasswordResetToken(null, null, org_needy);
                }

            } else {
                return ResponseEntity.ok("Invalid role, no password reset sent");
            }

            if (passToken != null) {
                String url = "http://localhost:8085/api/public/verifyResetPass?token=" + passToken
                        + "&email=" + user_email + "&role=" + role;
                String subject = "Password Reset Link";
                String htmlContent = "<html><body>"
                        + "<h3>Hello,</h3>"
                        + "<p>Please click the following link to Reset your password:</p>"
                        + "<p><a href=\"" + url + "\">Reset Password</a></p>"
                        + "<p>If you did not request this, please ignore this email.</p>"
                        + "</body></html>";

                mailservice.sendEmail(user_email, subject, htmlContent);
                return ResponseEntity.ok("Password reset email sent successfully.");
            } else {
                return ResponseEntity.status(400).body("User not found.");
            }

        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred during password reset: " + e.getMessage());
        }
    }

    @PutMapping("/public/ResetPass")
    public ResponseEntity<String> ResetPassword(@RequestParam("token") String passToken,
            @RequestParam("email") String email, @RequestParam("role") char role,
            @RequestBody Map<String, Object> user) {

        String new_password = null;
        if (role == '1') {
            Admin adminUser = convertToAdmin(user);
            Optional<Admin> db_admin = adminRepo.findByEmail(adminUser.getEmail());
            if (db_admin.isPresent()) {
                Admin org_admin = db_admin.get();
                new_password = org_admin.getPassword();
            }

        } else if (role == '2') {
            Donor donorUser = convertToDonor(user);
            Optional<Donor> db_donor = donorRepo.findByEmail(donorUser.getEmail());
            if (db_donor.isPresent()) {
                Donor org_donor = db_donor.get();
                new_password = org_donor.getPassword();
            }

        } else if (role == '3') {
            Needy needyUser = convertToNeedy(user);
            Optional<Needy> db_needy = needyRepo.findByEmail(needyUser.getEmail());
            if (db_needy.isPresent()) {
                Needy org_needy = db_needy.get();
                new_password = org_needy.getPassword();
            }

        } else {
            return ResponseEntity.ok("Invalid role, no password reset sent");
        }
        try {
            if (passwordService.validatePasswordResetToken(passToken, email, role, new_password))
                return ResponseEntity.ok("Password reset successfully !!");
            else
                return ResponseEntity.ok("Page error try again!!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid or expired token: " + e.getMessage());
        }
    }

    @PutMapping("/ResetEmail")
    public ResponseEntity<String> ResetEmail(@RequestParam("role") char role,
            @RequestBody Map<String, Object> user) {

        if (role == '1') {
            Admin adminUser = convertToAdmin(user);
            Optional<Admin> db_admin = adminRepo.findByEmail(adminUser.getEmail());
            if (db_admin.isPresent()) {
                Admin org_admin = db_admin.get();
                org_admin.setEmail(adminUser.getEmail());
                adminRepo.save(org_admin);
            }

        } else if (role == '2') {
            Donor donorUser = convertToDonor(user);
            Optional<Donor> db_donor = donorRepo.findByEmail(donorUser.getEmail());
            if (db_donor.isPresent()) {
                Donor org_donor = db_donor.get();
                org_donor.setEmail(donorUser.getEmail());
                donorRepo.save(org_donor);
            }

        } else if (role == '3') {
            Needy needyUser = convertToNeedy(user);
            Optional<Needy> db_needy = needyRepo.findByEmail(needyUser.getEmail());
            if (db_needy.isPresent()) {
                Needy org_needy = db_needy.get();
                org_needy.setEmail(needyUser.getEmail());
                needyRepo.save(org_needy);
            }
        }

        else {
            return ResponseEntity.ok("Email Reset failed !!");
        }
        return ResponseEntity.ok("Email Reset successfully !!");
    }

    @PutMapping("/direct/ResetPassword")
    public ResponseEntity<String> DirectResetPassword(@RequestParam("role") char role,
            @RequestBody Map<String, Object> user) {

        if (role == '1') {
            Admin adminUser = convertToAdmin(user);
            Optional<Admin> db_admin = adminRepo.findByEmail(adminUser.getEmail());
            if (db_admin.isPresent()) {
                Admin org_admin = db_admin.get();
                org_admin.setPassword(adminUser.getPassword()); 
                adminRepo.save(org_admin); 
            }

        } else if (role == '2') {
            Donor donorUser = convertToDonor(user);
            Optional<Donor> db_donor = donorRepo.findByEmail(donorUser.getEmail());
            if (db_donor.isPresent()) {
                Donor org_donor = db_donor.get();
                org_donor.setPassword(donorUser.getPassword());
                donorRepo.save(org_donor); 
            }

        } else if (role == '3') {
            Needy needyUser = convertToNeedy(user);
            Optional<Needy> db_needy = needyRepo.findByEmail(needyUser.getEmail());
            if (db_needy.isPresent()) {
                Needy org_needy = db_needy.get();
                org_needy.setPassword(needyUser.getPassword()); 
                needyRepo.save(org_needy); 
            }
        }

        else {
            return ResponseEntity.ok("Password Reset failed !!");
        }
        return ResponseEntity.ok("Password Reset successfully !!");
    }



    @PostMapping("/donateToOrg")
    public ResponseEntity<String> DonateToOrg(@RequestParam ("donor_username") String donor_username, 
    @RequestParam("donor_orgId") Long donor_orgId){
        return null;
    }


    @GetMapping("/getDonationData")
    public ResponseEntity<Map<String, Double>> getDonationData(@RequestParam ("donorUserID") Long donorUserID){
        Optional<Donor> db_Donor = donorRepo.findById(donorUserID);
        Map<String, Double> map = new HashMap<>();
        if(db_Donor.isPresent()){
            Donor org_db_donor = db_Donor.get();
            map.put(org_db_donor.getUsername(), (double)0);
            List<Donation> donation_list = new ArrayList<>();
            double Sum_of_donation = 0.0;
            donation_list = donationRepo.findByDonorId(donorUserID);
            for(int i=0; i<donation_list.size(); i++){
                Donation current_donation = donation_list.get(i);
                Sum_of_donation += current_donation.getDonatedAmount();
            }
            map.put(org_db_donor.getUsername(), Sum_of_donation);
        }
        return ResponseEntity.ok(map);
    }



    @GetMapping("/getDonatedAmount")
    public ResponseEntity<Double> getDonatedAmount(@RequestParam ("NeedyUserID") Long orgID){
        Optional<Needy> db_Needy = needyRepo.findById(orgID);
        double Sum_of_donation = 0.0;
        if(db_Needy.isPresent()){
            Needy org_db_needy = db_Needy.get();
            Sum_of_donation = org_db_needy.getCollectedDonation();
        }
        return ResponseEntity.ok(Sum_of_donation);
    }

}
